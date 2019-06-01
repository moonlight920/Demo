package com.yf.popbubble.bubble

internal interface OnBubbleLayoutSizeChangeListener {
    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
}