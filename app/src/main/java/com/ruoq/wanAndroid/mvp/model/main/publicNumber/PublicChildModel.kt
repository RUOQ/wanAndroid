package com.ruoq.wanAndroid.mvp.model.main.publicNumber

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicChildContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:47
 * @Description : 文件描述
 */
@FragmentScope
class PublicChildModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),
PublicChildContract.Model{

    @Inject
    lateinit var mGson:Gson

    @Inject
    lateinit var mApplication:Application

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun getPublicDates(
        pageNo: Int,
        cid: Int
    ): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).getPublicNewData(pageNo,cid)
    }

    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .collect(id)
    }

    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .unCollect(id)
    }
}