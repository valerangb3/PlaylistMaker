package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.TrackActivity
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.search.presentation.state.TrackListState
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INPUT_VALUE = "INPUT_VALUE"
        const val DEFAULT_INPUT_VALUE = ""
        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

    private lateinit var trackListAdapter: TrackListAdapter

    private lateinit var binding: ActivitySearchBinding

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    private var isClickAllowed = true

    private var textWatcher: TextWatcher? = null

    private lateinit var viewModel: SearchViewModel

    private val handler = Handler(Looper.getMainLooper())

    private fun renderSearchResult(state: TrackListState) {
        when (state) {
            is TrackListState.Loading -> {
                showLoading()
            }
            is TrackListState.SearchContent -> {
                hideLoading()
                showTrackList(state.data)
            }
            is TrackListState.HistoryContent -> {
                showTrackHistoryList(state.data)
            }
            is TrackListState.Error -> {
                hideLoading()
                showErrorContainer(state.error)
            }
        }
    }

    private fun hideLoading() {
        binding.progressBar.gone()
    }

    private fun showLoading() {
        hideHistoryView()
        hideTrackList()
        binding.progressBar.show()
    }

    private fun handleTap(trackItem: Track) {
        viewModel.addToHistory(trackItem)
        if (viewModel.getTracksState().value is TrackListState.HistoryContent) {
            trackListAdapter.trackList = viewModel.getHistoryList().toMutableList()
            trackListAdapter.notifyDataSetChanged()
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
        binding.historyTitle.gone()
        binding.clearHistory.gone()
    }

    private fun showHistoryView() {
        binding.historyTitle.show()
        binding.clearHistory.show()
    }

    private fun addToAdapter(trackList: List<Track>) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
        }
        trackListAdapter.trackList.addAll(trackList)
        trackListAdapter.notifyDataSetChanged()
    }

    private fun showTrackHistoryList(trackList: List<Track>) {
        if (trackList.isNotEmpty()) {
            addToAdapter(trackList)
            showHistoryView()
        }
        hideErrorContainer()
    }

    private fun showTrackList(trackList: List<Track>) {
        addToAdapter(trackList)
        hideErrorContainer()
    }

    private fun showErrorContainer(errorType: ErrorType) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }
        binding.errorContainer.show()
        binding.errorImage.show()
        binding.errorText.show()
        when (errorType) {
            ErrorType.EMPTY_DATA -> {
                binding.errorImage.setImageResource(R.drawable.nothing_found)
                binding.errorText.text = getString(R.string.search_error_nothing_found)
                binding.updateRequest.gone()
            }

            ErrorType.CONNECTION_ERROR -> {
                binding.errorImage.setImageResource(R.drawable.connection_problems)
                binding.errorText.text = getString(R.string.search_error_internet)
                binding.updateRequest.show()
            }
        }
    }

    private fun hideErrorContainer() {
        binding.errorContainer.gone()
        binding.errorText.gone()
        binding.updateRequest.gone()
        binding.errorImage.gone()
    }

    private fun initRecyclerView() {
        trackListAdapter = TrackListAdapter {
            if (clickDebounce()) {
                handleTap(it)
            }
        }
        binding.trackList.adapter = trackListAdapter
        binding.trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun handleSearchInput() {
        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.repeatRequest()
                true
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.factory())[SearchViewModel::class.java]

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            hideTrackList()
        }

        binding.updateRequest.setOnClickListener {
            hideErrorContainer()
            viewModel.repeatRequest()
        }

        if (searchInputValue.isNotEmpty()) {
            binding.search.setText(searchInputValue)
        }

        binding.resetButton.setOnClickListener {
            binding.search.setText(DEFAULT_INPUT_VALUE)
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(binding.resetButton.windowToken, 0)
        }

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            if (binding.search.text.isEmpty()) {
                if (hasFocus) {
                    viewModel.showHistoryList()
                } else {
                    hideTrackList()
                    hideHistoryView()
                    hideErrorContainer()
                }
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.showHistoryList()
                    binding.resetButton.gone()
                } else {
                    if (!binding.resetButton.isVisible) {
                        binding.resetButton.show()
                    }
                    viewModel.searchDebounce(changedText = s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher?.let { tw ->
            binding.search.addTextChangedListener(tw)
        }

        initRecyclerView()
        handleSearchInput()

        viewModel.getTracksState().observe(this) { state ->
            renderSearchResult(state)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { tw ->
            binding.search.removeTextChangedListener(tw)
        }
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