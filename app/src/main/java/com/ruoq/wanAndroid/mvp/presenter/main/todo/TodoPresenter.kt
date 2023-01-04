package com.ruoq.wanAndroid.mvp.presenter.main.todo

import android.app.Application
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.ruoq.wanAndroid.app.utils.HttpUtils
import com.ruoq.wanAndroid.mvp.contract.todo.TodoContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.TodoResponse
import io.reactivex.Scheduler
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
class TodoPresenter
@Inject constructor(model:TodoContract.Model,rootView:TodoContract.View):
BasePresenter<TodoContract.Model,TodoContract.View>(model,rootView){

    @Inject
    lateinit var mErrorHandler:RxErrorHandler
    @Inject
    lateinit var mApplication :Application
    @Inject
    lateinit var mImageLoader:ImageLoader
    @Inject
    lateinit var mAppManager:AppManager

    /**
     * 获取待办任务数据
    **/
    fun getTodoData(pageNo:Int){
        mModel.getTodoData(pageNo)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1,0))
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>(mErrorHandler){
                override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>) {
                    if(response.isSuccess()){
                        mRootView.requestDataSuccess(response.data)
                    }else{
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
     * 删除待办事项
     */
    fun delTodo(id:Int,position:Int){
        mModel.deleteTodoData(id)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1,0))
            .doOnSubscribe {
                //显示加载框
                mRootView.showLoading()
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .doFinally{
                mRootView.hideLoading() //隐藏加载框
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object:ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                override fun onNext(t: ApiResponse<Any>) {
                    if(t.isSuccess()){
                        mRootView.deleteTodoDataSuccess(position)
                    }else{
                        mRootView.updateTodoDataFailed(t.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.updateTodoDataFailed(HttpUtils.getErrorText(t))
                }
            })
    }

    fun updateTodo(id:Int,position:Int){
        mModel.updateTodoData(id,1)//1,完成
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1,0))
            .doOnSubscribe {
                mRootView.showLoading() //显示加载库
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .doFinally{
                mRootView.hideLoading() //隐藏加载框
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object:ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                override fun onNext(response: ApiResponse<Any>) {
                    if(response.isSuccess()){
                        mRootView.updateTodoDataSuccess(position)
                    }else{
                        mRootView.updateTodoDataFailed(response.errorMsg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.updateTodoDataFailed(HttpUtils.getErrorText(t))
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}