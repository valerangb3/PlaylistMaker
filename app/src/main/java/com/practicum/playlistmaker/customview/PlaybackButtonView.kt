package com.practicum.playlistmaker.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    enum class STATE {
        PLAY, PAUSE;
    }

    var currentState = STATE.PAUSE
        set(value) {
            field = value
            invalidate()
        }

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?

    private val playbackImageRect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val tint = getColor(R.styleable.PlaybackButtonView_tint, 0)
                playImageBitmap = getDrawable(R.styleable.PlaybackButtonView_playIco)?.apply { setTint(tint) }?.toBitmap()
                pauseImageBitmap = getDrawable(R.styleable.PlaybackButtonView_pauseIco)?.apply { setTint(tint) }?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        playbackImageRect.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (currentState) {
            STATE.PLAY -> {
                pauseImageBitmap?.let {
                    canvas.drawBitmap(it, null, playbackImageRect, null)
                }
            }
            STATE.PAUSE -> {
                playImageBitmap?.let {
                    canvas.drawBitmap(it, null, playbackImageRect, null)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

}