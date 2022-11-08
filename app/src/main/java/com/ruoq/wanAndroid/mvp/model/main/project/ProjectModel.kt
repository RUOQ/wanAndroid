package com.ruoq.wanAndroid.mvp.model.main.project

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:36
 * @Description : 文件描述
 */
@FragmentScope
class ProjectModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),ProjectContract.Model
{
    @Inject
    lateinit var mGson:Gson

    @Inject
    lateinit var mApplication:Application
    override fun getTitles(): Observable<ApiResponse<MutableList<ClassifyResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getProjectTypes()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}