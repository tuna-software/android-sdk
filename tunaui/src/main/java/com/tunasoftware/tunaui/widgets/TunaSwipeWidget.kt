package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.extensions.dp
import kotlinx.android.synthetic.main.widget_tuna_swipe.view.*
import kotlin.math.abs

class TunaSwipeWidget : FrameLayout {

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
        inflate(context, R.layout.widget_tuna_swipe, this)

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
                                val progressInc = diff / swipe_layout.width
                                Log.d("swipe", "progressInc $progressInc")
                                if (swipe_layout.progress + progressInc <= 1f)
                                    swipe_layout.progress += progressInc
                                else
                                    swipe_layout.progress = 1f
                            }
                        } else {
                            val diff = event.x - startX!!
                            Log.d("swipe", "diff $diff")
                            Log.d("swipe", "diff dp ${diff/ 1.dp}")
                            if (abs(diff) > 20.dp) {
                                hasMoven = true
                                val progressInc = diff / swipe_layout.width
                                Log.d("swipe", "progressInc $progressInc")
                                if (swipe_layout.progress - progressInc >= 0f)
                                    swipe_layout.progress -= progressInc
                                else
                                    swipe_layout.progress = 0f
                            }
                        }

                    }
                    MotionEvent.ACTION_UP -> {
                        if (!hasMoven || swipeDisabled){
                            swipe_layout.progress = 0f
                            _onItemClickListener.invoke()
                        } else {
                            if (event.x < startX?:0f){
                                swipe_layout.transitionToEnd()
                            } else {
                                swipe_layout.transitionToStart()
                            }
                        }
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        if (hasMoven && !swipeDisabled) {
                            if (event.x < startX ?: 0f) {
                                swipe_layout.transitionToEnd()
                            } else {
                                swipe_layout.transitionToStart()
                            }
                        }
                    }
                }
                return true
            }

        })
    }

    fun close(){
        swipe_layout.progress = 0f
    }

}