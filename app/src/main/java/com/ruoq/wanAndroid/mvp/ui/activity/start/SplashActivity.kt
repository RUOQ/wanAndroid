package com.ruoq.wanAndroid.mvp.ui.activity.start

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<IPresenter>() {
    private lateinit var binding:ActivitySplashBinding
    private var alphaAnimation:AlphaAnimation ?= null

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
       return 0
    }

    override fun initData(savedInstanceState: Bundle?) {

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //防止出现按Home键回到桌面是，再次点击重新进入Splash界面的bug
        if(intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0){
            finish()
            return
        }
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title = ""
            }
            welcomeBase.setBackgroundColor(SettingUtil.getColor(this@SplashActivity))
            //做一个1s的透明度动画，没什么用，后面透明度被我改成一样了，直接当作计时器
            alphaAnimation = AlphaAnimation(1.0f,1.0f)
            alphaAnimation?.run{
                duration = 1000
                setAnimationListener(object: Animation.AnimationListener{
                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        gotoMainActivity()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                })

                welcomeBase.startAnimation(alphaAnimation)
            }
        }
    }

    private fun gotoMainActivity(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
        //带点渐变动画
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this)))
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.statusBarColor = SettingUtil.getColor(this)
        }
        binding.toolBar.setBackgroundColor(SettingUtil.getColor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        alphaAnimation?.cancel()
        alphaAnimation =  null
    }
}