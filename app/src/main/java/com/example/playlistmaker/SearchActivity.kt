package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

class SearchActivity : AppCompatActivity() {

    //View для запроса и работы с Retrofit
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TracksAdapter
    private lateinit var placeholderNothingFind: LinearLayout
    private lateinit var placeholderNoConnection: LinearLayout
    private lateinit var updateButton: com.google.android.material.button.MaterialButton

    private val trackList = ArrayList<TracksFields>()
    companion object {
        private const val SEARCH_QUERY_KEY = "EditText"
        private var edit = ""
        private var lastRequestStr = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Активация тулбара для окна Поиска
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_search)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val inputEditText = findViewById<EditText>(R.id.edit_text_search_id)
        val clearButton = findViewById<ImageView>(R.id.clear_btn_id)
        //Установить восстановленные данные в EditText
        if (savedInstanceState != null) {
            inputEditText.setText(edit)
        }

        clearButton.setOnClickListener {
            //очистка строки поиска
            inputEditText.setText("")
            //очистка списка треков
            updateListTracks(emptyList())
            onClearScreen()
            //скрытие клавиатуры
            val hideKeyB =
                inputEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyB.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                } else {
                    clearButton.isVisible = true
                }
                edit = inputEditText.text.toString()
            }
        })

        //установка RecyclerView, инициализация адаптера
        recyclerView = findViewById(R.id.recycler)
        adapter = TracksAdapter(trackList)
        recyclerView.adapter = adapter

        placeholderNothingFind = findViewById(R.id.nothing_find)
        placeholderNoConnection = findViewById(R.id.no_connection)
        updateButton = findViewById(R.id.update_btn)

        //Обработка кнопки Обновить
        //Последний запрос
        updateButton.setOnClickListener{
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

    //Обработка запроса для Retrofit
    private fun executeRequest(inputText: String){

        //Сохранение последнего запроса:
        lastRequestStr = inputText

        ITunesSearchAPI.ITunesApiCreate.search(entity = "song", text = inputText)
            .enqueue(object : retrofit2.Callback<ITunesResponse> {
                //Успешный ответ
                override fun onResponse(call: Call<ITunesResponse>,
                                        response: Response<ITunesResponse>) {

                    if (response.isSuccessful){
                        val result = response.body()?.tracks?: emptyList()     //Если ответ null, то вернет пустой список

                        if (result.isNotEmpty()){
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
                override fun onFailure(call: Call<ITunesResponse>,
                                       t: Throwable) {
                    Log.d("FAIL", "Ошибка $t", t)
                    updateListTracks(emptyList())
                    onFailureResponse()
                }
            })
    }

    //Функция обновления списка в адаптере и установка видимости контейнеров
    @SuppressLint("NotifyDataSetChanged")
    private fun updateListTracks(data: List<TracksFields>){
        //очистка, доб результата в список, обновление адаптера
        trackList.clear()
        trackList.addAll(data)
        adapter.notifyDataSetChanged()
    }
    //Ф-ии управления видимостью контейнеров
    private fun onSuccess(){
        recyclerView.isVisible = true
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }
    private fun onEmptyResponse(){
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = true
        placeholderNoConnection.isVisible = false
    }
    private fun onFailureResponse(){
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = true
    }
    private fun onClearScreen(){
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