package com.ruoq.wanAndroid.mvp.ui.activity.setting

import android.os.Bundle
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.databinding.ActivitySettingBinding
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/10 13:46
 * @Description : 文件描述
 */
class SettingActivity:BaseActivity<IPresenter>() {
    private var _binding:ActivitySettingBinding ?= null
    private val binding get() = _binding!!

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title = "设置"
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    finish()
                }
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.setting_frame,GeneralPreferenceFragment())
                .commit()
        }
    }

    @Subscribe
    fun settingEvent(event:SettingChangeEvent){
        initStatusBar()
    }
}