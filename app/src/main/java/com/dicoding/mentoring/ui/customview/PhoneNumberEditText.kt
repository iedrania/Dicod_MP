package com.dicoding.mentoring.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.dicoding.mentoring.R

class PhoneNumberEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Menambahkan text aligmnet pada editText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        // Menginisialisasi gambar clear button
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable


        // Menambahkan aksi kepada clear button
        setOnTouchListener(this)

        // Menambahkan aksi ketika ada perubahan text akan memunculkan clear button
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
                val phoneNumber = s.toString()

                // Check apakah phone number tidak dimulai dengan "+62"
                if (!phoneNumber.startsWith("+62")) {
                    // Berikan error pada EditText
                    error = "Input harus dimulai dengan '+62'"
                } else {
                    // Hapus error jika input valid
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                s.toString()
            }
        })
    }

    // Menampilkan clear button
    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage) //ngeset dimana button x diletakkan
    }

    // Menghilangkan clear button
    private fun hideClearButton() {
        setButtonDrawables()
    }

    //Mengkonfigurasi button
    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) { //pengecekan jenis handphone apakah menggunakan format RTL (Right-to-left) seperti bahasa Arab atau format LTR (Left-to-right) seperti bahasa Indonesia/Inggris.

                //pengecekan apakah area yang ditekan adalah area tempat tombol silang berada
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    //(ketika tombol ditekan) tombol akan tetap tampil, namun
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }
                    //teks yang di dalam EditText akan dihapus (clear) dan tombol silang akan disembunyikan dengan memanggil fungsi hideClearButton.
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}