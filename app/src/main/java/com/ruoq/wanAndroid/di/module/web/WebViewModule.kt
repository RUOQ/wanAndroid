package com.ruoq.wanAndroid.di.module.web

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.web.WebViewContract
import com.ruoq.wanAndroid.mvp.model.main.web.WebviewModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/22 22:30
 * @Description : 文件描述
 */
@Module
class WebViewModule(private val view:WebViewContract.View) {
    @ActivityScope
    @Provides
    fun provideWebViewView():WebViewContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideWebViewModel(model:WebviewModel):WebViewContract.Model{
        return model
    }
}