package ir.smilegame.findthespy

import kotlinx.android.synthetic.main.my_custom_number_picker.view.*

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

class MyCustomNumberPicker(context: Context, attrs: AttributeSet) : MyNumberPicker(context, attrs){

    var myIcon:Drawable? = null
        set(value) {
            if (value != null){
                imageView.setImageDrawable(value)
                imageView.visibility = VISIBLE
                field = value
            }
    }
    var text:String? = ""
    set(value) {
        textView.text = value
        textView.visibility = VISIBLE
        field = value
    }
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyCustomNumberPicker,
            0, 0
        ).apply {

            try {
                myIcon = getDrawable(R.styleable.MyCustomNumberPicker_myIcon)
                if (myIcon != null){
                    imageView.setImageDrawable(myIcon)
                    imageView.visibility = VISIBLE
                }else{
                    imageView.visibility = GONE
                }
                text = getString(R.styleable.MyCustomNumberPicker_text)
                if(!text.equals("")){
                    textView.text = text
                    textView.visibility = VISIBLE
                }else{
                    textView.visibility = GONE
                }
            } finally {
                recycle()
            }
        }
    }

    override fun initView(attrs: AttributeSet) {
        View.inflate(context, R.layout.my_custom_number_picker, this)
    }

}

