package com.ruoq.wanAndroid.mvp.model.main.todo

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.todo.TodoContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.TodoResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:54
 * @Description : 文件描述
 */
@ActivityScope
class TodoModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),
TodoContract.Model{
    @Inject
    lateinit var mGson:Gson

    @Inject
    lateinit var mApplication: Application
    override fun getTodoData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).getTodoData(pageNo)
    }

    override fun updateTodoData(id: Int, status: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).doneTodo(id,status)
    }

    override fun deleteTodoData(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).deleteTodo(id)
    }
}