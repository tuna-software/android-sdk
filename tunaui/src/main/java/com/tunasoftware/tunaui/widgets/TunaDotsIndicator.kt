package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.TunaWidgetDotBinding
import com.tunasoftware.tunaui.databinding.TunaWidgetDotIndicatorBinding

class TunaDotsIndicator : LinearLayout {

    private val binding: TunaWidgetDotIndicatorBinding

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
        binding.dotsContainer.removeAllViews()
        dots.forEachIndexed { index, tunaDotView ->
            binding.dotsContainer.addView(tunaDotView)
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
        binding = TunaWidgetDotIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
    }

}

class TunaDotView : FrameLayout {

    private val binding : TunaWidgetDotBinding

    var active = false
    set(value) {
        field = value
        binding.tunaDotViewSelected.animate().alpha(if (value) 1f else 0f).setDuration(200).start()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        inflate(context, R.layout.tuna_widget_dot, this)
        binding = TunaWidgetDotBinding.bind(this)
        binding.tunaDotViewSelected.alpha = 0f
    }
}