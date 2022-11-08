package com.ruoq.wanAndroid.mvp.model.main.project

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectChildContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:39
 * @Description : 文件描述
 */
@FragmentScope
class ProjectChildModel
@Inject constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    ProjectChildContract.Model {

    @Inject
    lateinit var mGson:Gson
    @Inject
    lateinit var mApplication:Application

    override fun getProjects(
        pageNo: Int,
        cid: Int
    ): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getProjectDataByType(pageNo,cid)
    }

    override fun getNewProjects(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getProjectNewData(pageNo)
    }

    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .collect(id)
    }

    override fun uncollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .unCollect(id)
    }
}