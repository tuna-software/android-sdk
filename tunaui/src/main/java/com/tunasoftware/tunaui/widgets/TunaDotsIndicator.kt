package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tunasoftware.tunaui.R
import kotlinx.android.synthetic.main.tuna_widget_dot_indicator.view.*

class TunaDotsIndicator : LinearLayout {

    var steps :Int = 0
    set(value) {
        field = value
        dots.clear()
        for (i in 0..value){
            dots.add(
                TunaDotView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                }
            )
        }
        dotsContainer.removeAllViews()
        dots.forEachIndexed { index, tunaDotView ->
            dotsContainer.addView(tunaDotView)
            tunaDotView.active = current == index
        }
    }

    var current: Int = 0
    set(value) {
        field = value
        dots.forEachIndexed { index, tunaDotView ->  tunaDotView.active = current == index}
    }

    private var dots = mutableListOf<TunaDotView>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        inflate(context, R.layout.tuna_widget_dot_indicator, this)
    }

}

class TunaDotView : FrameLayout {

    var active = false
    set(value) {
        field = value
        animate().alpha(if (value) 1f else 0.5f).setDuration(200).start()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        inflate(context, R.layout.tuna_widget_dot, this)
        alpha = 0.5f
    }
}