package com.ruoq.wanAndroid.mvp.presenter

import android.app.Application
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.ruoq.wanAndroid.mvp.contract.MainContract
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/9 18:11
 * @Description : 文件描述
 */
@FragmentScope
class MainPresenter
@Inject
constructor(model:MainContract.Model,rootView:MainContract.View):
BasePresenter<MainContract.Model,MainContract.View>(model,rootView)
{
    @Inject
    lateinit var mErrorHandler:RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader:ImageLoader
    @Inject
    lateinit var mAppManager:AppManager
}