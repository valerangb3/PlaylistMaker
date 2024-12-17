package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.player.TrackActivity
import com.practicum.playlistmaker.ui.search.adapter.TrackListAdapter
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.api.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INPUT_VALUE = "INPUT_VALUE"
        const val DEFAULT_INPUT_VALUE = ""
        const val CLICK_DEBOUNCE_DELAY = 1_000L
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private enum class ErrorType {
        NOT_FOUND, CONNECTIONS_PROBLEMS
    }

    private enum class TrackListType {
        TRACK_LIST, TRACK_LIST_HISTORY;
    }

    //Свойство необходимо для определения нажатия для:
    // 1) списка треков из ответа сервера itunes;
    // 2) списка треков из истории.
    private var trackListState = TrackListType.TRACK_LIST_HISTORY

    private val historyTrackList = mutableListOf<Track>()

    private lateinit var loadTracksUseCase: TracksInteractor
    private lateinit var tracksHistoryUseCase: TracksHistoryInteractor

    private lateinit var trackListAdapter: TrackListAdapter

    private lateinit var buttonBack: Button
    private lateinit var resetButton: ImageView
    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorContainer: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var updateRequestButton: Button
    private lateinit var errorImage: ImageView
    private lateinit var historyTitle: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var itemsContainer: LinearLayout

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable {
        makeRequest()
    }

    private fun handleTap(trackItem: Track) {
        tracksHistoryUseCase.addToHistory(trackItem)
        when (trackListState) {
            //Тап истории.
            TrackListType.TRACK_LIST_HISTORY -> {
                trackListAdapter.trackList = tracksHistoryUseCase.getHistory().toMutableList()
                trackListAdapter.notifyDataSetChanged()
            }
            //Тап из списка. Добавляем треки в историю.
            TrackListType.TRACK_LIST -> {
                historyTrackList.clear()
                historyTrackList.addAll(tracksHistoryUseCase.getHistory())
            }
        }
        intent = Intent(this@SearchActivity, TrackActivity::class.java)
        intent.putExtra(Track::class.simpleName, trackItem)
        startActivity(intent)
    }

    private fun hideTrackList() {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }
        //Если нажимаем "X", в случае когда ничего не найдено, то скрываем контейнер с ошибкой
        hideErrorContainer()
        hideHistoryView()
    }

    private fun hideHistoryView() {
        historyTitle.gone()
        clearHistoryButton.gone()
    }

    private fun showHistoryView() {
        historyTitle.show()
        clearHistoryButton.show()
    }

    private fun showTrackList(state: TrackListType, trackList: List<Track>) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
        }
        //В зависимости от состояния наполняем адаптеры нужным нам списком
        when (state) {
            TrackListType.TRACK_LIST -> {
                trackListAdapter.trackList.addAll(trackList)
                hideErrorContainer()
                trackListAdapter.notifyDataSetChanged()
            }
            TrackListType.TRACK_LIST_HISTORY -> {
                if (historyTrackList.isNotEmpty()) {
                    trackListAdapter.trackList.addAll(trackList)
                    trackListAdapter.notifyDataSetChanged()
                    showHistoryView()
                }
            }
        }

    }

    private fun showErrorContainer(errorType: ErrorType) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }
        errorContainer.show()
        errorImage.show()
        errorText.show()
        when (errorType) {
            ErrorType.NOT_FOUND -> {
                errorImage.setImageResource(R.drawable.nothing_found)
                errorText.text = getString(R.string.search_error_nothing_found)
                updateRequestButton.gone()
            }

            ErrorType.CONNECTIONS_PROBLEMS -> {
                errorImage.setImageResource(R.drawable.connection_problems)
                errorText.text = getString(R.string.search_error_internet)
                updateRequestButton.show()
            }
        }
    }

    private fun hideErrorContainer() {
        errorContainer.gone()
        errorText.gone()
        updateRequestButton.gone()
        errorImage.gone()
    }

    private fun initRecyclerView() {
        trackListAdapter = TrackListAdapter {
            if (clickDebounce()) {
                handleTap(it)
            }
        }
        recyclerView = findViewById(R.id.track_list)
        recyclerView.adapter = trackListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun makeRequest() {
        if (searchInput.text.isNotEmpty()) {
            progressBar.show()
            hideTrackList()
            loadTracksUseCase.searchTracks(searchInput.text.toString(), object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: Resource<List<Track>>) {
                    handler.post {
                        progressBar.gone()
                        when (foundTracks) {
                            is Resource.Error -> {
                                showErrorContainer(ErrorType.CONNECTIONS_PROBLEMS)
                            }
                            is Resource.Success -> {
                                if (foundTracks.data.isNotEmpty()) {
                                    showTrackList(TrackListType.TRACK_LIST, foundTracks.data)
                                    //Сохраняем состояние для handleTap
                                    trackListState = TrackListType.TRACK_LIST
                                } else {
                                    showErrorContainer(ErrorType.NOT_FOUND)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun handleSearchInput() {
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //На случай если нажали на кнопку
                handler.removeCallbacks(searchRunnable)
                makeRequest()
                true
            }
            false
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //1) При запуске активити мы должны понять, есть ли у нас итемы в SharedPereferences
        //2) В зависимости от п.1

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        loadTracksUseCase = Creator.provideTracksInteractor()
        tracksHistoryUseCase = Creator.provideTrackHistoryInteractor()

        buttonBack = findViewById(R.id.button_back)
        resetButton = findViewById(R.id.reset_button)
        searchInput = findViewById(R.id.search)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        updateRequestButton = findViewById(R.id.updateRequest)
        errorImage = findViewById(R.id.errorImage)
        historyTitle = findViewById(R.id.historyTiyle)
        clearHistoryButton = findViewById(R.id.clearHistory)

        itemsContainer = findViewById(R.id.ItemsContainer)
        progressBar = findViewById(R.id.ProgressBar)

        buttonBack.setOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
            tracksHistoryUseCase.remove()
            historyTrackList.clear()
            hideTrackList()
        }

        updateRequestButton.setOnClickListener {
            hideErrorContainer()
            makeRequest()
        }

        if (searchInputValue.isNotEmpty()) {
            searchInput.setText(searchInputValue)
        }

        resetButton.setOnClickListener {
            searchInput.setText(DEFAULT_INPUT_VALUE)
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(resetButton.windowToken, 0)
        }

        searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchInput.text.isEmpty()) {
                showTrackList(TrackListType.TRACK_LIST_HISTORY, tracksHistoryUseCase.getHistory())
                //Сохраняем состояние для handleTap
                trackListState = TrackListType.TRACK_LIST_HISTORY
            } else {
                hideTrackList()
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    resetButton.gone()
                    searchInputValue = DEFAULT_INPUT_VALUE
                    //Сохраняем состояние для handleTap
                    trackListState = TrackListType.TRACK_LIST_HISTORY
                    hideTrackList()
                    showTrackList(TrackListType.TRACK_LIST_HISTORY, tracksHistoryUseCase.getHistory())

                    if (searchInput.hasFocus()) {
                        showTrackList(TrackListType.TRACK_LIST_HISTORY, tracksHistoryUseCase.getHistory())
                    }
                } else {
                    if (!resetButton.isVisible) {
                        resetButton.show()
                    }
                    searchInputValue = s.toString()
                    //Скрываем экран, если был список истории
                    if (trackListState == TrackListType.TRACK_LIST_HISTORY) {
                        hideTrackList()
                    }
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchInput.addTextChangedListener(simpleTextWatcher)

        initRecyclerView()
        handleSearchInput()
    }

    override fun onStart() {
        super.onStart()
        if (trackListState == TrackListType.TRACK_LIST_HISTORY) {
            historyTrackList.addAll(tracksHistoryUseCase.getHistory())
        }
    }

    override fun onStop() {
        super.onStop()
        if (trackListState == TrackListType.TRACK_LIST_HISTORY) {
            historyTrackList.clear()
            historyTrackList.addAll(trackListAdapter.trackList)
        }
        tracksHistoryUseCase.saveHistory(historyTrackList)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, searchInputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputValue = savedInstanceState.getString(INPUT_VALUE, DEFAULT_INPUT_VALUE)
    }
}