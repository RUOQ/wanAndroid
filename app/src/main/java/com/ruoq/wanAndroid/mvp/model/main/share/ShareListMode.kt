package com.ruoq.wanAndroid.mvp.model.main.share

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.share.ShareListContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ShareResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:10
 * @Description : 文件描述
 */
@ActivityScope
class ShareListMode
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),
        ShareListContract.Model

{
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application
    override fun getShareData(pageNo: Int): Observable<ApiResponse<ShareResponse>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .getShareData(pageNo)
    }

    override fun deleteShareData(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
            .deleteShareData(id)
    }
}