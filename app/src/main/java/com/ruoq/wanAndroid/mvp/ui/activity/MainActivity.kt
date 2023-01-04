package com.ruoq.wanAndroid.mvp.ui.activity

import android.os.Bundle
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.BuildConfig
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.ShowUtils
import com.ruoq.wanAndroid.databinding.ActivityMainBinding
import com.ruoq.wanAndroid.mvp.ui.activity.main.MainFragment
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseActivity<IPresenter>() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
       return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        if(findFragment(MainFragment::class.java) == null){
            loadRootFragment(R.id.main_frameLayout,MainFragment.newInstance())
        }
//        //进入首页检查更新,bugly
//        Beta.checkAppUpgrade(false,true)

        //如果导入bugly,请替换key
        if(BuildConfig.APPLICATION_ID != "com.ruoq.wanAndroid" && BuildConfig.BUGLY_KEY == "5a5f6366fc"){
            showMessage("请更换Bugly Key！防止产生的错误信息反馈到作者账号上，具体请查看app模块中的 build.gradle文件，修改BUGLY_KEY字段值为自己在Bugly官网申请的Key")
        }
    }

    override fun onCreateFragmentAnimator():FragmentAnimator{
        //设置横向，和（安卓4.x)的动画相同
        return DefaultHorizontalAnimator()
    }


    /**
     * 启动一个其他的Fragment
     *
     */
    fun startBrotherFragment(targetFragment:SupportFragment){
        start(targetFragment)
    }

    @Subscribe
    fun settingEvent(event:SettingChangeEvent){
        initStatusBar()
    }

    var exitTime:Long  = 0

    override fun onBackPressedSupport() {
        if(System.currentTimeMillis() - this.exitTime > 2000L){
            ShowUtils.showToast(this,"再按一次退出程序")
            this.exitTime = System.currentTimeMillis()
            return
        }else{
            super.onBackPressed()
        }
    }

}