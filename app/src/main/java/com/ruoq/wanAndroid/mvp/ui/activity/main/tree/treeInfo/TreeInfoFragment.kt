package com.ruoq.wanAndroid.mvp.ui.activity.main.tree.treeInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.mvp.contract.main.tree.TreeInfoContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.presenter.main.tree.treeinfo.TreeInfoPresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:08
 * @Description : 文件描述
 */
class TreeInfoFragment: BaseFragment<TreeInfoPresenter>(),TreeInfoContract.View {
    lateinit var loadSir:LoadService<Any>
    lateinit var adapter:ArticleAdapter
    private var initPageNo = 0 //注意体系页码从0 开始
    private var cid :Int = 0
    lateinit var footView:DefineLoadMoreView
    override fun setupFragmentComponent(appComponent: AppComponent) {
        TODO("Not yet implemented")
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        TODO("Not yet implemented")
    }

    override fun requestDataSucc(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>) {
        TODO("Not yet implemented")
    }

    override fun requestDataFaild(errorMsg: String) {
        TODO("Not yet implemented")
    }

    override fun collect(collected: Boolean, position: Int) {
        TODO("Not yet implemented")
    }
}