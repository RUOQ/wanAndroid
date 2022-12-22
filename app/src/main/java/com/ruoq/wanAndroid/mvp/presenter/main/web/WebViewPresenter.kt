package com.ruoq.wanAndroid.mvp.presenter.main.web

import android.app.Application
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.HttpUtils
import com.ruoq.wanAndroid.mvp.contract.web.WebViewContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/22 22:01
 * @Description : 文件描述
 */
@ActivityScope
class WebViewPresenter
@Inject
constructor(model:WebViewContract.Model,rootView:WebViewContract.View):
BasePresenter<WebViewContract.Model,WebViewContract.View>(model,rootView){

    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader:ImageLoader
    @Inject
    lateinit var mAppManager:AppManager


    /**
     * 收藏
     */
    fun collect(id:Int){
        mModel.collect(id)
            .subscribeOn(Schedulers.io())
            .retryWhen (RetryWithDelay(1,0))
            .subscribeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object:ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                override fun onNext(response: ApiResponse<Any>) {
                   if(response.isSuccess()){
                       //收藏成功
                       mRootView.collect(true)
                   }else{
                       mRootView.collect(false)
                       mRootView.showMessage(response.errorMsg)
                   }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    //收藏失败
                    mRootView.collect(false)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }

            })
    }



    /**
     * 收藏网址
     */
    fun collectUrl(name:String,link:String) {
        mModel.collectUrl(name,link)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<CollectUrlResponse>>(mErrorHandler) {
                override fun onNext(response: ApiResponse<CollectUrlResponse>) {
                    if (response.isSuccess()) {
                        //收藏成功
                        mRootView.collectUrlSucc(true,response.data)
                    }else{
                        //收藏失败
                        mRootView.collect(false)
                        mRootView.showMessage(response.errorMsg)
                    }
                }
                override fun onError(t: Throwable) {
                    super.onError(t)
                    //收藏失败
                    mRootView.collect(false)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }
            })
    }

    /**
     * 取消收藏
     */
    fun unCollect(id:Int) {
        mModel.unCollect(id)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                override fun onNext(response: ApiResponse<Any>) {
                    if (response.isSuccess()) {
                        //取消收藏成功
                        mRootView.collect(false)
                    }else{
                        //取消收藏失败
                        mRootView.collect(true)
                        mRootView.showMessage(response.errorMsg)
                    }
                }
                override fun onError(t: Throwable) {
                    super.onError(t)
                    //取消收藏失败
                    mRootView.collect(true)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }
            })
    }
    /**
     * 取消收藏
     */
    fun unCollectList(id:Int,originId:Int) {
        mModel.unCollectList(id,originId)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                override fun onNext(response: ApiResponse<Any>) {
                    if (response.isSuccess()) {
                        //取消收藏成功
                        mRootView.collect(false)
                    }else{
                        //取消收藏失败
                        mRootView.collect(true)
                        mRootView.showMessage(response.errorMsg)
                    }
                }
                override fun onError(t: Throwable) {
                    super.onError(t)
                    //取消收藏失败
                    mRootView.collect(true)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }
            })
    }

    /**
     * 取消收藏网址
     */
    fun unCollectUrl(id: Int) {
        mModel.unCollectUrl(id)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                override fun onNext(response: ApiResponse<Any>) {
                    if (response.isSuccess()) {
                        //取消收藏成功
                        mRootView.collect(false)
                    } else {
                        //取消收藏失败
                        mRootView.collect(true)
                        mRootView.showMessage(response.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    //取消收藏失败
                    mRootView.collect(true)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }
            })
    }

}