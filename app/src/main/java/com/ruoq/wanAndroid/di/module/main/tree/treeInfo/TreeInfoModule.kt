package com.ruoq.wanAndroid.di.module.main.tree.treeInfo

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.tree.TreeInfoContract
import com.ruoq.wanAndroid.mvp.model.main.tree.treeinfo.TreeInfoModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:49
 * @Description : 文件描述
 */
@Module
//构建TreeInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TreeInfoModule(private val view: TreeInfoContract.View) {
    @FragmentScope
    @Provides
    fun provideTreeinfoView(): TreeInfoContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideTreeInfoModel(model: TreeInfoModel): TreeInfoContract.Model {
        return model
    }
}