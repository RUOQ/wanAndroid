package com.ruoq.wanAndroid.mvp.presenter.main.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.HttpUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 13:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class HomePresenter
@Inject
constructor(model: HomeContract.Model, rootView: HomeContract.View) :
        BasePresenter<HomeContract.Model, HomeContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    @Inject
    lateinit var adapter: ArticleAdapter

    /**
     * 获取首页banner数据
     */
    fun getBanner() {
        mModel.getBannerList()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView,FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<BannerResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<BannerResponse>>) {
                        if (response.isSuccess()) {
                            mRootView.requestBannerSuccess(response.data)
                        }
                    }
                })
    }

    /**
     * 获取文章列表集合数据
     */
    fun getAriList(pageNo: Int) {
        var data: ApiPagerResponse<MutableList<ArticleResponse>>
        mModel.getArticleList(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView,FragmentEvent.DESTROY))//fragment的绑定方式 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>(mErrorHandler) {
                    @SuppressLint("LogNotTimber")
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>) {
                        if (response.isSuccess()) {
                            data = response.data
                            Log.e("qin","data--${data.size}")
                            if (SettingUtil.getRequestTop(mApplication) && pageNo == 0) {
                                //如果设置的时获取置顶文章，并且当前请求是第一页的话----获取首页置顶文章
                                mModel.getTopArticleList()
                                        .subscribeOn(Schedulers.io())
                                        .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .compose(RxLifecycleUtils.bindUntilEvent(mRootView,FragmentEvent.DESTROY))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                                        .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<ArticleResponse>>>(mErrorHandler) {
                                            override fun onNext(response: ApiResponse<MutableList<ArticleResponse>>) {
                                                if (response.isSuccess()) {
                                                    //获取置顶文章成功，吧数据插到前面
                                                    Log.e("qin","data--获取置顶数据成功")
                                                    data.datas.addAll(0, response.data)
                                                    mRootView.requestArticleSuccess(data)
                                                } else {
                                                    //获取置顶文章失败，那就不管他了
                                                    Log.e("qin","data--获取置顶数据失败")
                                                    mRootView.requestArticleSuccess(data)
                                                }
                                            }

                                            override fun onError(t: Throwable) {
                                                super.onError(t)
                                                //获取置顶文章失败，那就不管他了
                                                mRootView.requestArticleSuccess(data)
                                            }
                                        })
                            } else {
                                Log.e("qin","data--没有获取置顶数据")
                                mRootView.requestArticleSuccess(response.data)
                            }
                        } else {
                            Log.e("qin","data--数据错误")
                            mRootView.requestArticleFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        Log.e("qin","data--直接报错")
                        mRootView.requestArticleFailed(HttpUtils.getErrorText(t))
                    }
                })
    }

    /**
     * 收藏
     */
    fun collect(id:Int,position:Int) {
        mModel.collect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView,FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSuccess()) {
                            //收藏成功
                            mRootView.collect(true,position)
                        }else{
                            //收藏失败
                            mRootView.collect(false,position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //收藏失败
                        mRootView.collect(false,position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })
    }

    /**
     * 取消收藏
     */
    fun uncollect(id:Int,position:Int) {
        mModel.unCollect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView,FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSuccess()) {
                            //取消收藏成功
                            mRootView.collect(false,position)
                        }else{
                            //取消收藏失败
                            mRootView.collect(true,position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //取消收藏失败
                        mRootView.collect(true,position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })
    }



}