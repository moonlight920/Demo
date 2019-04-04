package com.yuefeng.lib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.LayoutRes
import android.support.v4.widget.PopupWindowCompat
import android.util.Log
import android.view.*
import android.widget.PopupWindow

abstract class OkBasePopup<T : OkBasePopup<T>> {

    companion object {
        const val TAG = "OkBasePopup"
    }

    /**PopupWindow对象
     *
     */
    private var mPopupWindow: PopupWindow? = null

    /**
     * 点击外部是否消失
     */
    private var mFocusAndOutsideEnable = true

    /**
     * context
     */
    private var mContext: Context? = null
    /**
     * contentView
     */
    private var mContentView: View? = null
    private var mLayoutId: Int = 0

    /**
     * popupWindow宽高
     */
    private var mWidth = 0
    private var mHeight = 0

    /**
     * 弹出动画
     */
    private var mAnimationStyle: Int = android.R.anim.fade_in

    @Suppress("UNCHECKED_CAST")
    private fun self(): T {
        return this as T
    }

    fun build(): T {
        mPopupWindow = mPopupWindow ?: PopupWindow()

        // 初始化popupWindow属性
        initPopupWindowAttr()
        // 初始化布局
        initContentView()
        // 初始化内部控件
        initView(mContentView!!, self())

        if (mAnimationStyle != 0) {
            mPopupWindow!!.animationStyle = mAnimationStyle
        }

        initFocusAndBack()

        return self()
    }

    protected abstract fun initPopupWindowAttr()

    protected abstract fun initView(mContentView: View, popupWindow: T)

    /**
     * 设置布局
     * @param layoutId
     */
    protected fun setContentView(context: Context, @LayoutRes layoutId: Int): T {
        mContext = context
        mLayoutId = layoutId
        mContentView = null
        return self()
    }

    protected fun setContentView(contentView: View): T {
        mContentView = contentView
        mLayoutId = 0
        return self()
    }

    private fun initContentView() {
        if (mContentView == null) {
            if (mContext != null && mLayoutId > 0) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
            } else {
                throw IllegalArgumentException("The content view is null")
            }
        }
        mPopupWindow?.contentView = mContentView
        // 测量宽高
        mContentView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        mWidth = mContentView!!.measuredWidth
        mHeight = mContentView!!.measuredHeight
        // 默认宽高是WRAP_CONTENT
        mPopupWindow!!.width = when (mWidth) {
            in 1..Int.MAX_VALUE,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT -> {
                mWidth
            }
            else -> ViewGroup.LayoutParams.WRAP_CONTENT
        }

        mPopupWindow!!.height = when (mHeight) {
            in 1..Int.MAX_VALUE,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT -> {
                mHeight
            }
            else -> ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    fun setOutsideEnable(touchable: Boolean): T {
        mFocusAndOutsideEnable = touchable
        return self()
    }

    /**
     * 初始化外部是否可以点击
     */
    private fun initFocusAndBack() {
        mPopupWindow?.apply {
            // 点击外部不消失
            if (!mFocusAndOutsideEnable) {
                isFocusable = true
                isOutsideTouchable = false
                setBackgroundDrawable(null)
                //注意下面这三个是contentView 不是PopupWindow，响应返回按钮事件
                contentView.isFocusable = true
                contentView.isFocusableInTouchMode = true
                contentView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dismiss()
                        return@OnKeyListener true
                    }
                    false
                })
                //在Android 6.0以上 ，只能通过拦截事件来解决
                setTouchInterceptor(View.OnTouchListener { v, event ->
                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    if (event.action == MotionEvent.ACTION_DOWN && (x < 0 || x >= mWidth || y < 0 || y >= mHeight)) {
                        //outside
                        Log.d(TAG, "onTouch outside:mWidth=$mWidth,mHeight=$mHeight")
                        return@OnTouchListener true
                    } else if (event.action == MotionEvent.ACTION_OUTSIDE) {
                        //outside
                        Log.d(TAG, "onTouch outside event:mWidth=$mWidth,mHeight=$mHeight")
                        return@OnTouchListener true
                    }
                    false
                })
            } else {
                isFocusable = true
                isOutsideTouchable = true
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    open fun showAtAnchorView(anchorView: View, @XGravity xGravity: Int, @YGravity yGravity: Int, xOff: Int, yOff: Int) {
        val newXOff = calculateX(anchorView, xGravity, mWidth, xOff)
        val newYOff = calculateY(anchorView, yGravity, mHeight, yOff)
        PopupWindowCompat.showAsDropDown(mPopupWindow!!, anchorView, newXOff, newYOff, Gravity.NO_GRAVITY)
    }

    /**
     * 计算弹出的X坐标位置
     */
    private fun calculateX(anchor: View, @XGravity xGravity: Int, popWidth: Int, xOff: Int): Int {
        return when (xGravity) {
            XGravity.LEFT -> {
                xOff - popWidth
            }
            XGravity.CENTER -> {
                xOff + (anchor.width / 2 - popWidth / 2)
            }
            XGravity.RIGHT -> {
                xOff + anchor.width
            }
            XGravity.ALIGN_LEFT -> {
                xOff
            }
            XGravity.ALIGN_RIGHT -> {
                xOff + (anchor.width - popWidth)
            }
            else -> xOff
        }
    }

    /**
     * 计算弹出的Y坐标位置
     */
    private fun calculateY(anchor: View, @YGravity yGravity: Int, popHeight: Int, yOff: Int): Int {
        return when (yGravity) {
            YGravity.ABOVE -> {
                yOff - (anchor.height + popHeight)
            }
            YGravity.CENTER -> {
                yOff - (anchor.height / 2 + popHeight / 2)
            }
            YGravity.BELOW -> {
                yOff
            }
            YGravity.ALIGN_TOP -> {
                yOff - anchor.height
            }
            YGravity.ALIGN_BOTTOM -> {
                yOff - popHeight
            }
            else -> yOff
        }
    }
}