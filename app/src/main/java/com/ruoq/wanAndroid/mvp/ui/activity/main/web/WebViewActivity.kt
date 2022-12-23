package com.ruoq.wanAndroid.mvp.ui.activity.main.web

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.jess.arms.di.component.AppComponent
import com.just.agentweb.AgentWeb
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.CollectEvent
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.utils.CacheUtil
import com.ruoq.wanAndroid.app.utils.startActivityKx
import com.ruoq.wanAndroid.app.weight.LollipopFixedWebView
import com.ruoq.wanAndroid.databinding.ActivityMainBinding
import com.ruoq.wanAndroid.databinding.ActivityWebviewBinding
import com.ruoq.wanAndroid.di.component.web.DaggerWebViewComponent
import com.ruoq.wanAndroid.di.module.web.WebViewModule
import com.ruoq.wanAndroid.mvp.contract.web.WebViewContract
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import com.ruoq.wanAndroid.mvp.model.entity.enums.CollectType
import com.ruoq.wanAndroid.mvp.presenter.main.web.WebViewPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.start.LoginActivity
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import org.greenrobot.eventbus.Subscribe

class WebViewActivity : BaseActivity<WebViewPresenter>(),WebViewContract.View {
    //是否收藏
    var collect = false
    var id = 0
    //标题
    lateinit var showTitle:String
    lateinit var url:String
    //需要收藏的类型，具体类型参数说明请看CollectType枚举类
    var collectType = 0
    private lateinit var mAgentWeb:AgentWeb
    private var _binding:ActivityWebviewBinding ?= null
    private val binding get() = _binding!!

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerWebViewComponent
            .builder()
            .appComponent(appComponent)
            .webViewModule(WebViewModule(this))
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
       //因为有多个地方进入详情，且数据结构不同，
        //如文章，轮播页，收藏文章列表，收藏地址列表邓
        //做收藏的话需要判断，这里搞了感觉很多余的处理
        _binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getSerializableExtra("data")?.let {
            it as ArticleResponse
            id = it.id
            showTitle = it.title
            collect = it.collect
            url = it.link
            collectType = CollectType.Ariticle.type
        }

        //点击首页轮播图进来的
        intent.getSerializableExtra("bannerdata")?.let {
            it  as BannerResponse
            id = it.id
            showTitle = it.title
            collect = false // 从首页轮播图进来的，没法判断是否已经收藏过，所以默认没有收藏
            url = it.url
            collectType = CollectType.Url.type
        }

        //从收藏文章列表进来的
        intent.getSerializableExtra("collect")?.let {
            it as CollectResponse
            id = it.originId
            showTitle = it.title
            collect = true
            url = it.link
            collectType = CollectType.Ariticle.type

        }

        //点击收藏网址列表进来的
        intent.getSerializableExtra("collectUrl")?.let {
            it as CollectUrlResponse
            id = it.id
            showTitle = it.name
            collect = true
            url = it.link
            collectType = CollectType.Url.type
        }

        binding.toolBar.run{
            setSupportActionBar(this)
            title = Html.fromHtml(showTitle)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                finish()
            }
        }

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.webViewContent,
                LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator()
            .setWebView(LollipopFixedWebView(this))
            .createAgentWeb()
            .ready()
            .go(url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(mAgentWeb.handleKeyEvent(keyCode,event)){
            true
        }else super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }




    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //如果收藏了，右上角的图标相应改变
        if(collect){
            menu.findItem(R.id.web_collect).icon = ContextCompat.getDrawable(this,R.drawable.ic_collected)
        }else{
            menu.findItem(R.id.web_collect).icon = ContextCompat.getDrawable(this,R.drawable.ic_collect)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.web_share ->{
                //分享
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,"$showTitle:$url")
                    type = "text/plain"
                },"分享到"))
            }

            R.id.web_refresh ->{
                //刷新网页
                mAgentWeb.urlLoader.reload()
            }

            R.id.web_collect ->{
                //点击收藏
                //是否已经登录，没有登录跳转到登录页
                if(CacheUtil.isLogin()){
                    if(collect){
                        if(collectType == CollectType.Url.type){
                            //取消收藏网址
                            mPresenter?.unCollectUrl(id)
                        }else{
                            //取消收藏文章
                            mPresenter?.unCollect(id)
                        }
                    }else{
                        if(collectType == CollectType.Url.type){
                            //收藏网址
                            mPresenter?.collectUrl(showTitle,url)
                        }else{
                            mPresenter?.collect(id)
                        }
                    }
                }else{
                    //跳转到登录页
                    startActivityKx(LoginActivity::class.java)
                }
            }

            R.id.web_liulanqi ->{
                //用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 收藏回调，不管成功或者失败都会进来
     */

    override fun collect(collected: Boolean) {
        collect = collected
        //刷新一下menu
        window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
        invalidateOptionsMenu()
        //通知app刷新相对应的ID的数据的收藏的值
        CollectEvent(collected,id).post()
    }


    /**
     * 收藏网址成功回调
     */
    override fun collectUrlSucc(collected: Boolean, data: CollectUrlResponse) {
        collect = collected
        //刷新一下menu
        window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
        invalidateOptionsMenu()
        id = data.id
        //通知app刷新相对应的Id的数据的收藏的值
        CollectEvent(collect, id).post()
    }

    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了， 当前界面的id与账户收藏集合id匹配的值需要设置已经收藏 并刷新menu
        if (event.login) {
            event.collectIds.forEach {
                if (it.toInt() == id) {
                    collect = true
                    window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                    invalidateOptionsMenu()
                    return@forEach
                }
            }
        }
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()

    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}