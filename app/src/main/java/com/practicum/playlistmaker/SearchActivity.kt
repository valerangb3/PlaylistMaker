package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.api.TrackSearch
import com.practicum.playlistmaker.data.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private enum class ErrorType {
        NOT_FOUND, CONNECTIONS_PROBLEMS
    }

    private val trackListAdapter = TrackListAdapter(mutableListOf())
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchTrackApi = retrofit.create(TrackSearch::class.java)

    private lateinit var buttonBack: Button
    private lateinit var resetButton: ImageView
    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorContainer: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var updateRequestButton: Button
    private lateinit var errorImage: ImageView

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.track_list)
        recyclerView.adapter = trackListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun showErrorContainer(errorType: ErrorType) {
        if (trackListAdapter.trackList.isNotEmpty()) {
            trackListAdapter.trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }
        errorContainer.visibility = View.VISIBLE
        errorImage.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        when (errorType) {
            ErrorType.NOT_FOUND -> {
                errorImage.setImageResource(R.drawable.nothing_found)
                errorText.text = getString(R.string.search_error_nothing_found)
                updateRequestButton.visibility = View.GONE
            }
            ErrorType.CONNECTIONS_PROBLEMS -> {
                errorImage.setImageResource(R.drawable.connection_problems)
                errorText.text = getString(R.string.search_error_internet)
                updateRequestButton.visibility = View.VISIBLE
            }
        }
    }

    private fun hideErrorContainer() {
        errorContainer.visibility = View.GONE
        errorText.visibility = View.GONE
        updateRequestButton.visibility = View.GONE
        errorImage.visibility = View.GONE
    }

    private fun makeRequest(searchText: String) {
        searchTrackApi.getTrackList(searchText).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.resultCount!! > 0) {
                        trackListAdapter.trackList.clear()
                        trackListAdapter.trackList.addAll(response.body()?.results!!)
                        hideErrorContainer()
                        trackListAdapter.notifyDataSetChanged()
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        buttonBack = findViewById(R.id.button_back)
        resetButton = findViewById(R.id.reset_button)
        searchInput = findViewById(R.id.search)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        updateRequestButton = findViewById(R.id.updateRequest)
        errorImage = findViewById(R.id.errorImage)

        buttonBack.setOnClickListener {
            finish()
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
            if (trackListAdapter.trackList.isNotEmpty()) {
                trackListAdapter.trackList.clear()
                trackListAdapter.notifyDataSetChanged()
                searchInput.clearFocus()
            }
            //Если нажимаем "X", в случае когда ничего не найдено, то скрываем контейнер с ошибкой
            hideErrorContainer()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    if (resetButton.isVisible) {
                        resetButton.visibility = View.GONE
                        searchInputValue = DEFAULT_INPUT_VALUE
                    }
                } else {
                    if (!resetButton.isVisible) {
                        resetButton.visibility = View.VISIBLE
                    }
                    searchInputValue = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchInput.addTextChangedListener(simpleTextWatcher)

        initRecyclerView()
        handleSearchInput()
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