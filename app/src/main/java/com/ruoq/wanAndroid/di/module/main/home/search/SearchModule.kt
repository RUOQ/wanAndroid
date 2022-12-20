package com.ruoq.wanAndroid.di.module.main.home.search

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchContract
import com.ruoq.wanAndroid.mvp.model.main.home.search.SearchModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:19
 * @Description : 文件描述
 */
@Module
class SearchModule(private val view:SearchContract.View) {
    @ActivityScope
    @Provides
    fun provideSearchView():SearchContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideSearchModel(model: SearchModel):SearchContract.Model{
        return model
    }
}