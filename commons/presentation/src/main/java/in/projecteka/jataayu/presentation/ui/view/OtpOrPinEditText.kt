package `in`.projecteka.jataayu.presentation.ui.view

import `in`.projecteka.jataayu.presentation.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.*

class OtpOrPinEditText : AppCompatEditText {
    private var mSpace = 24f //24 dp by default, space between the lines
    private var mNumChars = 4f
    private var mLineSpacing = 8f //8dp by default, height of the text from our lines
    private var mLineStroke = 2f
    private var mLinesPaint: Paint? = null
    private var mClickListener: OnClickListener? = null
    var mShouldMask: Boolean = false
    private lateinit var mMaskCharacter: String
    private var mMaskDefaultCharacter: String = "*"

    constructor(context: Context?) : super(context!!)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OtpOrPinEditText,
            0, 0).apply {

            try {
                mShouldMask = getBoolean(R.styleable.OtpOrPinEditText_shouldMask, false)
                mMaskCharacter = getString(R.styleable.OtpOrPinEditText_maskCharacter) ?: mMaskDefaultCharacter
            } finally {
                recycle()
            }
        }


        val multi = context.resources.displayMetrics.density
        mLineStroke *= multi
        mLinesPaint = Paint(paint)
        mLinesPaint!!.strokeWidth = mLineStroke
        mLinesPaint!!.color = resources.getColor(R.color.colorPrimaryDark)
        setBackgroundResource(0)
        mSpace *= multi //convert to pixels for our density
        mLineSpacing *= multi //convert to pixels for our density
        mNumChars = minEms.toFloat()
        super.setOnClickListener { v ->
            // When tapped, move cursor to end of text.
            setSelection(Objects.requireNonNull(text!!).length)
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        val mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }
        var startX = paddingLeft
        val bottomValue = height - paddingBottom
        val txt = text
        val textLength = txt!!.length
        val textWidths = FloatArray(textLength)

        paint.getTextWidths(text, 0, textLength, textWidths)
        var i = 0
        while (i < mNumChars) {
            canvas.drawLine(startX.toFloat(), bottomValue.toFloat(), startX + mCharSize, bottomValue.toFloat(), mLinesPaint!!)
            if (text!!.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(getTextToShow(text!!.toString()), i, i + 1, middle - textWidths[0] / 2, bottomValue - mLineSpacing, paint)
            }
            startX += if (mSpace < 0) { (mCharSize * 2).toInt() }
            else { (mCharSize + mSpace.toInt()).toInt() }
            i++
        }
    }

    private fun getTextToShow(txt: String): String {
        return if (mShouldMask){
            getPattern(text = txt)
        } else {
            txt
        }
    }

    fun getPattern(text: String): String {
        var pattern: String = ""
        var i: Int = 0
        if (text.isNotEmpty()){
            while (i < text.length) {
                pattern += mMaskCharacter
                i++
            }
        }
        return pattern
    }

}