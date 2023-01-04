package com.ruoq.wanAndroid.mvp.ui.activity.todo

import android.os.Bundle
import android.text.TextUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker

import com.jess.arms.di.component.AppComponent
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.AddEvent
import com.ruoq.wanAndroid.app.event.AddEvent.Companion.TODO_CODE
import com.ruoq.wanAndroid.app.utils.DatetimeUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.PriorityDialog
import com.ruoq.wanAndroid.databinding.ActivityAddTodoBinding
import com.ruoq.wanAndroid.di.component.todo.DaggerAddTodoComponent
import com.ruoq.wanAndroid.di.module.todo.AddTodoModule
import com.ruoq.wanAndroid.mvp.contract.todo.AddTodoContract
import com.ruoq.wanAndroid.mvp.model.entity.TodoResponse
import com.ruoq.wanAndroid.mvp.model.entity.enums.TodoType
import com.ruoq.wanAndroid.mvp.presenter.main.todo.AddTodoPresenter
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import java.util.*

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 13:54
 * @Description : 文件描述
 */
class AddTodoActivity : BaseActivity<AddTodoPresenter>(), AddTodoContract.View{
    var todoResponse: TodoResponse ?= null
    private var _binding:ActivityAddTodoBinding ?= null
    private val binding get() = _binding!!
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerAddTodoComponent
            .builder()
            .appComponent(appComponent)
            .addTodoModule(AddTodoModule(this))
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.run{
            todoResponse = getSerializableExtra("data") as TodoResponse?
        }
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title = "添加待办清单"
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    finish()
                }
            }

            if(todoResponse == null){
                addTodoColorview.setView(TodoType.TodoType1.color)
                addTodoProx.text = TodoType.TodoType1.content
            }else{
                toolBar.title = "编辑待办清单"
                todoResponse?.let {
                    addTodoTitle.setText(it.title)
                    addTodoContent.setText(it.content)
                    addTodoDate.text = it.dateStr
                    addTodoColorview.setView(TodoType.byType(it.priority).color)
                    addTodoProx.text = TodoType.byType(it.priority).content
                }
            }
            SettingUtil.setShapeColor(addTodoSubmit,SettingUtil.getColor(this@AddTodoActivity))
        }

        click()
    }

    override fun addTodoSucc() {
        AddEvent(TODO_CODE).post()
        finish()
    }

    override fun addTodoFaild(errorMsg: String) {
        showMessage(errorMsg)
    }


    private fun click(){
        with(binding){
            addTodoDate.setOnClickListener {
                MaterialDialog(this@AddTodoActivity).show{
                    cornerRadius(0f)
                    datePicker(minDate = Calendar.getInstance()){dialog,date ->
                        addTodoDate.text = DatetimeUtil.formatDate(date.time,DatetimeUtil.DATE_PATTERN)
                    }
                }
            }

            addTodoProxlinear.setOnClickListener {
                PriorityDialog(this@AddTodoActivity,
                    TodoType.byValue(addTodoProx.text.toString()).type).apply {
                        setPriorityInterface(object:PriorityDialog.PriorityInterface{
                            override fun onSelect(type: TodoType) {
                                addTodoProx.text = type.content
                                addTodoColorview.setView(type.color)
                            }
                        })
                }.show()
            }


            addTodoSubmit.setOnClickListener {
                if(TextUtils.isEmpty(addTodoTitle.text.toString())){
                    showMessage("请填写标题")
                }else if(TextUtils.isEmpty(addTodoContent.text.toString())){
                    showMessage("请填写内容")
                }else if(TextUtils.isEmpty(addTodoDate.text.toString())){
                    showMessage("请选择预计完成时间")
                }else {
                    if(todoResponse==null){
                        MaterialDialog(this@AddTodoActivity).show {
                            title(R.string.title)
                            message(text = "确定要添加吗？")
                            positiveButton(text = "添加") {
                                mPresenter?.addTodo(addTodoTitle.text.toString(),
                                    addTodoContent.text.toString(),
                                    addTodoDate.text.toString(),
                                    TodoType.byValue(addTodoProx.text.toString()).type
                                )
                            }
                            negativeButton(R.string.cancel)
                        }
                    }else{
                        MaterialDialog(this@AddTodoActivity).show {
                            title(R.string.title)
                            message(text = "确定要提交编辑吗？")
                            positiveButton(text = "提交") {
                                mPresenter?.updateTodo(addTodoTitle.text.toString(),
                                    addTodoContent.text.toString(),
                                    addTodoDate.text.toString(),
                                    TodoType.byValue(addTodoProx.text.toString()).type,
                                    todoResponse!!.id
                                )
                            }
                            negativeButton(R.string.cancel)
                        }
                    }

                }
            }
            }
        }

    override fun showMessage(message: String) {
        super.showMessage(message)
    }

}