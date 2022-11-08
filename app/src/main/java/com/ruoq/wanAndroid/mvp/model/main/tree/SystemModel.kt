package com.ruoq.wanAndroid.mvp.model.main.tree

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.tree.SystemContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.SystemResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:01
 * @Description : 文件描述
 */
@FragmentScope
class SystemModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    SystemContract.Model {
    override fun getSystemData(): Observable<ApiResponse<MutableList<SystemResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getSystemData()
    }

    @Inject
    lateinit var mGson:Gson

    @Inject

    lateinit var mApplication:Application
}