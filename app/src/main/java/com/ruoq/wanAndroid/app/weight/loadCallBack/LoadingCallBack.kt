package com.ruoq.wanAndroid.app.weight.loadCallBack
import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.ruoq.wanAndroid.R


/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/27 23:18
 * @Description : 文件描述
 */
class LoadingCallBack:Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun getSuccessVisible(): Boolean {
        return super.getSuccessVisible()
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}