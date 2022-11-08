package com.ruoq.wanAndroid.mvp.model.main.share

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.share.ShareByIdContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ShareResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 18:08
 * @Description : 文件描述
 */
@ActivityScope
class ShareByIdModel
@Inject
constructor(repositoryManger:IRepositoryManager):BaseModel(repositoryManger),ShareByIdContract.Model
{
    override fun getShareData(pageNo: Int, id: Int): Observable<ApiResponse<ShareResponse>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getShareByidData(pageNo,id)

    }

    //取消收藏
    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .unCollect(id)
    }
    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .collect(id)
    }

    @Inject
    lateinit var mGson: Gson;
    @Inject
    lateinit var mApplication: Application;

    override fun onDestroy() {
        super.onDestroy();
    }
}