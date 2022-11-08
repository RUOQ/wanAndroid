package com.ruoq.wanAndroid.mvp.model.main.me

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.me.MeContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.IntegralResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:35
 * @Description : 文件描述
 */
@FragmentScope
class MeModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), MeContract.Model {
    override fun getIntegral(): Observable<ApiResponse<IntegralResponse>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getIntegral()

    }

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun onDestroy() {
        super.onDestroy()
    }
}
