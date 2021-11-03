package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetTunaSwipeBinding
import com.tunasoftware.tunaui.extensions.dp
import kotlin.math.abs

class TunaSwipeWidget : FrameLayout {

    private val binding : WidgetTunaSwipeBinding

    private var _onItemClickListener = {}
    private var _onDeleteClickListener = {}

    var swipeDisabled: Boolean = false

    fun setOnItemClickListener(listener: () -> Unit){
        _onItemClickListener = listener
    }

    fun setOnDeleteClickListener(listener: () -> Unit){
        _onDeleteClickListener = listener
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        binding = WidgetTunaSwipeBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(
            attrs,
            R.styleable.SwipeWidget,
            0,
            0
        ).apply {
            try {
                swipeDisabled = getBoolean(R.styleable.SwipeWidget_swipeDisabled, false)
            } finally {
                recycle()
            }
        }

        with(binding){
            btnDelete.setOnClickListener {
                _onDeleteClickListener.invoke()
            }

            gestureHandler.setOnTouchListener(object :OnTouchListener{

                var startX:Float? = null
                var hasMoven = false

                override fun onTouch(p0: View?, event: MotionEvent): Boolean {
                    when(event.action){

                        MotionEvent.ACTION_DOWN -> {
                            hasMoven = false
                            startX = event.x
                        }
                        MotionEvent.ACTION_MOVE -> {

                            if (swipeDisabled) return false

                            if (event.x < startX!!){
                                val diff = startX!! - event.x
                                Log.d("swipe", "diff $diff")
                                Log.d("swipe", "diff dp ${diff/ 1.dp}")
                                if (abs(diff) > 20.dp) {
                                    hasMoven = true
                                    val progressInc = diff / swipeLayout.width
                                    Log.d("swipe", "progressInc $progressInc")
                                    if (swipeLayout.progress + progressInc <= 1f)
                                        swipeLayout.progress += progressInc
                                    else
                                        swipeLayout.progress = 1f
                                }
                            } else {
                                val diff = event.x - startX!!
                                Log.d("swipe", "diff $diff")
                                Log.d("swipe", "diff dp ${diff/ 1.dp}")
                                if (abs(diff) > 20.dp) {
                                    hasMoven = true
                                    val progressInc = diff / swipeLayout.width
                                    Log.d("swipe", "progressInc $progressInc")
                                    if (swipeLayout.progress - progressInc >= 0f)
                                        swipeLayout.progress -= progressInc
                                    else
                                        swipeLayout.progress = 0f
                                }
                            }

                        }
                        MotionEvent.ACTION_UP -> {
                            if (!hasMoven || swipeDisabled){
                                swipeLayout.progress = 0f
                                _onItemClickListener.invoke()
                            } else {
                                if (event.x < startX?:0f){
                                    swipeLayout.transitionToEnd()
                                } else {
                                    swipeLayout.transitionToStart()
                                }
                            }
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            if (hasMoven && !swipeDisabled) {
                                if (event.x < startX ?: 0f) {
                                    swipeLayout.transitionToEnd()
                                } else {
                                    swipeLayout.transitionToStart()
                                }
                            }
                        }
                    }
                    return true
                }

            })
        }


    }

    fun close(){
        binding.swipeLayout.progress = 0f
    }

}