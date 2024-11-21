package com.sample.application.ea.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import com.sample.application.ea.R
import com.sample.application.ea.res.TypedArrayContainer
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

/**
 * Custom ImageView that supports circular, rounded, and oval shapes with customizable borders.
 */
class RoundImageView : AppCompatImageView {

    // Type of the shape (circle, round, or oval)
    private var type by Delegates.notNull<Int>()

    companion object {
        const val TYPE_CIRCLE: Int = 0
        const val TYPE_ROUND: Int = 1
        const val TYPE_OVAL: Int = 2
    }

    // Border color and width
    private var mBorderColor by Delegates.notNull<Int>()
    private var mBorderWidth by Delegates.notNull<Float>()

    // Corner radius for rounded shapes
    private var mCornerRadius by Delegates.notNull<Float>()
    private var mLeftTopCornerRadius by Delegates.notNull<Float>()
    private var mRightTopCornerRadius by Delegates.notNull<Float>()
    private var mLeftBottomCornerRadius by Delegates.notNull<Float>()
    private var mRightBottomCornerRadius by Delegates.notNull<Float>()

    // Paint objects for bitmap and border
    private var mBitmapPaint by Delegates.notNull<Paint>()
    private var mBorderPaint by Delegates.notNull<Paint>()

    // Radius for circular shapes
    private var mRadius by Delegates.notNull<Float>()

    // Matrix for scaling and translating
    private var mMatrix by Delegates.notNull<Matrix>()

    // Shader for the bitmap
    private var mBitmapShader by Delegates.notNull<BitmapShader>()

    // Width of the view
    private var mWidth by Delegates.notNull<Int>()

    // Rect and Path for drawing rounded shapes
    private var mRoundRect by Delegates.notNull<RectF>()

    // Flag to use density-independent pixels
    private var mRoundPath by Delegates.notNull<Path>()

    private var useUnitDip: Boolean = true

    constructor(
        context: Context
    ) : this(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)

    /**
     * Constructor for XML inflation with a default style.
     *
     * @param context The context in which the view is running.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
     */
    @SuppressLint("CustomViewStyleable")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        TypedArrayContainer(
            context,
            attrs,
            R.styleable.RoundImageView,
            defStyleAttr,
            0
        ).use { a ->
            a.apply {
                type = getInt(
                    R.styleable.RoundImageView_type,
                    TYPE_OVAL
                )
                mBorderColor = getColor(
                    R.styleable.RoundImageView_border_color,
                    Color.WHITE
                )
                mBorderWidth = getDimension(
                    R.styleable.RoundImageView_border_width,
                    0f
                )
                mCornerRadius = getDimension(
                    R.styleable.RoundImageView_corner_radius,
                    dp2px(10).toFloat()
                )
                mLeftTopCornerRadius = getDimension(
                    R.styleable.RoundImageView_leftTop_corner_radius,
                    0f
                )
                mLeftBottomCornerRadius = getDimension(
                    R.styleable.RoundImageView_leftBottom_corner_radius,
                    0f
                )
                mRightTopCornerRadius = getDimension(
                    R.styleable.RoundImageView_rightTop_corner_radius,
                    0f
                )
                mRightBottomCornerRadius = getDimension(
                    R.styleable.RoundImageView_rightBottom_corner_radius,
                    0f
                )
            }
        }

        init()
    }

    /**
     * Initializes the paint objects and other necessary fields.
     */
    private fun init() {
        mRoundPath = Path()
        mRoundRect = RectF()
        mMatrix = Matrix()
        mBitmapPaint = Paint()
        mBitmapPaint.isAntiAlias = true
        mBorderPaint = Paint()
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE

        updateStrokePaint()
    }

    /**
     * Updates the border paint with the current border color and width.
     */
    private fun updateStrokePaint() {
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (type == TYPE_CIRCLE) {
            mWidth = min(
                MeasureSpec.getSize(widthMeasureSpec).toDouble(),
                MeasureSpec.getSize(heightMeasureSpec).toDouble()
            ).toInt()
            mRadius = mWidth / 2 - mBorderWidth / 2
            setMeasuredDimension(mWidth, mWidth)
        }
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (type == TYPE_ROUND || type == TYPE_OVAL) {
            mRoundRect[
                mBorderWidth / 2,
                mBorderWidth / 2,
                w - mBorderWidth / 2] =
                h - mBorderWidth / 2
        }
    }

    override fun onDraw(
        canvas: Canvas
    ) {
        if (drawable == null) {
            return
        }
        setupShader()

        if (type == TYPE_ROUND) {
            setRoundPath()

            canvas.drawPath(mRoundPath, mBitmapPaint)

            if (mBorderWidth > 0) {
                canvas.drawPath(mRoundPath, mBorderPaint)
            }
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(
                mRadius + mBorderWidth / 2,
                mRadius + mBorderWidth / 2,
                mRadius,
                mBitmapPaint
            )

            if (mBorderWidth > 0) {
                canvas.drawCircle(
                    mRadius + mBorderWidth / 2,
                    mRadius + mBorderWidth / 2,
                    mRadius,
                    mBorderPaint
                )
            }
        } else {
            canvas.drawOval(mRoundRect, mBitmapPaint)

            if (mBorderWidth > 0) {
                canvas.drawOval(mRoundRect, mBorderPaint)
            }
        }
    }

    /**
     * Sets the path for rounded corners.
     */
    private fun setRoundPath() {
        mRoundPath.reset()
        if (mLeftTopCornerRadius == 0f &&
            mLeftBottomCornerRadius == 0f &&
            mRightTopCornerRadius == 0f &&
            mRightBottomCornerRadius == 0f
        ) {
            mRoundPath.addRoundRect(
                mRoundRect,
                floatArrayOf(
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius
                ),
                Path.Direction.CW
            )
        } else {
            mRoundPath.addRoundRect(
                mRoundRect,
                floatArrayOf(
                    mLeftTopCornerRadius, mLeftTopCornerRadius,
                    mRightTopCornerRadius, mRightTopCornerRadius,
                    mRightBottomCornerRadius, mRightBottomCornerRadius,
                    mLeftBottomCornerRadius, mLeftBottomCornerRadius
                ),
                Path.Direction.CW
            )
        }
    }

    /**
     * Sets up the bitmap shader for drawing.
     */
    private fun setupShader() {
        val drawable = drawable ?: return
        val bmp: Bitmap = drawableToBitmap(drawable) ?: return

        mBitmapShader = BitmapShader(
            bmp,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mMatrix.setTranslate(0f, 0f)
        var scale = 1.0f
        if (type == TYPE_CIRCLE) {
            if (bmp.width != width ||
                bmp.height != height
            ) {

                val bSize =
                    min(
                        bmp.width.toDouble(),
                        bmp.height.toDouble()
                    ).toInt()
                scale = mWidth * 1.0f / bSize
                //使缩放后的图片居中
                val dx = (bmp.width * scale - mWidth) / 2
                val dy = (bmp.height * scale - mWidth) / 2
                mMatrix.setTranslate(-dx, -dy)
            }
        } else if (type == TYPE_ROUND ||
            type == TYPE_OVAL
        ) {
            if (bmp.width != width ||
                bmp.height != height
            ) {

                scale = max(
                    (width * 1.0f / bmp.width).toDouble(),
                    (height * 1.0f / bmp.height).toDouble()
                ).toFloat()

                val dx = (scale * bmp.width - width) / 2
                val dy = (scale * bmp.height - height) / 2
                mMatrix.setTranslate(-dx, -dy)
            }
        }

        mMatrix.preScale(scale, scale)

        mBitmapShader.setLocalMatrix(mMatrix)

        mBitmapPaint.setShader(mBitmapShader)
    }

    /**
     * Converts a Drawable to a Bitmap.
     *
     * @param drawable The drawable to convert.
     * @return The resulting bitmap.
     */
    private fun drawableToBitmap(
        drawable: Drawable
    ): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        var bitmap: Bitmap? = null
        try {
            bitmap = Bitmap.createBitmap(
                w,
                h,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(
                0,
                0,
                w,
                h
            )
            drawable.draw(canvas)
        } catch (ignore: OutOfMemoryError) {
        }
        return bitmap
    }

    /**
     * Sets the type of shape.
     *
     * @param imageType The type of the shape.
     * @return The current instance for chaining.
     */
    fun setType(
        imageType: Int
    ): RoundImageView {
        if (this.type != imageType) {
            this.type = imageType
            if (this.type != TYPE_ROUND &&
                this.type != TYPE_CIRCLE &&
                this.type != TYPE_OVAL
            ) {
                this.type = TYPE_OVAL
            }
            requestLayout()
        }
        return this
    }

    /**
     * Sets the corner radius for all corners.
     *
     * @param cornerRadius The radius in dp.
     * @return The current instance for chaining.
     */
    fun setCornerRadius(
        cornerRadius: Int
    ): RoundImageView {
        val radius = dp2px(cornerRadius).toFloat()
        if (mCornerRadius != radius) {
            mCornerRadius = radius
            invalidate()
        }
        return this
    }

    /**
     * Sets the top-left corner radius.
     *
     * @param cornerRadius The radius in dp.
     * @return The current instance for chaining.
     */
    fun setLeftTopCornerRadius(
        cornerRadius: Int
    ): RoundImageView {
        val radius = dp2px(cornerRadius).toFloat()
        if (mLeftTopCornerRadius != radius) {
            mLeftTopCornerRadius = radius
            invalidate()
        }
        return this
    }

    /**
     * Sets the top-right corner radius.
     *
     * @param cornerRadius The radius in dp.
     * @return The current instance for chaining.
     */
    fun setRightTopCornerRadius(
        cornerRadius: Int
    ): RoundImageView {
        val radius = dp2px(cornerRadius).toFloat()
        if (mRightTopCornerRadius != radius) {
            mRightTopCornerRadius = radius
            invalidate()
        }
        return this
    }

    /**
     * Sets the bottom-left corner radius.
     *
     * @param cornerRadius The radius in dp.
     * @return The current instance for chaining.
     */
    fun setLeftBottomCornerRadius(
        cornerRadius: Int
    ): RoundImageView {
        val radius = dp2px(cornerRadius).toFloat()
        if (mLeftBottomCornerRadius != radius) {
            mLeftBottomCornerRadius = radius
            invalidate()
        }
        return this
    }

    /**
     * Sets the bottom-right corner radius.
     *
     * @param cornerRadius The radius in dp.
     * @return The current instance for chaining.
     */
    fun setRightBottomCornerRadius(
        cornerRadius: Int
    ): RoundImageView {
        val radius = dp2px(cornerRadius).toFloat()
        if (mRightBottomCornerRadius != radius) {
            mRightBottomCornerRadius = radius
            invalidate()
        }

        return this
    }

    /**
     * Sets the border width.
     *
     * @param borderWidth The width in dp.
     * @return The current instance for chaining.
     */
    fun setBorderWidth(
        borderWidth: Int
    ): RoundImageView {
        val width = dp2px(borderWidth).toFloat()
        if (mBorderWidth != width) {
            mBorderWidth = width
            updateStrokePaint()
            invalidate()
        }

        return this
    }

    /**
     * Sets the border color.
     *
     * @param borderColor The color as an integer.
     * @return The current instance for chaining.
     */
    fun setBorderColor(
        borderColor: Int
    ): RoundImageView {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor
            updateStrokePaint()
            invalidate()
        }

        return this
    }

    /**
     * Converts dp to pixels.
     *
     * @param dpVal The value in dp.
     * @return The value in pixels.
     */
    private fun dp2px(
        dpVal: Int
    ): Int {
        if (!useUnitDip) {
            return dpVal
        }
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    /**
     * Checks if the unit is set to dip.
     *
     * @return True if using dip, false otherwise.
     */
    fun isUseUnitDip() = useUnitDip

    /**
     * Sets whether to use dip for dimensions.
     *
     * @param useUnitDip True to use dip, false otherwise.
     */
    fun setUseUnitDip(
        useUnitDip: Boolean
    ) {
        this.useUnitDip = useUnitDip
    }
}

