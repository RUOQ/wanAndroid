package com.ruoq.wanAndroid.di.module.main.home.search

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchResultContract
import com.ruoq.wanAndroid.mvp.model.main.home.search.SearchResultModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:20
 * @Description : 文件描述
 */
@Module
class SearchResultModule(private val view:SearchResultContract.View){
    @ActivityScope
    @Provides
    fun provideSearchResultView():SearchResultContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideSearchResultModel(model: SearchResultModel):SearchResultContract.Model{
        return model
    }
}