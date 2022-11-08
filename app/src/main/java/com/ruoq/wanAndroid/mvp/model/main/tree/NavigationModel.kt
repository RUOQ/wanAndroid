package com.ruoq.wanAndroid.mvp.model.main.tree

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.tree.NavigationContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.NavigationResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:57
 * @Description : 文件描述
 */
@FragmentScope
class NavigationModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), NavigationContract.Model {
    override fun getNavigationData(): Observable<ApiResponse<MutableList<NavigationResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getNavigationData()
    }

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun onDestroy() {
        super.onDestroy()
    }
}