package com.okay.demo

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.yuefeng.lib.OkBasePopup
import com.yuefeng.lib.XGravity
import com.yuefeng.lib.YGravity
import com.yuefeng.lib.bubble.BubbleLayout

class BubblePopupWindow private constructor(private var mContext: Context) : OkBasePopup<BubblePopupWindow>() {

    companion object {
        @JvmStatic
        fun create(context: Context): BubblePopupWindow {
            return BubblePopupWindow(context)
        }
    }

    private var bubbleLayout: BubbleLayout? = null

    override fun initPopupWindowAttr() {
        setContentView(mContext, R.layout.pop_example1).setOutsideEnable(true)
    }

    override fun initView(mContentView: View, popupWindow: BubblePopupWindow) {
        bubbleLayout = mContentView.findViewById(R.id.bubbleLayout) as BubbleLayout
//        bubbleLayout.triangleMarginRight = 50
        val listView = mContentView.findViewById(R.id.listView) as ListView
        listView.adapter = ArrayAdapter(mContext, android.R.layout.simple_list_item_1, arrayListOf("1", "2", "3"))
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
        // 设置箭头的偏移量
        when (xGravity) {
            XGravity.LEFT, XGravity.RIGHT -> {
                when (yGravity) {
                    YGravity.ALIGN_BOTTOM -> {
                        bubbleLayout?.triangleMarginBottom = anchorView.height / 2 + yOff
                    }
                    YGravity.ALIGN_TOP -> {
                        bubbleLayout?.triangleMarginTop = anchorView.height / 2 - yOff
                    }
                    YGravity.CENTER -> {
                        bubbleLayout?.setTriangleOffset(-yOff)
                    }
                    YGravity.BELOW, YGravity.ABOVE -> {
                        // 参数异常，不做绘制
                        bubbleLayout?.setDirection(BubbleLayout.NONE)
                    }
                }
            }
        }
        when (yGravity) {
            YGravity.BELOW, YGravity.ABOVE -> {
                when (xGravity) {
                    XGravity.CENTER -> {
                        bubbleLayout?.setTriangleOffset(-xOff)
                    }
                    XGravity.ALIGN_RIGHT -> {
                        bubbleLayout?.triangleMarginRight = anchorView.width / 2 + xOff
                    }
                    XGravity.ALIGN_LEFT -> {
                        bubbleLayout?.triangleMarginLeft = anchorView.width / 2 - xOff
                    }
                    XGravity.RIGHT,XGravity.LEFT -> {
                        // 参数异常，不做绘制
                        bubbleLayout?.setDirection(BubbleLayout.NONE)
                    }
                }
            }
        }

        super.showAtAnchorView(anchorView, xGravity, yGravity, xOff, yOff)
    }

}