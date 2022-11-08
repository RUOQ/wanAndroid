package com.ruoq.wanAndroid.mvp.model.main.web

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.web.WebViewContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 19:01
 * @Description : 文件描述
 */
@ActivityScope
class WebviewModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), WebViewContract.Model {


    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    //取消收藏
    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .unCollect(id)
    }
    //取消收藏
    override fun unCollectList(id: Int, originId: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .unCollectList(id,originId)
    }
    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .collect(id)
    }
    override fun collectUrl(name: String, link: String): Observable<ApiResponse<CollectUrlResponse>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .collectUrl(name,link)
    }
    //取消收藏网址
    override fun unCollectUrl(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .deleteTool(id)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
