package com.ruoq.wanAndroid.mvp.model.main.publicNumber

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicContract
import com.ruoq.wanAndroid.mvp.model.api.Api
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/4 14:48
 * @Description : 文件描述
 */
@FragmentScope
class PublicModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), PublicContract.Model {

    @Inject
    lateinit var mGson: Gson;
    @Inject
    lateinit var mApplication: Application;

    override fun onDestroy() {
        super.onDestroy();
    }

    override fun getTitles(): Observable<ApiResponse<MutableList<ClassifyResponse>>> {
        return mRepositoryManager
            .obtainRetrofitService(Api::class.java)
            .getPublicTypes()
    }

}
