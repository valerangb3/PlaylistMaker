package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.track.adapter.TrackListAdapter
import com.practicum.playlistmaker.api.TrackSearch
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.data.TrackResponse
import com.practicum.playlistmaker.track.history.TrackListHistory
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private enum class ErrorType {
        NOT_FOUND, CONNECTIONS_PROBLEMS
    }

    enum class TrackListType {
        TRACK_LIST, TRACK_LIST_HISTORY;
    }

    //Свойство необходимо для определения нажатия для:
    // 1) списка треков из ответа сервера itunes;
    // 2) списка треков из истории.
    private var trackListState = TrackListType.TRACK_LIST_HISTORY

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchTrackApi = retrofit.create(TrackSearch::class.java)
    private var trackListResponse = mutableListOf<Track>()
    private val historyTrackList = mutableListOf<Track>()

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

    private lateinit var userHistory: TrackListHistory

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    private fun handleTap(trackItem: Track) {
        val duplicatePosition = userHistory.getPositionDuplicate(trackItem)
        when (trackListState) {
            //Тап истории перерисовываем: показываем, что логика работает
            TrackListType.TRACK_LIST_HISTORY -> {
                if (duplicatePosition > -1) {
                    userHistory.addToHistory(trackItem)
                    trackListAdapter.trackList.removeAt(duplicatePosition)
                    trackListAdapter.notifyItemRemoved(duplicatePosition)
                    trackListAdapter.notifyItemRangeChanged(duplicatePosition, trackListAdapter.trackList.size)
                    trackListAdapter.trackList.add(0, trackItem)
                    trackListAdapter.notifyItemInserted(0)
                    trackListAdapter.notifyItemRangeChanged(0, trackListAdapter.trackList.size)
                }
            }
            //Тап из списка. Добавляем треки в историю.
            TrackListType.TRACK_LIST -> {
                historyTrackList.clear()
                userHistory.addToHistory(trackItem)
                historyTrackList.addAll(userHistory.getTrackListHistory())
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

    private fun showTrackList(state: TrackListType) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
        }
        //В зависимости от состояния наполняем адаптеры нужным нам списком
        when (state) {
            TrackListType.TRACK_LIST -> {
                trackListAdapter.trackList.addAll(trackListResponse)
                hideErrorContainer()
                trackListAdapter.notifyDataSetChanged()
            }
            TrackListType.TRACK_LIST_HISTORY -> {
                if (historyTrackList.isNotEmpty()) {
                    trackListAdapter.trackList.addAll(historyTrackList)
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
            handleTap(it)
        }
        recyclerView = findViewById(R.id.track_list)
        recyclerView.adapter = trackListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun makeRequest(searchText: String) {
        searchTrackApi.getTrackList(searchText).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    val countResult = response.body()?.resultCount ?: 0
                    if (countResult > 0) {
                        val listResponse = response.body()?.results ?: arrayListOf()
                        if (listResponse.isNotEmpty()) {
                            trackListResponse = listResponse.filter {
                                it.trackName.isNotEmpty() &&
                                it.artistName.isNotEmpty() &&
                                it.trackTimeMillis > 0
                            }.toMutableList()
                            showTrackList(TrackListType.TRACK_LIST)
                            //Сохраняем состояние для handleTap
                            trackListState = TrackListType.TRACK_LIST
                        }
                    } else {
                        showErrorContainer(ErrorType.NOT_FOUND)
                    }
                } else {
                    showErrorContainer(ErrorType.NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showErrorContainer(ErrorType.CONNECTIONS_PROBLEMS)
            }
        })
    }

    private fun handleSearchInput() {
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchInput.text.isNotEmpty()) {
                    makeRequest(searchInput.text.toString())
                } else {
                    trackListAdapter.trackList.clear()
                    trackListAdapter.notifyDataSetChanged()
                }
                true
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //1) При запуске активити мы должны понять, есть ли у нас итемы в SharedPereferences
        //2) В зависимости от п.1

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        buttonBack = findViewById(R.id.button_back)
        resetButton = findViewById(R.id.reset_button)
        searchInput = findViewById(R.id.search)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        updateRequestButton = findViewById(R.id.updateRequest)
        errorImage = findViewById(R.id.errorImage)
        historyTitle = findViewById(R.id.historyTiyle)
        clearHistoryButton = findViewById(R.id.clearHistory)

        userHistory = TrackListHistory(applicationContext)

        buttonBack.setOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
            userHistory.removeTrackHistory()
            historyTrackList.clear()
            hideTrackList()
        }

        updateRequestButton.setOnClickListener {
            makeRequest(searchInput.text.toString())
        }

        if (searchInputValue.isNotEmpty()) {
            searchInput.setText(searchInputValue)
        }

        resetButton.setOnClickListener {
            searchInput.setText(DEFAULT_INPUT_VALUE)
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(resetButton.windowToken, 0)
        }

        searchInput.setOnFocusChangeListener { editText, hasFocus ->
            if (hasFocus && searchInput.text.isEmpty()) {
                showTrackList(TrackListType.TRACK_LIST_HISTORY)
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
                    showTrackList(TrackListType.TRACK_LIST_HISTORY)
                    //нужно фиксить, focus - false
                    if (searchInput.hasFocus()) {
                        showTrackList(TrackListType.TRACK_LIST_HISTORY)
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
            historyTrackList.addAll(userHistory.getTrackListHistory())
        }
    }

    override fun onStop() {
        super.onStop()
        if (trackListState == TrackListType.TRACK_LIST_HISTORY) {
            historyTrackList.clear()
            historyTrackList.addAll(trackListAdapter.trackList)
        }
        userHistory.saveTrackHistory(historyTrackList)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, searchInputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputValue = savedInstanceState.getString(INPUT_VALUE, DEFAULT_INPUT_VALUE)
    }

    companion object {
        const val INPUT_VALUE = "INPUT_VALUE"
        const val DEFAULT_INPUT_VALUE = ""
        const val BASE_URL = "https://itunes.apple.com/"
    }
}