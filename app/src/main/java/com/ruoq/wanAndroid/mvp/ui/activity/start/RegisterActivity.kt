package com.ruoq.wanAndroid.mvp.ui.activity.start

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.jess.arms.di.component.AppComponent
import com.jess.arms.integration.AppManager
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.utils.CacheUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.databinding.ActivityLoginBinding
import com.ruoq.wanAndroid.databinding.ActivityRegisterBinding
import com.ruoq.wanAndroid.di.component.start.DaggerLoginComponent
import com.ruoq.wanAndroid.mvp.contract.start.LoginContract
import com.ruoq.wanAndroid.mvp.model.entity.UserInfoResponse
import com.ruoq.wanAndroid.mvp.model.start.LoginModule
import com.ruoq.wanAndroid.mvp.presenter.start.LoginPresenter
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/4 20:48
 * @Description : 注册页面
 */
class RegisterActivity: BaseActivity<LoginPresenter> (),LoginContract.View{
    private lateinit var binding:ActivityRegisterBinding
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent
            .builder()
            .appComponent(appComponent)
            .loginModule(LoginModule(this))
            .build()
            .inject1(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title="注册"
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    finish()
                }
            }

            SettingUtil.setShapeColor(registerSub,SettingUtil.getColor(this@RegisterActivity))
            registerUsername.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null) {
                        if (p0.isNotEmpty()) {
                            registerClear.visibility = View.VISIBLE
                        } else {
                            registerClear.visibility = View.GONE
                        }
                    }
                }

            })

            registerPwd.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable) {
                    if (p0.isNotEmpty()) {
                        registerKey.visibility = View.VISIBLE
                    } else {
                        registerKey.visibility = View.GONE
                    }
                }
            })

            registerPwd1.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable) {
                    if (p0.isNotEmpty()) {
                        registerKey1.visibility = View.VISIBLE
                    } else {
                        registerKey1.visibility = View.GONE
                    }
                }
            })

            registerKey.setOnCheckedChangeListener{ _,isChecked ->
                if(isChecked){
                    registerPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }else{
                    registerPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                registerPwd.setSelection(registerPwd.text.toString().length)
            }

            registerKey1.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    registerPwd1.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }else{
                    registerPwd1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                registerPwd1.setSelection(registerPwd1.text.toString().length)
            }
        }
    }

    private fun click(){
        with(binding){
            registerClear.setOnClickListener {
                registerUsername.setText("")
            }

            registerSub.setOnClickListener {
                if(registerUsername.text.isEmpty()){
                    showMessage("请填写账号")
                }
                if(registerUsername.text.length< 6){
                    showMessage("账号长度不能小于6位")
                }
                if(registerPwd.text.isEmpty()){
                    showMessage("请填写密码")
                }
                if(registerPwd.text.length < 6){
                    showMessage("密码长度不能小于6位")
                }
                if(registerPwd1.text.isEmpty()){
                    showMessage("请填写确认密码")
                }
                if(registerPwd1.text.toString() != registerPwd.text.toString()){
                    showMessage("密码不一致")
                }
                mPresenter?.register(registerUsername.text.toString(),registerPwd.text.toString(),registerPwd1.text.toString())
            }
        }
    }

    override fun onSuccess(userinfo: UserInfoResponse) {
        CacheUtil.setUser(userinfo) //保存账号信息
        AppManager.getAppManager().killActivity(LoginActivity::class.java)
        LoginFreshEvent(true,userinfo.collectIds).post()
        finish()
    }
}