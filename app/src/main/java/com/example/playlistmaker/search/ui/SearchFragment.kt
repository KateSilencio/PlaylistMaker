package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.presentation.models.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel by viewModel<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding

    //для инициализации View
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchedTracksView: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var clearHistoryBtn: com.google.android.material.button.MaterialButton
    private lateinit var placeholderNothingFind: LinearLayout
    private lateinit var placeholderNoConnection: LinearLayout
    private lateinit var updateButton: com.google.android.material.button.MaterialButton
    private lateinit var progressBar: LinearLayout

    private lateinit var adapter: TracksAdapter
    private var adapterHistory: TracksAdapter? = null
    private val trackList = ArrayList<TracksData>()

    companion object {
        private var edit = ""
        private const val SEARCH_QUERY_KEY = "EditText"
        private const val TRACK_KEY = "TRACK"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //инициализация View
        inputEditText = binding.editTextSearchId
        clearButton = binding.clearBtnId
        searchedTracksView = binding.historySearched
        recyclerView = binding.recycler
        recyclerViewHistory = binding.recyclerHistory
        clearHistoryBtn = binding.clearHistoryBtn
        placeholderNothingFind = binding.nothingFind
        placeholderNoConnection = binding.noConnection
        updateButton = binding.updateBtn
        progressBar = binding.progressBar

        //установка RecyclerView, инициализация адаптера
        adapter = TracksAdapter(trackList)
        recyclerView.adapter = adapter

        observers()

        //Установить восстановленные данные в EditText
        if (savedInstanceState != null) {
            inputEditText.setText(edit)
        }

        // Обработка нажатия на кнопку "Done" на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.executeRequest(inputEditText.text.toString())
                true
            }
            false
        }

        //Кнопка Очистить историю
        clearHistoryBtn.setOnClickListener {
            searchViewModel.clearHistory()
        }

        // Нажатие основной список поиска
        adapter.setOnClickListener { track ->
            searchViewModel.onSaveTrackInHistory(track)
            searchedTracksView.isVisible = false
            recyclerView.isVisible = true
            //переход на экран Media
            searchViewModel.onTrackClick(track) {
                val  context = requireContext()
                val intent = Intent(context, MediaActivity::class.java)
                intent.putExtra(TRACK_KEY,it)
                context.startActivity(intent)
            }
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            //Работа с фокусом inputEditText для показа истории поиска
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                searchViewModel.onDebounce(inputEditText.text.toString())
                //список истории проверяется в onShowHistory
                if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) {
                    searchViewModel.onShowHistory()
                    inputEditText.hint = getString(R.string.search)
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

        //Работа с фокусом inputEditText для показа истории поиска
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            //для проверки не пустой истории
            if (hasFocus && inputEditText.text.isEmpty()) {
                searchViewModel.onShowHistory()
            } else {
                searchedTracksView.isVisible = false
                inputEditText.hint = ""
            }
        }

        //Обработка кнопки Обновить Последний запрос
        updateButton.setOnClickListener {
            searchViewModel.updateRequest()
        }

        //Кнопка Очистить EditText
        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.hint = getString(R.string.search)
            //очистка списка треков
            updateListTracks(emptyList())
            onClearScreen()
            //скрытие клавиатуры
            val hideKeyB =
                inputEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyB.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

    }

    private fun observers() {
        searchViewModel.searchStateLive.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.NoData -> showNoData()
            is SearchState.NothingFound -> showNothingFound()
            is SearchState.ConnectionError -> showConnectionError(state.error)
            is SearchState.TrackSearchResults -> showSearchResults(state.results)
            is SearchState.TrackSearchHistory -> {
                if (inputEditText.text.isEmpty() && inputEditText.hasFocus()) {
                    showSearchHistory(state.history)
                }
            }
            else -> {/*Заглушка для дефолтного состояния*/}
        }
    }

    private fun showSearchResults(results: List<TracksData>) {
        updateListTracks(results)
        progressBar.isVisible = false
        recyclerView.isVisible = true
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }

    private fun showLoading() {
        progressBar.isVisible = true
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }

    private fun showNoData() {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
        searchedTracksView.isVisible = false
    }

    //Ф-ии управления видимостью контейнеров
    private fun onClearScreen() {
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = false
    }
    private fun showNothingFound() {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = true
        placeholderNoConnection.isVisible = false
    }

    private fun showConnectionError(error: Int) {
        progressBar.isVisible = false
        recyclerView.isVisible = false
        placeholderNothingFind.isVisible = false
        placeholderNoConnection.isVisible = true
    }

    private fun showSearchHistory(history: MutableList<TracksData>){
        if (adapterHistory == null){
            adapterHistory = TracksAdapter(history)
            recyclerViewHistory.adapter = adapterHistory

            adapterHistory?.setOnClickListener{ track->
                //переход на экран Media
                searchViewModel.onTrackClick(track) {
                    val  context = requireContext()
                    val intent = Intent(context, MediaActivity::class.java)
                    intent.putExtra(TRACK_KEY,it)
                    context.startActivity(intent)
                }
            }

        } else {
            adapterHistory?.updateTracks(history)
        }
        searchedTracksView.isVisible = true
        recyclerView.isVisible = false
    }

    //Функция обновления списка в адаптере и установка видимости контейнеров
    @SuppressLint("NotifyDataSetChanged")
    private fun updateListTracks(data: List<TracksData>) {
        //очистка, доб результата в список, обновление адаптера
        trackList.clear()
        trackList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    //Сохранение EditText
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, edit)
    }

    //Восстановление EditText
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            edit = savedInstanceState.getString(SEARCH_QUERY_KEY, "")
            inputEditText.setText(edit) // Установите текст в EditText
        }
    }

    //Обработка кнопки Назад
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) finish()
//        return true
//    }
}


