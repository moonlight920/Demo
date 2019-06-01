package com.yf.demo

import android.app.Application
import com.yf.smarttemplate.SmartTemplate

/**
 * Created by yf.
 * @date 2019-05-28
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SmartTemplate.init(this){
            fragmentItem(Demo1Fragment::class.java){
                title = "用于演示4个方向弹出气泡"
            }
        }
    }
}