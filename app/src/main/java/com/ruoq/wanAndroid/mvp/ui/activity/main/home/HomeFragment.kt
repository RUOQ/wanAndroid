package com.ruoq.wanAndroid.mvp.ui.activity.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.di.component.home.DaggerHomeComponent
import com.ruoq.wanAndroid.di.module.main.home.HomeModule
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.ruoq.wanAndroid.mvp.presenter.main.home.HomePresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:05
 * @Description : 文件描述
 */
class HomeFragment :BaseFragment<HomePresenter>(),HomeContract.View{
    private var initPageNo = 0 //  注意，主页的页码从0开始的
    var pageNo = initPageNo

    @Inject
    lateinit var adapter:ArticleAdapter
    lateinit var loadSir: LoadService<Any>
    lateinit var footView:DefineLoadMoreView
    companion object{
        fun newInstance():HomeFragment {
            return HomeFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerHomeComponent.builder()
            .appComponent(appComponent)
            .homeModule(HomeModule(this))
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //加上这句话，menu才会显示出来
        setHasOptionsMenu(true)
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