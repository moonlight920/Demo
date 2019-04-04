package com.okay.demo

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.yuefeng.lib.OkBasePopup
import com.yuefeng.lib.XGravity
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
     * 显示的时候重新计算 气泡的箭头基点位置
     */
    override fun showAtAnchorView(anchorView: View, xGravity: Int, yGravity: Int, xOff: Int, yOff: Int) {


        when (xGravity) {
            XGravity.ALIGN_RIGHT -> {
                bubbleLayout?.triangleMarginRight = anchorView.width / 2 + xOff
            }
            XGravity.ALIGN_LEFT -> {
                bubbleLayout?.triangleMarginLeft = anchorView.width / 2 - xOff
            }
        }

        super.showAtAnchorView(anchorView, xGravity, yGravity, xOff, yOff)
    }

}