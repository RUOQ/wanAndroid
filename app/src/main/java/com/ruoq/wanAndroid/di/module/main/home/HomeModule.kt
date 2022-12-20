package com.ruoq.wanAndroid.di.module.main.home

import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.main.home.HomeModel
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:20
 * @Description : 文件描述
 */
@Module
class HomeModule(private val view:HomeContract.View){
    @FragmentScope
    @Provides
    fun provideHomeView():HomeContract.View{
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideHomeModel(model: HomeModel):HomeContract.Model{
        return model
    }


    @FragmentScope
    @Provides
    fun getData():MutableList<ArticleResponse>{
        return mutableListOf()
    }

    @FragmentScope
    @Provides
    fun getAdapter(data:MutableList<ArticleResponse>):ArticleAdapter{
        return ArticleAdapter(data,true)
    }
}