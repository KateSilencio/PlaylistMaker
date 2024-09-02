package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapter.TracksAdapter
import com.example.playlistmaker.data.ITunesResponse
import com.example.playlistmaker.data.TracksFields
import com.example.playlistmaker.retrofit.ITunesSearchAPI
import retrofit2.Call
import retrofit2.Response
import java.util.LinkedList

const val SEARCH_HISTORY = "search_history"
const val EDIT_HISTORY_KEY = "history_search_key"

class SearchActivity : AppCompatActivity() {

    //Для SharedPreferences
    private lateinit var history: SearchHistory

    //View для запроса и работы с Retrofit
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TracksAdapter
    private lateinit var placeholderNothingFind: LinearLayout
    private lateinit var placeholderNoConnection: LinearLayout
    private lateinit var progressBar: LinearLayout
    private lateinit var updateButton: com.google.android.material.button.MaterialButton

    //View для истории поиска
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var clearHistoryBtn: com.google.android.material.button.MaterialButton
    private var adapterHistory: TracksAdapter? = null
    private var trackListHistory = LinkedList<TracksFields>()

    private val trackList = ArrayList<TracksFields>()

    //Handler and Debounce
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchRunnable: Runnable

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_QUERY_KEY = "EditText"
        private var edit = ""
        private var lastRequestStr = ""
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_search)
        val inputEditText = findViewById<EditText>(R.id.edit_text_search_id)
        val clearButton = findViewById<ImageView>(R.id.clear_btn_id)
        val searchedTracksView = findViewById<LinearLayout>(R.id.history_searched)
        recyclerView = findViewById(R.id.recycler)
        recyclerViewHistory = findViewById(R.id.recycler_history)
        clearHistoryBtn = findViewById(R.id.clear_history_btn)
        placeholderNothingFind = findViewById(R.id.nothing_find)
        placeholderNoConnection = findViewById(R.id.no_connection)
        updateButton = findViewById(R.id.update_btn)
        progressBar = findViewById(R.id.progress_bar)

        //Активация тулбара для окна Поиска
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Установить восстановленные данные в EditText
        if (savedInstanceState != null) {
            inputEditText.setText(edit)
        }

        //установка RecyclerView, инициализация адаптера
        adapter = TracksAdapter(trackList)
        recyclerView.adapter = adapter

        /*********** История поиска ************/
        val sharedPrefs = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        history = SearchHistory(sharedPrefs)

        //Нажатие основной список поиска
        adapter.setOnClickListener { track ->
            trackListHistory = history.mainLogicSaveTracks(track)

            adapterHistory = TracksAdapter(trackListHistory)
            recyclerViewHistory.adapter = adapterHistory
        }

        //Нажатие список истории поиска
        adapterHistory?.setOnClickListener { track ->
            val intent = Intent(this, MediaActivity::class.java)
            intent.putExtra("TRACK", track)
            startActivity(intent)
        }

        //Кнопка Очистить историю
        clearHistoryBtn.setOnClickListener {
            history.clearHistory()
            trackListHistory = history.getTracks()
            adapterHistory = TracksAdapter(trackListHistory)
            recyclerViewHistory.adapter = adapterHistory
            searchedTracksView.isVisible = false
        }

        //Работа с фокусом inputEditText для показа истории поиска
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            //для проверки не пустой истории
            val json = sharedPrefs.getString(EDIT_HISTORY_KEY, null)

            if (hasFocus && inputEditText.text.isEmpty() && json != null) {
                trackListHistory = history.getTracks()
                adapterHistory = TracksAdapter(trackListHistory)
                recyclerViewHistory.adapter = adapterHistory

                searchedTracksView.isVisible = true
                onClearScreen()
            } else {
                searchedTracksView.isVisible = false
                inputEditText.hint = ""
            }
        }

        //Кнопка Очистить EditText
        clearButton.setOnClickListener {
            inputEditText.setText("")
            //очистка списка треков
            updateListTracks(emptyList())
            onClearScreen()
            //скрытие клавиатуры
            val hideKeyB =
                inputEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyB.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        //отложенный запрос
        searchRunnable = Runnable {
            if (inputEditText.text.isNotEmpty()) {
                executeRequest(inputEditText.text.toString())
            } else {
                onClearScreen()
            }
        }

        inputEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // Debounce пользовательского ввода
                searchDebounce()

                val json = sharedPrefs.getString(EDIT_HISTORY_KEY, null)
                if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && json != null) {
                    searchedTracksView.isVisible = true
                    onClearScreen()
                } else {
                    searchedTracksView.isVisible = false
                    inputEditText.hint = ""
                }

                if (inputEditText.text.isBlank()) {
                    onClearScreen()
                    return
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                    onClearScreen()
                } else {
                    clearButton.isVisible = true
                }
                edit = inputEditText.text.toString()
            }
        })

        //Обработка кнопки Обновить
        //Последний запрос
        updateButton.setOnClickListener {
            executeRequest(lastRequestStr)
        }

        // Обработка нажатия на кнопку "Done" на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                executeRequest(inputEditText.text.toString())
                true
            }
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    //Обработка запроса для Retrofit
    private fun executeRequest(inputText: String) {

        //Сохранение последнего запроса:
        lastRequestStr = inputText
        progressBar.isVisible = true
        onClearScreen()
        ITunesSearchAPI.ITunesApiCreate.search(entity = "song", text = inputText)
            .enqueue(object : retrofit2.Callback<ITunesResponse> {
                //Успешный ответ
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    progressBar.isVisible = false
                    if (response.isSuccessful) {
                        val result = response.body()?.tracks
                            ?: emptyList()     //Если ответ null, то вернет пустой список

                        if (result.isNotEmpty()) {
                            //результат запроса не пустой:
                            updateListTracks(result)
                            onSuccess()
                            Log.d("SUCCESS", "Успех ${response.code()}")
                        } else {
                            //результат запроса пустой:
                            updateListTracks(emptyList())
                            onEmptyResponse()
                            Log.d("EMPTY", "Пустой ${response.code()}")
                        }
                    } else {
                        //Неудачный запрос
                        updateListTracks(emptyList())
                        onFailureResponse()
                        Log.d("ERROR", "Неудача ${response.code()}")
                    }
                }

                //Ошибка сервера
                override fun onFailure(
                    call: Call<ITunesResponse>,
                    t: Throwable
                ) {
                    progressBar.isVisible = false
                    Log.e("FAIL", "Ошибка $t", t)
                    updateListTracks(emptyList())
                    onFailureResponse()
                }
            })
    }

    //Debounce пользовательского ввода
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    //Функция обновления списка в адаптере и установка видимости контейнеров
    @SuppressLint("NotifyDataSetChanged")
    private fun updateListTracks(data: List<TracksFields>) {
        //очистка, доб результата в список, обновление адаптера
        trackList.clear()
        trackList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    //Ф-ии управления видимостью контейнеров
    private fun onSuccess() {
        recyclerView.isVisible = true
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }

    private fun onEmptyResponse() {
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = true
        placeholderNoConnection.isVisible = false
    }

    private fun onFailureResponse() {
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = true
    }

    private fun onClearScreen() {
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }

    //Сохранение EditText
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, edit)
    }

    //Восстановление EditText
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        edit = savedInstanceState.getString(SEARCH_QUERY_KEY).toString()
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}