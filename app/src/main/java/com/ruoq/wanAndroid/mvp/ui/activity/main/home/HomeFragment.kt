package com.ruoq.wanAndroid.mvp.ui.activity.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.ruoq.wanAndroid.mvp.presenter.main.home.HomePresenter

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:05
 * @Description : 文件描述
 */
class HomeFragment :BaseFragment<HomePresenter>(),HomeContract.View{
    companion object{
        fun newInstance():HomeFragment {
            return HomeFragment()
        }
    }

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

    override fun requestBannerSuccess(banners: MutableList<BannerResponse>) {
        TODO("Not yet implemented")
    }

    override fun requestArticleSuccess(articles: ApiPagerResponse<MutableList<ArticleResponse>>) {
        TODO("Not yet implemented")
    }

    override fun requestArticleFailed(errorMsg: String) {
        TODO("Not yet implemented")
    }

    override fun collect(collected: Boolean, position: Int) {
        TODO("Not yet implemented")
    }
}