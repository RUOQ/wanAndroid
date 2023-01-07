package com.ruoq.wanAndroid.mvp.ui.activity.main.me

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.*
import com.ruoq.wanAndroid.databinding.FragmentMeBinding
import com.ruoq.wanAndroid.di.component.me.DaggerMeComponent
import com.ruoq.wanAndroid.di.module.main.me.MeModule
import com.ruoq.wanAndroid.mvp.contract.main.me.MeContract
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import com.ruoq.wanAndroid.mvp.model.entity.IntegralResponse
import com.ruoq.wanAndroid.mvp.model.entity.UserInfoResponse
import com.ruoq.wanAndroid.mvp.ui.activity.collect.CollectActivity
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.activity.start.LoginActivity
import com.ruoq.wanAndroid.mvp.ui.activity.todo.TodoActivity
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import me.hegj.wandroid.mvp.presenter.main.me.MePresenter
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:06
 * @Description : 文件描述
 */
class MeFragment: BaseFragment<MePresenter>(),MeContract.View {
    private lateinit var userInfo :UserInfoResponse
    var integral :IntegralResponse ?= null
    private var _binding: FragmentMeBinding? = null
    private val binding get() = _binding!!
    companion object{
        fun newInstance():MeFragment{
            return MeFragment()
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMeComponent.builder()
            .appComponent(appComponent)
            .meModule(MeModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeBinding.inflate(inflater,container,false)
        clickListener()
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.toolBar.run{
            title = ""
            setBackgroundColor(SettingUtil.getColor(_mActivity))
        }
        binding.meSwipe.run{
            setOnRefreshListener {
                //刷新积分
                mPresenter?.getIntegral()
            }
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
        }

        binding.meLinear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        binding.meIntegral.setTextColor(SettingUtil.getColor(_mActivity))

        ArmsUtils.obtainAppComponentFromContext(_mActivity)
            .imageLoader()
            .loadImage(_mActivity.applicationContext,ImageConfigImpl
                .builder()
                .url("https://avatars2.githubusercontent.com/u/18655288?s=460&v=4")
                .imageView(binding.imageView)
                .errorPic(R.drawable.ic_account)
                .fallback(R.drawable.ic_account)
                .placeholder(R.drawable.ic_account)
                .isCrossFade(true)
                .isCircle(true)
                .build()
            )

    }


    @SuppressLint("SetTextI18n")
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if(CacheUtil.isLogin()){
            //如果已经登录了赋值，请求积分接口
            userInfo = CacheUtil.getUser()
            with(binding){
                meName.text = if(userInfo.nickname.isEmpty()) userInfo.username else userInfo.nickname
                meSwipe.isRefreshing = true
                mPresenter?.getIntegral()
            }
        }else{
            //没登录就不要去请求积分接口了
            with(binding){
                meName.text = "请先登录"
                meInfo.text = "id : -- 排名: --"
                meIntegral.text = "0"
            }
        }
    }
    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @SuppressLint("SetTextI18n")
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        if (event.login) {
            //接收到登录了，赋值 并去请求积分接口
            userInfo = CacheUtil.getUser()
            binding.meName.text = if(userInfo.nickname.isEmpty()) userInfo.username else userInfo.nickname
            //吊起请求 设置触发 下拉 swipe
            binding.meSwipe.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //接受到退出登录了，赶紧清空赋值
            binding.meName.text = "请先登录~"
            binding.meIntegral.text = "0"
            binding.meInfo.text = "id : --　排名 : --"
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        setUiTheme(_mActivity, listOf(binding.meSwipe,binding.toolBar,binding.meLinear,binding.meIntegral))
    }

    private fun clickListener(){
        with(binding){
            meLinear.setOnClickListener {
                if(!CacheUtil.isLogin()){
                    startActivityKx(LoginActivity::class.java)
                }
            }

            meCollect.setOnClickListener{
                startActivityKx(CollectActivity::class.java)
            }

            meTodo.setOnClickListener {
                startActivityKx(TodoActivity::class.java)
            }

            meIntegralLinear.setOnClickListener {
//                startActivityKx(IntegralActivity::class.java,true,Bundle().apply {
//                    integral?.let {
//                        putSerializable("integral",it)
//                    }
//                })
            }

            meArticle.setOnClickListener {
//                startActivityKx(ShareListActivity::class.java,true)
            }

            meAbout.setOnClickListener {
                val data = BannerResponse("",
                0,"",0,0,"玩安卓",0,"https://www.wanandroid.com/")
                startActivityKx(WebViewActivity::class.java,false,Bundle().apply {
                    putSerializable("bannerdata",data)
                })
            }

            meJoin.setOnClickListener {
//                joinQQGroup("")
            }

            meSetting.setOnClickListener {
//                startActivityKx(SettingActivity::class.java)
            }

        }
    }

    /**
     * 获取积分成功回调
     */
    @SuppressLint("SetTextI18n")
    override fun getIntegralSuccess(integral: IntegralResponse) {
        this.integral = integral
        binding.meSwipe.isRefreshing = false
        binding.meInfo.text = "id : ${integral.userId}　排名 : ${integral.rank}"
        binding.meIntegral.text = integral.coinCount.toString()
    }

    /**
     * 获取积分失败回调
     */
    override fun getIntegralFailed(errorMsg: String) {
        binding.meSwipe.isRefreshing = false
        ShowUtils.showToast(_mActivity, errorMsg)
    }

    /**
     * 加入qq聊天群
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            ShowUtils.showToast(_mActivity,"未安装手机QQ或安装的版本不支持")
            false
        }
    }
}