package com.yuefeng.lib

import android.content.Context
import android.util.Log
import android.view.View
import com.yuefeng.lib.bubble.BubbleLayout
import com.yuefeng.lib.bubble.OnBubbleLayoutSizeChangeListener

class BubblePopupWindow private constructor(private var mContext: Context, private var mBubbleContentView: View) :
    OkBasePopup<BubblePopupWindow>() {

    companion object {
        @JvmStatic
        fun create(context: Context, bubbleContentView: View): BubblePopupWindow {
            return BubblePopupWindow(context, bubbleContentView).build()
        }
    }

    private var bubbleLayout: BubbleLayout? = null

    override fun initPopupWindowAttr() {
        setContentView(mContext, R.layout.poplib_bubble_pop).setOutsideEnable(true)
    }

    override fun initView(mContentView: View, popupWindow: BubblePopupWindow) {
        bubbleLayout = mContentView.findViewById(R.id.bubbleLayout) as BubbleLayout
        if (mBubbleContentView.parent != null) {
            throw IllegalArgumentException("this view already has parent")
        }
        bubbleLayout?.addView(mBubbleContentView)
    }

    /**
     * 显示的时候重新计算气泡的箭头基点位置,对准anchorView
     */
    override fun showAtAnchorView(anchorView: View, xGravity: Int, yGravity: Int, xOff: Int, yOff: Int) {
        // 设置箭头的朝向
        bubbleLayout?.setDirection(
            when {
                xGravity == XGravity.LEFT -> BubbleLayout.RIGHT
                xGravity == XGravity.RIGHT -> BubbleLayout.LEFT
                yGravity == YGravity.ABOVE -> BubbleLayout.BOTTOM
                yGravity == YGravity.BELOW -> BubbleLayout.TOP
                else -> BubbleLayout.NONE
            }
        )
        bubbleLayout?.onBubbleLayoutSizeChangeListener = object : OnBubbleLayoutSizeChangeListener {
            // 气泡布局测量完成之后，计算箭头位置
            override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                Log.d(TAG, "onSizeChanged w:$w h:$h")
                when (xGravity) {
                    XGravity.LEFT, XGravity.RIGHT -> {
                        when (yGravity) {
                            YGravity.ALIGN_BOTTOM -> {
                                val marginBottom = anchorView.height / 2 + yOff
                                bubbleLayout?.offset = h / 2 - marginBottom
                            }
                            YGravity.ALIGN_TOP -> {
                                val marginTop = anchorView.height / 2 - yOff
                                bubbleLayout?.offset = -(h / 2 - marginTop)
                            }
                            YGravity.CENTER -> {
                                bubbleLayout?.offset = -yOff
                            }
                            else -> {
                                // 参数异常
                                bubbleLayout?.setDirection(BubbleLayout.NONE)
                            }
                        }
                    }
                }
                when (yGravity) {
                    YGravity.BELOW, YGravity.ABOVE -> {
                        when (xGravity) {
                            XGravity.CENTER -> {
                                bubbleLayout?.offset = -xOff
                            }
                            XGravity.ALIGN_RIGHT -> {
                                val marginRight = anchorView.width / 2 + xOff
                                bubbleLayout?.offset = w / 2 - marginRight
                            }
                            XGravity.ALIGN_LEFT -> {
                                val marginLeft = anchorView.width / 2 - xOff
                                bubbleLayout?.offset = -(w / 2 - marginLeft)
                            }
                            else -> {
                                // 参数异常
                                bubbleLayout?.setDirection(BubbleLayout.NONE)
                            }
                        }
                    }
                }
            }
        }
        super.showAtAnchorView(anchorView, xGravity, yGravity, xOff, yOff)
    }
}