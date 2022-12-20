package com.ruoq.wanAndroid.mvp.model

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.MainContract
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/6 19:04
 * @Description : 文件描述
 */
@FragmentScope
class MainModel
@Inject
constructor(repositoryManager:IRepositoryManager):BaseModel(repositoryManager),
MainContract.Model{
    @Inject
    lateinit var mGson: Gson;
    @Inject
    lateinit var mApplication: Application;

    override fun onDestroy() {
        super.onDestroy();
    }
}