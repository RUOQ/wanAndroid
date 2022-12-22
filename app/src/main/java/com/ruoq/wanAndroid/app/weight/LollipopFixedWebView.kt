package com.ruoq.wanAndroid.app.weight

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/22 23:16
 * @Description : 文件描述
 */
class LollipopFixedWebView : WebView {
    constructor(context: Context) : super(getFixedContext(context))
    constructor(context: Context, attrs: AttributeSet) : super(getFixedContext(context), attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(getFixedContext(context), attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(getFixedContext(context), attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, privateBrowsing: Boolean) : super(getFixedContext(context), attrs, defStyleAttr, privateBrowsing)

    companion object {
        fun getFixedContext(context: Context): Context {
            return context.createConfigurationContext(Configuration())
        }
    }

}