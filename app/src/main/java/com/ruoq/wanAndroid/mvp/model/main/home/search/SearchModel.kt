package com.ruoq.wanAndroid.mvp.model.main.home.search

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchContract
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchResultContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.SearchResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 21:45
 * @Description : 文件描述
 */
@ActivityScope
class SearchModel
@Inject constructor(repositoryManager: IRepositoryManager) :
    BaseModel(repositoryManager) ,SearchContract.Model{

    @Inject
    lateinit var mGson: Gson

    @Inject
    lateinit var mApplication: Application
    override fun getHotData(): Observable<ApiResponse<MutableList<SearchResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getSearchData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}