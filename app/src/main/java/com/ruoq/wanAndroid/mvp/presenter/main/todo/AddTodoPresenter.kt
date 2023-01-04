package com.ruoq.wanAndroid.mvp.presenter.main.todo

import android.app.Application
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.HttpUtils
import com.ruoq.wanAndroid.mvp.contract.todo.AddTodoContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 13:56
 * @Description : 文件描述
 */
@ActivityScope
class AddTodoPresenter
@Inject constructor(model:AddTodoContract.Model,rootView:AddTodoContract.View)
    :BasePresenter<AddTodoContract.Model,AddTodoContract.View>(model,rootView){

        @Inject
        lateinit var mErrorHandler:RxErrorHandler
        @Inject
        lateinit var mApplication: Application
        @Inject
        lateinit var mImageLoader:ImageLoader
        @Inject
        lateinit var mAppManager: AppManager

        fun addTodo(title:String, content:String, date:String, priority:Int){
            mModel.addTodo(title,content,date, 0, priority)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1,0))
                .doOnSubscribe {
                    mRootView.showLoading() //显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object:ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<Any>) {
                        if(response.isSuccess()){
                            mRootView.addTodoSucc()
                        }else{
                            mRootView.addTodoFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.addTodoFaild(HttpUtils.getErrorText(t))
                    }


                })
        }

    fun updateTodo(title:String,content:String,date:String,priority:Int,id:Int){
        mModel.updateTodo(title,content,date,0,priority,id)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1,0))
            .doOnSubscribe { mRootView.showLoading() }
            .subscribeOn(AndroidSchedulers.mainThread())
            .doFinally{
                mRootView.hideLoading()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object:ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                override fun onNext(t: ApiResponse<Any>) {
                    if(t.isSuccess()){
                        mRootView.addTodoSucc()
                    }else{
                        mRootView.addTodoFaild(t.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.addTodoFaild(HttpUtils.getErrorText(t))
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}