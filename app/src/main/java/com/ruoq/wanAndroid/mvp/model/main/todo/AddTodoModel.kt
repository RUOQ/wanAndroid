package com.ruoq.wanAndroid.mvp.model.main.todo

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.todo.AddTodoContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:51
 * @Description : 文件描述
 */
@ActivityScope
class AddTodoModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),AddTodoContract.Model
{

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application
    override fun addTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .addTodo(title,content,date,type,priority)
    }

    override fun updateTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int,
        id: Int
    ): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .updateTodo(title, content, date, type, priority,id)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}