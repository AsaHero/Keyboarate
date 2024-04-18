package com.example.remotekeyboardapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

class CustomKeyboardView : View {
    interface KeyPressListener {
        fun onKeyPress(key: String)
    }

    var keyPressListener: KeyPressListener? = null
    private var lastKey: String? = null
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
        style = Paint.Style.FILL
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val unicodeChar = event.getUnicodeChar(event.metaState)

        when (keyCode) {
            KeyEvent.KEYCODE_SPACE -> handleKeyInput("space")
            KeyEvent.KEYCODE_ENTER -> handleKeyInput("enter")
            KeyEvent.KEYCODE_DEL -> handleKeyInput("backspace")
            else -> {
                when (unicodeChar) {
                    '('.code -> handleKeyInput("(")
                    ')'.code -> handleKeyInput(")")
                    '{'.code -> handleKeyInput("{")
                    '}'.code -> handleKeyInput("}")
                    '<'.code -> handleKeyInput("<")
                    '>'.code -> handleKeyInput(">")
                    '?'.code -> handleKeyInput("?")
                    '!'.code -> handleKeyInput("!")
                    '+'.code -> handleKeyInput("+")
                    '_'.code -> handleKeyInput("_")
                    '-'.code -> handleKeyInput("-")
                    '='.code -> handleKeyInput("=")
                    '|'.code -> handleKeyInput("|")
                    '\\'.code -> handleKeyInput("\\")
                    ':'.code -> handleKeyInput(":")
                    ';'.code -> handleKeyInput(";")
                    '"'.code -> handleKeyInput("\"")
                    '\''.code -> handleKeyInput("'")
                    ','.code -> handleKeyInput(",")
                    '.'.code -> handleKeyInput(".")
                    '/'.code -> handleKeyInput("/")
                    '*'.code -> handleKeyInput("*")
                    '&'.code -> handleKeyInput("&")
                    '^'.code -> handleKeyInput("^")
                    '%'.code -> handleKeyInput("%")
                    '$'.code -> handleKeyInput("$")
                    '#'.code -> handleKeyInput("#")
                    '@'.code -> handleKeyInput("@")
                    '`'.code -> handleKeyInput("`")
                    '~'.code -> handleKeyInput("~")
                    '['.code -> handleKeyInput("[")
                    ']'.code -> handleKeyInput("]")
                    '×'.code -> handleKeyInput("×")
                    '÷'.code -> handleKeyInput("÷")
                    '√'.code -> handleKeyInput("√")
                    '∆'.code -> handleKeyInput("∆")
                    else -> {
                        if (Character.isLetterOrDigit(unicodeChar.toChar())) {
                            handleKeyInput(unicodeChar.toChar().toString())
                        } else {
                            return super.onKeyDown(keyCode, event)
                        }
                    }
                }
            }
        }
        return true
    }

    private fun handleKeyInput(key: String) {
        lastKey = key
        keyPressListener?.onKeyPress(lastKey!!)
        invalidate()
        postDelayed({
            lastKey = null
            invalidate()
        }, 1000)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lastKey?.let {
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRect(rect, backgroundPaint)
            canvas.drawText(it, width / 2f, height / 2f + (paint.descent() + paint.ascent()) / -2, paint)
        }
    }
}