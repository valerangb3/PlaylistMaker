package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {

    private val trackList = mutableListOf<Track>()

    init {
        (1..50).map {
            val defaultTrack = DEFAULT_TRACK_LIST[Random.nextInt(0, DEFAULT_TRACK_LIST.size)]
            trackList.add(
                Track(
                    defaultTrack.trackName,
                    defaultTrack.singerName,
                    defaultTrack.duration,
                    defaultTrack.src
                )
            )
        }
    }

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.track_list)
        recyclerView.adapter = TrackListAdapter(trackList.toList())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<Button>(R.id.button_back)
        buttonBack.setOnClickListener {
            finish()
        }

        val resetButton = findViewById<ImageView>(R.id.reset_button)
        val searchInput = findViewById<EditText>(R.id.search)
        if (searchInputValue.isNotEmpty()) {
            searchInput.setText(searchInputValue)
        }
        resetButton.setOnClickListener {
            searchInput.setText(DEFAULT_INPUT_VALUE)

            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(resetButton.windowToken, 0)
            searchInput.clearFocus()
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

        private val DEFAULT_TRACK_LIST = listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine. Loooong string. Loooong string. Loooong string.",
                "Guns N' Roses. Guns N' Roses. Guns N' Roses. Guns N' Roses.",
                "5:035:035:035:035:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
    }
}