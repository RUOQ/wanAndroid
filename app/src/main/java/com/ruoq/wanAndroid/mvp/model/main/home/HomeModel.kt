package com.ruoq.wanAndroid.mvp.model.main.home

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:30
 * @Description : 文件描述
 */
@FragmentScope
class HomeModel
@Inject constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    HomeContract.Model {

    @Inject
    lateinit var mGson:Gson

    @Inject
    lateinit var mApplication:Application

    override fun getArticleList(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return  mRepositoryManager.obtainRetrofitService(Api::class.java)
            .getArticleList(pageNo)
    }

    override fun getTopArticleList(): Observable<ApiResponse<MutableList<ArticleResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getTopArticleList()
    }

    override fun getBannerList(): Observable<ApiResponse<MutableList<BannerResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getBanner()
    }

    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).collect(id)
        }

    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java).unCollect(id)
    }
}