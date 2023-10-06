package com.example.sampleproject


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

class LearnerCircularProgressView : View {
    companion object {

        private const val KEY_STATE = "KEY_STATE"

        private const val KEY_TOTAL_VALUE = "KEY_TOTAL_VALUE"
        private const val KEY_TOTAL_COLOR = "KEY_TOTAL_COLOR"
        private const val KEY_TOTAL_WIDTH = "KEY_TOTAL_WIDTH"

        private const val KEY_TOTAL_VALUE_2 = "KEY_TOTAL_TWO_VALUE_2"
        private const val KEY_TOTAL_COLOR_2 = "KEY_TOTAL_TWO_COLOR_2"
        private const val KEY_TOTAL_WIDTH_2 = "KEY_TOTAL_TWO_WIDTH_2"

        private const val KEY_PROGRESS_VALUE = "KEY_PROGRESS_VALUE"
        private const val KEY_PROGRESS_COLOR = "KEY_PROGRESS_COLOR"
        private const val KEY_PROGRESS_WIDTH = "KEY_PROGRESS_WIDTH"
        private const val KEY_PROGRESS_ROUND_CAP = "KEY_PROGRESS_ROUND_CAP"
        private const val KEY_PROGRESS_INTERPOLATOR_RES_ID = "KEY_PROGRESS_INTERPOLATOR_RES_ID"

        private const val KEY_PROGRESS_VALUE_2 = "KEY_PROGRESS_VALUE_2"
        private const val KEY_PROGRESS_COLOR_2 = "KEY_PROGRESS_COLOR_2"
        private const val KEY_PROGRESS_WIDTH_2 = "KEY_PROGRESS_WIDTH_2"
        private const val KEY_PROGRESS_ROUND_CAP_2 = "KEY_PROGRESS_ROUND_CAP_2"

        private const val KEY_IMAGE_RESOURCE = "KEY_IMAGE_RESOURCE"

        private const val KEY_WIDTH_BETWEEN_TWO_PROGRESS = "KEY_WIDTH_BETWEEN_TWO_PROGRESS"

        private const val KEY_FILL_COLOR = "KEY_FILL_COLOR"
        private const val KEY_START_ANGLE = "KEY_START_ANGLE"
        private const val KEY_ANIMATE = "KEY_ANIMATE"
        private const val KEY_ANIMATE_DURATION = "KEY_ANIMATE_DURATION"

    }

    val paintTotal = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintFill = Paint(Paint.ANTI_ALIAS_FLAG)

    val paintTotal2 = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintProgress2 = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintFill2 = Paint(Paint.ANTI_ALIAS_FLAG)


    val circleBounds = RectF()
    var centerX: Float = 0f
    var centerY: Float = 0f

    val circleBounds2 = RectF()
    var centerX2: Float = 0f
    var centerY2: Float = 0f

    var totalValue: Int = 100
    var totalColor: Int = 0
    var totalWidth: Float = 16f

    var totalValue2: Int = 100
    var totalColor2: Int = 0
    var totalWidth2: Float = 16f

    var progressValue: Int = 0
    var progressColor: Int = 0
    var progressWidth: Float = 16f
    var progressRoundCap: Boolean = false

    var progressValue2: Int = 0
    var progressColor2: Int = 0
    var progressWidth2: Float = 16f
    var progressRoundCap2: Boolean = false

    var progressInterpolatorResId = android.R.anim.linear_interpolator
    var progressInterpolator: Interpolator = LinearInterpolator()

    var widthBetweenTwoProgress: Float = 4f

    var imageResource: Int = 0
    var imageBitmap: Bitmap? = null
    var fillColor: Int = 0
    var startAngle: Float = 270f
    val imageBounds = Rect()
    var animate = false
    var animateDuration: Long = 300     // In milliseconds
    var progressAnimator: ValueAnimator? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LearnerCircularProgressView)

            totalValue = typedArray.getInt(R.styleable.LearnerCircularProgressView_totalValue, 100)
            totalColor = typedArray.getColor(R.styleable.LearnerCircularProgressView_totalColor, 0)
            totalWidth = typedArray.getDimensionPixelSize(R.styleable.LearnerCircularProgressView_totalWidth, 16).toFloat()

            totalValue2 = typedArray.getInt(R.styleable.LearnerCircularProgressView_totalValue2, 100)
            totalColor2 = typedArray.getColor(R.styleable.LearnerCircularProgressView_totalColor2, 0)
            progressValue = typedArray.getInt(R.styleable.LearnerCircularProgressView_progressValue, 0)
            progressColor = typedArray.getColor(R.styleable.LearnerCircularProgressView_progressColor, 0)
            progressWidth = typedArray.getDimensionPixelSize(R.styleable.LearnerCircularProgressView_progressWidth, 16).toFloat()
            progressRoundCap = typedArray.getBoolean(R.styleable.LearnerCircularProgressView_progressRoundCap, false)

            progressValue2 = typedArray.getInt(R.styleable.LearnerCircularProgressView_progressValue2, 0)
            progressColor2 = typedArray.getColor(R.styleable.LearnerCircularProgressView_progressColor2, 0)
            progressRoundCap2 = typedArray.getBoolean(R.styleable.LearnerCircularProgressView_progressRoundCap2, false)

            // Set default to linear interpolator
            val interpolatorResId = typedArray.getResourceId(R.styleable.LearnerCircularProgressView_progressInterpolator, android.R.anim.linear_interpolator)
            progressInterpolatorResId = interpolatorResId
            progressInterpolator = AnimationUtils.loadInterpolator(context, interpolatorResId)

            widthBetweenTwoProgress = typedArray.getDimensionPixelSize(R.styleable.LearnerCircularProgressView_widthBetweenTwoProgress, 16).toFloat()

            imageResource = typedArray.getResourceId(R.styleable.LearnerCircularProgressView_imageComponent, 0)
            if (imageResource != 0) {
                imageBitmap = BitmapFactory.decodeResource(context.resources, imageResource)
            }

            fillColor = typedArray.getColor(R.styleable.LearnerCircularProgressView_fillColor, 0)

            startAngle = typedArray.getFloat(R.styleable.LearnerCircularProgressView_startAngle, 270f)
            animate = typedArray.getBoolean(R.styleable.LearnerCircularProgressView_animate, false)
            animateDuration = typedArray.getInt(R.styleable.LearnerCircularProgressView_animateDuration, 300).toLong()

            typedArray.recycle()
        }

        // Set the valid progress value
        progressValue = getValidProgressValue(progressValue)
        progressValue2 = getValidProgressValue2(progressValue2)

        setupPaint()
    }

    private fun setupPaint() {
        paintTotal.style = Paint.Style.STROKE
        paintTotal.color = totalColor
        paintTotal.strokeWidth = totalWidth

        paintTotal2.style = Paint.Style.STROKE
        paintTotal2.color = totalColor2
        paintTotal2.strokeWidth = totalWidth2

        paintProgress.style = Paint.Style.STROKE
        paintProgress.color = progressColor
        paintProgress.strokeWidth = progressWidth
        paintProgress.strokeCap = if (progressRoundCap) Paint.Cap.ROUND else Paint.Cap.BUTT

        paintProgress2.style = Paint.Style.STROKE
        paintProgress2.color = progressColor2
        paintProgress2.strokeWidth = progressWidth2
        paintProgress2.strokeCap = if (progressRoundCap2) Paint.Cap.ROUND else Paint.Cap.BUTT

        paintFill.style = Paint.Style.FILL
        paintFill.color = fillColor

        paintFill2.style = Paint.Style.FILL
        paintFill2.color = fillColor
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        updateBounds()

        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()

        centerX2 = (width / 2).toFloat()
        centerY2 = (height / 2).toFloat()

//        loadSvgImage()

        Log.d("centerX", "$centerX")
        Log.d("centerX2", "$centerX2")
        Log.d("centerY", "$centerY")
        Log.d("centerY2", "$centerY2")

    }



    private fun updateBounds() {
        // Calculate padding for the total circle to make sure it is always within the view's bounds
        val padding = totalWidth / 2

        // Set the size and position of the first circle (total progress circle)
        circleBounds.set(
            padding,
            padding,
            width - padding,
            height - padding
        )

        // Calculate the radius of the first circle
        val radius1 = (width - 2 * padding) / 2

        // Calculate padding for the second circle to ensure it is within the view's bounds
        val padding2 = totalWidth.coerceAtLeast(progressWidth) / 2 + widthBetweenTwoProgress

        // Calculate the radius of the second circle based on the size of the first circle and the specified distance
        val radius2 = radius1 - padding2

        // Calculate the position of the second circle based on the center of the first circle
        val centerX2 = width / 2
        val centerY2 = height / 2

        // Set the size and position of the second circle
        circleBounds2.set(
            centerX2 - radius2,
            centerY2 - radius2,
            centerX2 + radius2,
            centerY2 + radius2
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw fill color circle if color exists
        if (fillColor != 0) {

            // Calculate padding for fill color to make sure it is always within the total progress circle
            val padding = if (totalWidth >= progressWidth) {
                totalWidth
            } else {
                (progressWidth / 2) + (totalWidth / 2)
            }

            val padding2 = if (totalWidth2 >= progressWidth2) {
                totalWidth2
            } else {
                (progressWidth2 / 2) + (totalWidth2 / 2)
            }

            val radius = centerX - padding + 1  // Adding 1 to fill the tiny gap that is left
            val radius2 = centerX2 - padding2 + 1  // Adding 1 to fill the tiny gap that is left

            Log.d("padding", "$padding")
            Log.d("padding2", "$padding2")

            Log.d("radius", "$radius")
            Log.d("radius2", "$radius2")

            canvas.drawCircle(centerX, centerY, radius, paintFill)

            canvas.drawCircle(centerX2, centerY2, radius2, paintFill2)
        }

        // Draw total progress
        canvas.drawOval(circleBounds, paintTotal)

        canvas.drawOval(circleBounds2, paintTotal2)

        // Current progress is calculated in degrees from total and progress values
        if (totalValue != 0 && progressValue != 0 && progressValue <= totalValue) {
            val progressSweepAngle = if (totalValue == progressValue) 360f else ((360f / totalValue) * progressValue)
            canvas.drawArc(circleBounds, startAngle, progressSweepAngle, false, paintProgress)
        }

        if (totalValue2 != 0 && progressValue2 != 0 && progressValue2 <= totalValue2) {
            val progressSweepAngle2 = if (totalValue2 == progressValue2) 360f else ((360f / totalValue2) * progressValue2)
            canvas.drawArc(circleBounds2, startAngle, progressSweepAngle2, false, paintProgress2)
        }

        // Draw the SVG image within the second circle
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap!!, null, imageBounds, null)
        }

    }

    /*private fun loadSvgImage() {
        if (imageResource != 0) {
            try {
                val svgInputStream = resources.openRawResource(imageResource)
                val svg = SVG.getFromInputStream(svgInputStream)
                val picture = svg.renderToPicture()

                // Calculate the position and size for the SVG image within the second circle
                val svgWidth = svg.documentWidth
                val svgHeight = svg.documentHeight
                val svgAspectRatio = svgWidth / svgHeight
                val centerX2 = width / 2
                val centerY2 = height / 2
                val radius2 = (centerX2 - circleBounds2.left) // Radius of the second circle
                val imageWidth = radius2 * 2
                val imageHeight = imageWidth / svgAspectRatio

                // Create a Bitmap with the calculated size
                imageBitmap = Bitmap.createBitmap(imageWidth.toInt(), imageHeight.toInt(), Bitmap.Config.ARGB_8888)

                // Create a Canvas to draw the SVG image onto the Bitmap
                val canvas = Canvas(imageBitmap!!)
                canvas.drawPicture(picture)

                // Calculate the bounds for the image within the second circle
                val left = centerX2 - imageWidth / 2
                val top = centerY2 - imageHeight / 2
                val right = centerX2 + imageWidth / 2
                val bottom = centerY2 + imageHeight / 2

                imageBounds.set(left.roundToInt(), top.roundToInt(), right.roundToInt(), bottom.roundToInt())

                // Close the input stream
                svgInputStream.close()
            } catch (e: Exception) {
                Log.d("svgInput is Crashing", "${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    }*/


    /**
     * Filter out any invalid progress value
     * */

    private fun getValidProgressValue(input: Int): Int {
        return when {
            input < 0 -> 0
            input > totalValue -> totalValue
            else -> input
        }
    }

    private fun getValidProgressValue2(input: Int): Int {
        return when {
            input < 0 -> 0
            input > totalValue2 -> totalValue2
            else -> input
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // To ensure the view is always circular in shape
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()

        // View's internal state
        bundle.putParcelable(KEY_STATE, super.onSaveInstanceState())

        // Keys for total
        bundle.putInt(KEY_TOTAL_VALUE, totalValue)
        bundle.putInt(KEY_TOTAL_COLOR, totalColor)
        bundle.putFloat(KEY_TOTAL_WIDTH, totalWidth)

        bundle.putInt(KEY_TOTAL_VALUE_2, totalValue2)
        bundle.putInt(KEY_TOTAL_COLOR_2, totalColor2)
        bundle.putFloat(KEY_TOTAL_WIDTH_2, totalWidth2)

        // Keys for progress
        bundle.putInt(KEY_PROGRESS_VALUE, progressValue)
        bundle.putInt(KEY_PROGRESS_COLOR, progressColor)
        bundle.putFloat(KEY_PROGRESS_WIDTH, progressWidth)
        bundle.putBoolean(KEY_PROGRESS_ROUND_CAP, progressRoundCap)
        bundle.putInt(KEY_PROGRESS_INTERPOLATOR_RES_ID, progressInterpolatorResId)

        bundle.putInt(KEY_PROGRESS_VALUE_2, progressValue2)
        bundle.putInt(KEY_PROGRESS_COLOR_2, progressColor2)
        bundle.putFloat(KEY_PROGRESS_WIDTH_2, progressWidth2)
        bundle.putBoolean(KEY_PROGRESS_ROUND_CAP_2, progressRoundCap2)

        bundle.putFloat(KEY_WIDTH_BETWEEN_TWO_PROGRESS, widthBetweenTwoProgress)

        bundle.putInt(KEY_IMAGE_RESOURCE, imageResource)

        // Other keys
        bundle.putInt(KEY_FILL_COLOR, fillColor)
        bundle.putFloat(KEY_START_ANGLE, startAngle)
        bundle.putBoolean(KEY_ANIMATE, animate)
        bundle.putLong(KEY_ANIMATE_DURATION, animateDuration)

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            // Restore the keys of the view that we saved in the onSaveInstanceState
            totalValue = state.getInt(KEY_TOTAL_VALUE)
            totalColor = state.getInt(KEY_TOTAL_COLOR)
            totalWidth = state.getFloat(KEY_TOTAL_WIDTH)

            totalValue2 = state.getInt(KEY_TOTAL_VALUE_2)
            totalColor2 = state.getInt(KEY_TOTAL_COLOR_2)
            totalWidth2 = state.getFloat(KEY_TOTAL_WIDTH_2)

            progressValue = state.getInt(KEY_PROGRESS_VALUE)
            progressColor = state.getInt(KEY_PROGRESS_COLOR)
            progressWidth = state.getFloat(KEY_PROGRESS_WIDTH)
            progressRoundCap = state.getBoolean(KEY_PROGRESS_ROUND_CAP)
            progressInterpolatorResId = state.getInt(KEY_PROGRESS_INTERPOLATOR_RES_ID)

            progressValue2 = state.getInt(KEY_PROGRESS_VALUE_2)
            progressColor2 = state.getInt(KEY_PROGRESS_COLOR_2)
            progressWidth2 = state.getFloat(KEY_PROGRESS_WIDTH_2)
            progressRoundCap2 = state.getBoolean(KEY_PROGRESS_ROUND_CAP_2)

            imageResource = state.getInt(KEY_IMAGE_RESOURCE)

            fillColor = state.getInt(KEY_FILL_COLOR)
            startAngle = state.getFloat(KEY_START_ANGLE)
            animate = state.getBoolean(KEY_ANIMATE)
            animateDuration = state.getLong(KEY_ANIMATE_DURATION)

            widthBetweenTwoProgress = state.getFloat(KEY_WIDTH_BETWEEN_TWO_PROGRESS)

            progressInterpolator = AnimationUtils.loadInterpolator(context, progressInterpolatorResId)

            setupPaint()

            // Restore the view's internal state
            super.onRestoreInstanceState(state.getParcelable(KEY_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.cancel()
        imageBitmap?.recycle() // Recycle the imageBitmap
    }
}
