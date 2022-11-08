package com.ruoq.wanAndroid.mvp.model.integral

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.integral.IntegralContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.IntegralHistoryResponse
import com.ruoq.wanAndroid.mvp.model.entity.IntegralResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 21:29
 * @Description : 文件描述
 */
@ActivityScope
class IntegralModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager)
,IntegralContract.Model{
    @Inject
    lateinit var mGson:Gson
    @Inject
    lateinit var mApplication:Application

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getIntegralData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .getIntegralRank(pageNo)
    }

    override fun getIntegralHistoryData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>> {
       return mRepositoryManager.obtainRetrofitService(Api::class.java)
           .getIntegralHistory(pageNo)
    }
}