package com.ruoq.wanAndroid.mvp.ui.activity.start

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.utils.CacheUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.ShowUtils
import com.ruoq.wanAndroid.databinding.ActivityLoginBinding
import com.ruoq.wanAndroid.di.component.start.DaggerLoginComponent
import com.ruoq.wanAndroid.mvp.contract.start.LoginContract
import com.ruoq.wanAndroid.mvp.model.entity.UserInfoResponse
import com.ruoq.wanAndroid.mvp.model.start.LoginModule
import com.ruoq.wanAndroid.mvp.presenter.start.LoginPresenter

class LoginActivity : BaseActivity<LoginPresenter>(),LoginContract.View {
    private lateinit var binding:ActivityLoginBinding

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .loginModule(LoginModule(this))
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        click()
    }

    private fun click() {
        with(binding){
            //清除按钮
            loginClear.setOnClickListener {
                loginUsername.setText("")
            }
            //登录页面
            loginSub.setOnClickListener {
                if(loginUsername.text.isEmpty()){
                    showMessage("请填写账号")
                    return@setOnClickListener
                }
                if(loginPwd.text.isEmpty()){
                    showMessage("请填写密码")
                    return@setOnClickListener
                }
                mPresenter?.login(loginUsername.text.toString(),loginPwd.text.toString())

            }

            //去注册
            loginGoregister.setOnClickListener {
                startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
            }

        }
    }

    override fun initView(savedInstanceState: Bundle?): Int {

        return 0 //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        with(binding){
            binding.toolBar.run {
                setSupportActionBar(this)
                title = "登录"
                setNavigationIcon(R.drawable.ic_close)
                setNavigationOnClickListener { finish() }
            }
            SettingUtil.setShapeColor(loginSub, SettingUtil.getColor(this@LoginActivity))
            loginGoregister.setTextColor(SettingUtil.getColor(this@LoginActivity))
            loginUsername.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null) {
                        if (p0.isNotEmpty()) {
                            loginClear.visibility = View.VISIBLE
                        } else {
                            loginClear.visibility = View.GONE
                        }
                    }
                }

            })

            loginPwd.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null) {
                        if (p0.isNotEmpty()) {
                            loginKey.visibility = View.VISIBLE
                        } else {
                            loginKey.visibility = View.GONE
                        }
                    }
                }

            })


            loginKey.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    loginPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    loginPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                loginPwd.setSelection(loginPwd.text.toString().length)
            }
        }

    }

    override fun onSuccess(userinfo: UserInfoResponse) {
         CacheUtil.setUser(userinfo)//保存账户信息
        //保存账户与密码，在其他接口请求的时候当做Cookie传到Header中
        LoginFreshEvent(true, userinfo.collectIds).post()//通知其他界面登录成功了，有收藏的地方需要刷新一下数据
        finish()
    }

    override fun showMessage(message: String) {
         ShowUtils.showToastCenter(this@LoginActivity,message)
    }

}