package com.yf.popbubble

import android.support.annotation.IntDef

@IntDef(
    XGravity.CENTER,
    XGravity.LEFT,
    XGravity.RIGHT,
    XGravity.ALIGN_LEFT,
    XGravity.ALIGN_RIGHT
)
@Retention(AnnotationRetention.SOURCE)
annotation class XGravity {
    companion object {
        const val CENTER = 0
        const val LEFT = 1
        const val RIGHT = 2
        const val ALIGN_LEFT = 3
        const val ALIGN_RIGHT = 4
    }
}