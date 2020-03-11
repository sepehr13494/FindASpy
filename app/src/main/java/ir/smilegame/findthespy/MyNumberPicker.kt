package ir.smilegame.findthespy

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.my_number_picker.view.*


open class MyNumberPicker(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs){

    var number:Int = 0
    var mDefNum:Int = 0
    set(value) {
        number = value
        field = value
        listener.onNumberChanged(value)
    }
    var count:Int = 1
    var textColor = Color.BLACK
    var min_number = 0
    var max_number = 100

    private var listener : OnNumberChangedListener = object : OnNumberChangedListener{
        override fun onNumberChanged(sum: Int) {
        }
    }

    init {

        initView(attrs)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyNumberPicker,
            0, 0
        ).apply {

            try {
                mDefNum = getInt(R.styleable.MyNumberPicker_default_number, 1)
                count = getInt(R.styleable.MyNumberPicker_increase_count, 1)
                textColor = getColor(R.styleable.MyNumberPicker_text_color, Color.BLACK)
                min_number = getInt(R.styleable.MyNumberPicker_min_number,1)
                max_number = getInt(R.styleable.MyNumberPicker_max_number,100)
                number = mDefNum
                edittext.setTextColor(textColor)
                edittext.text = mDefNum.toString()
                listener.onNumberChanged(number)
            } finally {
                recycle()
            }
        }

        plus_btn.setOnClickListener {
            var sum = edittext.text.toString().toInt() + count
            if (sum>=max_number){
                sum = max_number
            }
            edittext.text = sum.toString()
            number = sum
            listener.onNumberChanged(sum)
        }

        mines_btn.setOnClickListener {
            var sum = edittext.text.toString().toInt() - count
            if (sum<=min_number){
                sum = min_number
            }
            edittext.text = sum.toString()
            number = sum
            listener.onNumberChanged(sum)
        }

    }

    open fun initView(attrs: AttributeSet) {
        View.inflate(context, R.layout.my_number_picker, this)
    }

    fun setOnNumberChangedListener(listener: OnNumberChangedListener) {
        this.listener = listener
    }

    interface OnNumberChangedListener{
        fun onNumberChanged(sum:Int)
    }
}