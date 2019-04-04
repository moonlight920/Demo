package com.okay.demo.bubble

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.okay.demo.R


class BubbleLayout(context: Context, @Nullable attrs: AttributeSet) : FrameLayout(context, attrs) {

    /**
     * 圆角大小
     */
    private var mRadius: Int = 0

    /**
     * 三角形的方向
     */
    @Direction
    private var mDirection: Int = 0

    /**
     * 三角形的底边中心点，以此作为绘画基点
     */
    private var mDatumPoint: Point

    /**
     * 三角形位置偏移量(默认居中)
     */
    private var mOffset: Int = 0

    private var mBorderPaint: Paint

    private var mPath: Path

    private var mRect: RectF

    @IntDef(LEFT, TOP, RIGHT, BOTTOM)
    private annotation class Direction

    companion object {
        const val LEFT = 1
        const val TOP = 2
        const val RIGHT = 3
        const val BOTTOM = 4
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout)
        //背景颜色
        val backGroundColor = ta.getColor(R.styleable.BubbleLayout_background_color, Color.WHITE)
        //阴影颜色
        val shadowColor = ta.getColor(
            R.styleable.BubbleLayout_shadow_color,
            Color.parseColor("#999999")
        )
        val defShadowSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            4f, resources.displayMetrics
        ).toInt()
        //阴影尺寸
        val shadowSize = ta.getDimensionPixelSize(R.styleable.BubbleLayout_shadow_size, defShadowSize)
        mRadius = ta.getDimensionPixelSize(R.styleable.BubbleLayout_radius, 0)
        //三角形方向
        mDirection = ta.getInt(R.styleable.BubbleLayout_direction, BOTTOM)
        mOffset = ta.getDimensionPixelOffset(R.styleable.BubbleLayout_offset, 0)
        ta.recycle()

        mBorderPaint = Paint().apply {
            isAntiAlias = true
            color = backGroundColor
            setShadowLayer(shadowSize.toFloat(), 0f, 0f, shadowColor)
        }

        mPath = Path()
        mRect = RectF()
        mDatumPoint = Point()

        setWillNotDraw(false)
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDatumPoint.x > 0 && mDatumPoint.y > 0)
            when (mDirection) {
                LEFT -> drawLeftTriangle(canvas)
                TOP -> drawTopTriangle(canvas)
                RIGHT -> drawRightTriangle(canvas)
                BOTTOM -> drawBottomTriangle(canvas)
            }
    }

    private fun drawLeftTriangle(canvas: Canvas) {
        val triangularLength = paddingLeft
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            addRoundRect(mRect, mRadius.toFloat(), mRadius.toFloat(), Path.Direction.CCW)
            moveTo(mDatumPoint.x.toFloat(), (mDatumPoint.y - triangularLength / 2).toFloat())
            lineTo((mDatumPoint.x - triangularLength / 2).toFloat(), mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), (mDatumPoint.y + triangularLength / 2).toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    private fun drawTopTriangle(canvas: Canvas) {
        val triangularLength = paddingTop
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            addRoundRect(mRect, mRadius.toFloat(), mRadius.toFloat(), Path.Direction.CCW)
            moveTo(mDatumPoint.x.toFloat() + triangularLength / 2, mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() - triangularLength / 2)
            lineTo(mDatumPoint.x.toFloat() - triangularLength / 2, mDatumPoint.y.toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    private fun drawRightTriangle(canvas: Canvas) {
        val triangularLength = paddingRight
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            addRoundRect(mRect, mRadius.toFloat(), mRadius.toFloat(), Path.Direction.CCW)
            moveTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() - triangularLength / 2)
            lineTo(mDatumPoint.x.toFloat() + triangularLength / 2, mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() + triangularLength / 2)
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    private fun drawBottomTriangle(canvas: Canvas) {
        val triangularLength = paddingBottom
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            addRoundRect(mRect, mRadius.toFloat(), mRadius.toFloat(), Path.Direction.CCW)
            moveTo(mDatumPoint!!.x.toFloat() + triangularLength / 2, mDatumPoint!!.y.toFloat())
            lineTo(mDatumPoint!!.x.toFloat(), mDatumPoint!!.y.toFloat() + triangularLength / 2)
            lineTo(mDatumPoint!!.x.toFloat() - triangularLength / 2, mDatumPoint!!.y.toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRect.apply {
            left = paddingLeft.toFloat()
            top = paddingTop.toFloat()
            right = (w - paddingRight).toFloat()
            bottom = (h - paddingBottom).toFloat()
        }
        when (mDirection) {
            LEFT -> {
                mDatumPoint.x = paddingLeft
                mDatumPoint.y = h / 2
            }
            TOP -> {
                mDatumPoint.x = w / 2
                mDatumPoint.y = paddingTop
            }
            RIGHT -> {
                mDatumPoint.x = w - paddingRight
                mDatumPoint.y = h / 2
            }
            BOTTOM -> {
                mDatumPoint.x = w / 2
                mDatumPoint.y = h - paddingBottom
            }
        }

        if (mOffset != 0) {
            applyOffset()
        }
    }

    /**
     * 设置三角形偏移位置并重绘
     *
     * @param offset 偏移量
     */
    fun setTriangleOffset(offset: Int) {
        this.mOffset = offset
        applyOffset()
        invalidate()
    }

    private fun applyOffset() {
        when (mDirection) {
            LEFT, RIGHT -> mDatumPoint.y += mOffset
            TOP, BOTTOM -> mDatumPoint.x += mOffset
        }
    }
}