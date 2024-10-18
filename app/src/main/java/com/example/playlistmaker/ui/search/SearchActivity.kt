package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.data.sharedprefs.usecases.SearchHistoryUseCase
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.domain.models.TracksSearchRequest
import com.example.playlistmaker.ui.media.MediaActivity
import java.util.LinkedList

class SearchActivity : AppCompatActivity() {

    //***Clean Architecture***
    //для запроса Retrofit
    private val interactor = Creator.provideTracksInteractor()
    //Для SharedPreferences
    private val searchHistoryUseCase: SearchHistoryUseCase by lazy {
        Creator.provideSearchHistoryUseCase()
    }

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
    private var trackListHistory = LinkedList<TracksParceling>()

    private val trackList = ArrayList<TracksParceling>()

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
        Creator.initialize(this)

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
        //Нажатие основной список поиска
        adapter.setOnClickListener { track ->
            trackListHistory = searchHistoryUseCase.saveTrack(track)

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
            searchHistoryUseCase.clearHistory()
            trackListHistory = searchHistoryUseCase.getTracks()
            adapterHistory = TracksAdapter(trackListHistory)
            recyclerViewHistory.adapter = adapterHistory
            searchedTracksView.isVisible = false
        }

        //Работа с фокусом inputEditText для показа истории поиска
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            //для проверки не пустой истории
            if (hasFocus && inputEditText.text.isEmpty()) {

                trackListHistory = searchHistoryUseCase.getTracks()
                if (trackListHistory.isNotEmpty()) {
                    trackListHistory = searchHistoryUseCase.getTracks()
                    adapterHistory = TracksAdapter(trackListHistory)
                    recyclerViewHistory.adapter = adapterHistory

                    searchedTracksView.isVisible = true
                    onClearScreen()
                } else {
                    searchedTracksView.isVisible = false
                    inputEditText.hint = ""
                }
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

                if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && trackListHistory.isNotEmpty()) {
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

    //Обработка запроса для Retrofit +CA
    private fun executeRequest(inputText: String) {
        //Сохранение последнего запроса:
        lastRequestStr = inputText
        progressBar.isVisible = true
        onClearScreen()

        val request = TracksSearchRequest(entity = "song", text = inputText)
        interactor.searchTracks(request, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<TracksParceling>) {
                handler.post {
                    progressBar.isVisible = false
                        updateListTracks(foundTracks)
                        onSuccess()
                }
            }

            override fun onError(error: TracksInteractor.ErrorType) {
                handler.post{
                    progressBar.isVisible = false
                    when (error){
                        TracksInteractor.ErrorType.EMPTY_RESPONSE ->{
                            updateListTracks(emptyList())
                            onEmptyResponse()
                        }
                        TracksInteractor.ErrorType.FAILURE ->{
                            updateListTracks(emptyList())
                            onFailureResponse()
                        }
                    }
                }
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
    private fun updateListTracks(data: List<TracksParceling>) {
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