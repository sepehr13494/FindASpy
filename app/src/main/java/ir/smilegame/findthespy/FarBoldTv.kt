package ir.smilegame.findthespy

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

class FarBoldTv @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){
    var autoSizeInLines:Int = 0
    init {
        if(!isInEditMode){
            var tf = Typeface.createFromAsset(getContext().assets,"fonts/Far_Bold.ttf")
            typeface = tf
        }
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FarBoldTv,
            0, 0
        ).apply {

            try {
                autoSizeInLines = getInt(R.styleable.FarBoldTv_autoSizeLines,0)
            } finally {
                recycle()
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (autoSizeInLines>0){
            Handler().postDelayed(Runnable {
                if (lineCount>autoSizeInLines) {//Or any Loops
                    val sp = textSize/resources.displayMetrics.scaledDensity
                    setTextSize(TypedValue.COMPLEX_UNIT_SP,sp-2)

                }
            },100)
        }

    }

}