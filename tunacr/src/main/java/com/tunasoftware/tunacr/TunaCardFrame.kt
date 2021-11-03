package com.tunasoftware.tunacr

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import extensions.dp

class TunaCardFrame : View {

    private var squarePath: Path? = null
    private val squerePaint = Paint()

    private var pointsOld = listOf<Point>()
    private var pointsNew = listOf<Point>()
    private var scaleUp = 15.dp
    val bounceAnimation = ValueAnimator.ofFloat(15f, 21f)
    var cornerColor = Color.WHITE
    private var isBouncing = true
    var isAnimating = false
    var frameColor = Color.WHITE


    init {
        squerePaint.style = Paint.Style.STROKE
        squerePaint.color = Color.WHITE
        squerePaint.flags = Paint.ANTI_ALIAS_FLAG
        squerePaint.strokeWidth = 0.5.dp.toFloat()
        bounceAnimation.repeatMode = ValueAnimator.REVERSE
        bounceAnimation.repeatCount = ValueAnimator.INFINITE
        bounceAnimation.duration = 500
        bounceAnimation.addUpdateListener {
            if (isBouncing) {
                scaleUp = (it.animatedValue as Float).dp
                pointsNew?.let { createPath(it) }
                invalidate()
            } else {
                scaleUp = 15f.dp
            }
        }
    }

    var startCorner: Corner? = null

    fun startBouncing() {
        isBouncing = true
        if (!bounceAnimation.isRunning)
            bounceAnimation.start()
    }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context?.obtainStyledAttributes(attrs, R.styleable.CameraFrame)?.apply {
            try {
                frameColor = getColor(
                    R.styleable.CameraFrame_frameColor,
                    ContextCompat.getColor(context, android.R.color.white)
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            squarePath?.let {
                canvas.drawPath(it, squerePaint)
            }
            startCorner?.let { start ->
                start.drawCorner(this)
                start.next?.let { second ->
                    second.drawCorner(this)
                    second.next?.let { third ->
                        third.drawCorner(this)
                        third.next?.let { fourth ->
                            fourth.drawCorner(this)
                        }
                    }
                }
            }
        }
    }

    fun setSquarePointsInternal(points: List<Point>, factorX: Float, factorY: Float) {
        if (pointsOld.isEmpty()) {
            pointsOld = points
            pointsNew = points.map { point ->
                Point(
                    (point.x * factorX).toInt(),
                    (point.y * factorY).toInt()
                )
            }
            createPath(points)
        } else {
            pointsNew = points.map { point ->
                Point(
                    (point.x * factorX).toInt(),
                    (point.y * factorY).toInt()
                )
            }
            animatePoints()
        }
    }

    fun animatePoints() {
        if (isAnimating)
            return
        isAnimating = true
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.addUpdateListener {
            val progress = it.animatedValue as Float
            val old = pointsOld.toList()
            old.let { oldPoints ->
                var topPointsOld =
                    oldPoints.sortedBy { p -> p.y }.subList(0, 2).sortedBy { p -> p.x }
                var bottomPointsOld =
                    oldPoints.sortedByDescending { p -> p.y }.subList(0, 2).sortedBy { p -> p.x }
                pointsNew.let { newPoints ->
                    var topPoints =
                        newPoints.sortedBy { p -> p.y }.subList(0, 2).sortedBy { p -> p.x }
                    var bottomPoints = newPoints.sortedByDescending { p -> p.y }.subList(0, 2)
                        .sortedBy { p -> p.x }
                    val points = listOf(
                        calcDiff(topPointsOld[0], topPoints[0], progress),
                        calcDiff(topPointsOld[1], topPoints[1], progress),
                        calcDiff(bottomPointsOld[0], bottomPoints[0], progress),
                        calcDiff(bottomPointsOld[1], bottomPoints[1], progress)
                    )
                    createPath(points)
                }
            }
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                pointsOld = pointsNew
                isAnimating = false
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = 500
        anim.start()
    }

    fun calcDiff(point1: Point, point2: Point, progress: Float): Point {
        val diffX = point2.x - point1.x
        val diffY = point2.y - point1.y
        return Point(point1.x + (diffX * progress).toInt(), point1.y + (diffY * progress).toInt())
    }

    fun addPX(point1: Point, point2: Point, px: Int): Point {
        val diffX = point2.x - point1.x
        val diffY = point2.y - point1.y
        val addX = if (diffX == 0) 0 else (if (diffX>0) px else -px)
        val addY = if (diffY == 0) 0 else (if (diffY>0) px else -px)
        return Point(point1.x + addX, point1.y + addY)
    }


    private fun createPath(points: List<Point>) {
        squarePath = Path()
        squarePath?.apply {
            var topPoints = points.sortedBy { p -> p.y }.subList(0, 2).sortedBy { p -> p.x }
            var bottomPoints =
                points.sortedByDescending { p -> p.y }.subList(0, 2).sortedBy { p -> p.x }

            startCorner = Corner(Point(topPoints[0].x - scaleUp, topPoints[0].y - scaleUp))
            val corner2 = Corner(Point(topPoints[1].x + scaleUp, topPoints[1].y - scaleUp))
            val corner3 = Corner(Point(bottomPoints[1].x + scaleUp, bottomPoints[1].y + scaleUp))
            val corner4 = Corner(Point(bottomPoints[0].x - scaleUp, bottomPoints[0].y + scaleUp))
            startCorner?.apply {
                next = corner2
                previous = corner4
                calcCornerPath()
            }
            corner2.apply {
                next = corner3
                previous = startCorner
                calcCornerPath()
            }
            corner3.apply {
                next = corner4
                previous = corner2
                calcCornerPath()
            }
            corner4.apply {
                next = startCorner
                previous = corner3
                calcCornerPath()
            }

        }
        invalidate()
    }

    fun setNoSquarePoints() {
        cornerColor = Color.WHITE

        val width = this.width.coerceAtMost(500.dp)
        val center = this.width / 2
        val margin = if (width == this.width) 40.dp else 0
        val halfWidth = (width - (margin*2)) / 2
        val squareHeight = ((width - (2*margin))*0.6).toInt()
        val halfSquare = squareHeight / 2

        val points = listOf(
            Point(center - halfWidth, (height / 2) - halfSquare),
            Point(center + halfWidth, (height / 2) - halfSquare),
            Point(center - halfWidth, (height / 2) + halfSquare),
            Point(center + halfWidth, (height / 2) + halfSquare)
        )
        setSquarePointsInternal(points, 1f, 1f)
        startBouncing()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (squarePath == null)
            setNoSquarePoints()
    }


    inner class Corner(val point: Point) {
        var next: Corner? = null
        var previous: Corner? = null

        var cornerPath = Path()
        val paint: Paint = Paint()

        init {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f.dp.toFloat()
            paint.flags = Paint.ANTI_ALIAS_FLAG
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.SQUARE
        }

        fun calcCornerPath() {
            paint.color = cornerColor
            next?.let { next ->
                previous?.let { previous ->
                    val nextPoint = addPX(point, next.point, 25.dp)
                    val nextPointEnd = addPX(point, next.point, 25.dp)
                    val previousPoint = addPX(point, previous.point, 25.dp)
                    val previousPointEnd = addPX(point, previous.point, 25.dp)
                    cornerPath.apply {
                        moveTo(previousPointEnd.x.toFloat(), previousPointEnd.y.toFloat())
                        lineTo(previousPoint.x.toFloat(), previousPoint.y.toFloat())
                        quadTo(
                            point.x.toFloat(),
                            point.y.toFloat(),
                            nextPoint.x.toFloat(),
                            nextPoint.y.toFloat()
                        )
                        lineTo(nextPointEnd.x.toFloat(), nextPointEnd.y.toFloat())

                        //Draw the square
                        squarePath?.apply {
                            val nextPointStart = calcDiff(point, next.point, 0.95f)
                            moveTo(nextPoint.x.toFloat(), nextPoint.y.toFloat())
                            lineTo(nextPointStart.x.toFloat(), nextPointStart.y.toFloat())
                        }
                    }
                }
            }
        }

        fun drawCorner(canvas: Canvas) {
            canvas.drawPath(cornerPath, paint)
        }
    }
}




