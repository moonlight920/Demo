package com.yuefeng.lib.bubble

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.yuefeng.lib.R


class BubbleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

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
     * 三角形位置偏移量(默认居中)。三角形在上下，则是x轴偏移；三角形在左右，则是y轴偏移
     */
    var offset: Int = 0

    private var mBorderPaint: Paint

    private var mPath: Path

    private var mRect: RectF

    internal var onBubbleLayoutSizeChangeListener: OnBubbleLayoutSizeChangeListener? = null

    @IntDef(NONE, LEFT, TOP, RIGHT, BOTTOM)
    private annotation class Direction

    /**
     * 表示箭头的朝向，NONE没有箭头
     */
    companion object {
        const val NONE = 0
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
        // 默认阴影尺寸
        val defShadowSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX, 4f,
            resources.displayMetrics
        ).toInt()
        //阴影尺寸
        val shadowSize = ta.getDimensionPixelSize(R.styleable.BubbleLayout_shadow_size, defShadowSize)
        mRadius = ta.getDimensionPixelSize(R.styleable.BubbleLayout_radius, 0)
        //三角形方向
        mDirection = ta.getInt(R.styleable.BubbleLayout_direction, BOTTOM)
        offset = ta.getDimensionPixelOffset(R.styleable.BubbleLayout_offset, 0)
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
        mPath.reset()
        // 画矩形
        mPath.addRoundRect(mRect, mRadius.toFloat(), mRadius.toFloat(), Path.Direction.CCW)
        canvas.drawPath(mPath, mBorderPaint)
        if (mDatumPoint.x > 0 && mDatumPoint.y > 0)
        // 根据基点算出三个顶点，画三角
            when (mDirection) {
                LEFT -> drawLeftTriangle(canvas)
                TOP -> drawTopTriangle(canvas)
                RIGHT -> drawRightTriangle(canvas)
                BOTTOM -> drawBottomTriangle(canvas)
            }
    }

    /**
     * 画左侧三角
     */
    private fun drawLeftTriangle(canvas: Canvas) {
        val triangularLength = paddingLeft
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            moveTo(mDatumPoint.x.toFloat(), (mDatumPoint.y - triangularLength / 2).toFloat())
            lineTo((mDatumPoint.x - triangularLength / 2).toFloat(), mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), (mDatumPoint.y + triangularLength / 2).toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    /**
     * 画上方三角
     */
    private fun drawTopTriangle(canvas: Canvas) {
        val triangularLength = paddingTop
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            moveTo(mDatumPoint.x.toFloat() + triangularLength / 2, mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() - triangularLength / 2)
            lineTo(mDatumPoint.x.toFloat() - triangularLength / 2, mDatumPoint.y.toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    /**
     * 画右侧三角
     */
    private fun drawRightTriangle(canvas: Canvas) {
        val triangularLength = paddingRight
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            moveTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() - triangularLength / 2)
            lineTo(mDatumPoint.x.toFloat() + triangularLength / 2, mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() + triangularLength / 2)
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    /**
     * 画下面三角
     */
    private fun drawBottomTriangle(canvas: Canvas) {
        val triangularLength = paddingBottom
        if (triangularLength == 0) {
            return
        }
        mPath.apply {
            moveTo(mDatumPoint.x.toFloat() + triangularLength / 2, mDatumPoint.y.toFloat())
            lineTo(mDatumPoint.x.toFloat(), mDatumPoint.y.toFloat() + triangularLength / 2)
            lineTo(mDatumPoint.x.toFloat() - triangularLength / 2, mDatumPoint.y.toFloat())
            close()
        }
        canvas.drawPath(mPath, mBorderPaint)
    }

    fun setDirection(@Direction direction: Int) {
        mDirection = direction
    }

    /**
     * 根据padding计算矩形的位置
     * 再根据矩形的宽高和padding，计算出三角形基点的位置
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("onSizeChanged", "new w $w   new h $h")
        mRect.apply {
            left = paddingLeft.toFloat()
            top = paddingTop.toFloat()
            right = (w - paddingRight).toFloat()
            bottom = (h - paddingBottom).toFloat()
        }
        // 默认三角在朝向的正中央
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

        onBubbleLayoutSizeChangeListener?.onSizeChanged(w, h, oldw, oldh)

        if (offset != 0) {
            applyOffset()
        }
    }

    /**
     * 根据三角形的偏移量，改变三角形基点位置
     */
    private fun applyOffset() {
        when (mDirection) {
            LEFT, RIGHT -> mDatumPoint.y += offset
            TOP, BOTTOM -> mDatumPoint.x += offset
        }
    }
}