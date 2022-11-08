package com.ruoq.wanAndroid.mvp.presenter.collect

import android.app.Application
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.HttpUtils
import com.ruoq.wanAndroid.mvp.contract.collect.CollectUrlContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 19:06
 * @Description : 文件描述
 */
@FragmentScope
class CollectUrlPresenter
@Inject
constructor(model:CollectUrlContract.Model,rootView:CollectUrlContract.View)
    :BasePresenter<CollectUrlContract.Model,CollectUrlContract.View>(model,rootView){
        @Inject
        lateinit var mErrorHandler:RxErrorHandler
        @Inject
        lateinit var mApplication:Application
        @Inject
        lateinit var mImageLoader: ImageLoader
        @Inject
        lateinit var mAppManager:AppManager

        fun getCollectUrlData(){
            mModel.getCollectUrlDates()
                .subscribeOn(Schedulers.io())
                    //遇到错误时重试，第一个参数为重试几次，第二个参数为重试的间隔
                .retryWhen(RetryWithDelay(1,0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<CollectUrlResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<CollectUrlResponse>>) {
                        if (response.isSuccess()) {
                            mRootView.requestDataUrlSuccess(response.data)
                        } else {
                            mRootView.requestDataFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestDataFailed(HttpUtils.getErrorText(t))
                    }
                })
        }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int, position: Int) {
        mModel.unCollectList(id)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                override fun onNext(response: ApiResponse<Any>) {
                    if (response.isSuccess()) {
                        //取消收藏成功
                        mRootView.unCollect(position)
                    } else {
                        //取消收藏失败
                        mRootView.uncollectFailed(position)
                        mRootView.showMessage(response.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    //取消收藏失败
                    mRootView.uncollectFailed(position)
                    mRootView.showMessage(HttpUtils.getErrorText(t))
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}