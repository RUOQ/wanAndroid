package com.ruoq.wanAndroid.mvp.model.main.share

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.share.ShareArticleContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:05
 * @Description : 文件描述
 */
@ActivityScope
class ShareArticleModel @Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),ShareArticleContract.Model{
    @Inject
    lateinit var mGson: Gson;
    @Inject
    lateinit var mApplication: Application

    override fun addArticle(title: String, url: String): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).addArticle(title,url)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}