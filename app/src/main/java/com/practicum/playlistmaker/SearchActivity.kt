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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    private var searchInputValue: String = DEFAULT_INPUT_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        /*Log.d(TAG_HW, "onCreate input = $searchInputValue")
        if (savedInstanceState === null) {
            Log.d(TAG_HW, "instanceState = empty")
        }*/

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
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(resetButton.windowToken, 0)

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
    }

    /*override fun onStart() {
        super.onStart()
        Log.d(TAG_HW, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG_HW, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG_HW, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG_HW, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_HW, "onDestroy")
    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, searchInputValue)
//        Log.d(TAG_HW, "onSaveInstanceState input = $searchInputValue")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputValue = savedInstanceState.getString(INPUT_VALUE, DEFAULT_INPUT_VALUE)
//        Log.d(TAG_HW, "onRestoreInstanceState input = $searchInputValue")
    }
    companion object {
        const val INPUT_VALUE = "INPUT_VALUE"
        const val DEFAULT_INPUT_VALUE = ""
        const val TAG_HW = "HW_9"
    }
}