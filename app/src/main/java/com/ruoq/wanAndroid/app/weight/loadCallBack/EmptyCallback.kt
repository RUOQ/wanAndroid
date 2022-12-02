package com.ruoq.wanAndroid.app.weight.loadCallBack
import com.kingja.loadsir.callback.Callback
import com.ruoq.wanAndroid.R

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/27 23:18
 * @Description : 文件描述
 */
class EmptyCallback:Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty

    }
}