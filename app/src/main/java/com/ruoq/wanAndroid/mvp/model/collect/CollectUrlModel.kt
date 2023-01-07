package com.ruoq.wanAndroid.mvp.model.collect

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.collect.CollectUrlContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 13:23
 * @Description : 文件描述
 */
@FragmentScope
class CollectUrlModel
@Inject
constructor(repositoryManager:IRepositoryManager): BaseModel(repositoryManager),CollectUrlContract.Model{
    @Inject
    lateinit var mGson:Gson
    @Inject
    lateinit var mApplication: Application
    override fun getCollectUrlDates(): Observable<ApiResponse<MutableList<CollectUrlResponse>>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .getCollectUrlData()
    }

    override fun unCollectList(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .deleteTool(id)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}