package com.ruoq.wanAndroid.mvp.presenter.main.publicNumber

import android.app.Application
import com.google.gson.Gson

import com.jess.arms.integration.AppManager
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.CacheUtil
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:02
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class PublicPresenter
@Inject
constructor(model: PublicContract.Model, rootView: PublicContract.View) :
        BasePresenter<PublicContract.Model, PublicContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    fun getProjectTitles() {
        val datas = CacheUtil.getPublicTitles()
        if(datas.size!=0){
            mRootView.requestTitleSuccess(datas)
        }
        mModel.getTitles()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//activity的绑定方式 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<ClassifyResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<ClassifyResponse>>) {
                        if (response.isSuccess()) {
                            CacheUtil.setPublicTitles(Gson().toJson(response.data))
                            if(datas.size==0){
                                mRootView.requestTitleSuccess(response.data)
                            }
                        } else {
                            mRootView.requestTitleSuccess(datas)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        if(datas.size==0){
                            mRootView.requestTitleSuccess(datas)
                        }
                    }

                })
    }

}
