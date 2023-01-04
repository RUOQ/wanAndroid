package com.ruoq.wanAndroid.mvp.ui.activity.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.AddEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.ActivityTodoBinding
import com.ruoq.wanAndroid.di.component.todo.DaggerTodoComponent
import com.ruoq.wanAndroid.di.module.todo.TodoModule
import com.ruoq.wanAndroid.mvp.contract.todo.TodoContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.TodoResponse
import com.ruoq.wanAndroid.mvp.presenter.main.todo.TodoPresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.TodoAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 13:54
 * @Description : 任务清单
 */
class TodoActivity: BaseActivity<TodoPresenter>(),TodoContract.View {
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter:TodoAdapter
    private var initPageNo = 1
    private var pageNo:Int = initPageNo //当前页码
    private var footView:DefineLoadMoreView ?= null
    private var _binding:ActivityTodoBinding ?= null
    private val binding get() = _binding!!


    override fun setupActivityComponent(appComponent: AppComponent) {
        //如果找不到该类，请到Component包下，创建相应的Component类，并重新编译
        DaggerTodoComponent.builder()
            .appComponent(appComponent)
            .todoModule(TodoModule(this))
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
         return 0
    }

    @SuppressLint("CheckResult")
    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title = "待办清单"
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    finish()
                }
            }

            //绑定loadSir
            loadSir = LoadSir.getDefault().register(swipeRefreshLayout){
                //界面加载错误，或者没有数据时，点击重试的监听
                loadSir.showCallback(LoadingCallBack::class.java)
                pageNo = initPageNo
                mPresenter?.getTodoData(pageNo)
            }.apply {
                SettingUtil.setLoadingColor(this@TodoActivity,this)
                showCallback(LoadingCallBack::class.java)
            }

            //初始化Adapter
            adapter = TodoAdapter(arrayListOf()).apply {
                if(SettingUtil.getListMode(this@TodoActivity) != 0){
                    openLoadAnimation(SettingUtil.getListMode(this@TodoActivity))
                }else{
                    closeLoadAnimation()
                }

                setOnItemClickListener { adapter, view, position ->
                    launchActivity(Intent(this@TodoActivity,AddTodoActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putSerializable("data",this@TodoActivity.adapter.data[position])
                        })
                    })
                }

                setOnItemChildClickListener{ _, _, position ->
                    MaterialDialog(this@TodoActivity).show{
                        listItems(items = adapter.data[position].isDone().let {
                            if(it){
                                listOf("编辑","删除")
                            }else{
                                listOf("编辑","删除","完成")
                            }
                        }){ _, index, _ ->
                            if(index == 0){
                                launchActivity(Intent(this@TodoActivity,AddTodoActivity::class.java).apply {
                                    putExtras(Bundle().apply {
                                        putSerializable("data",this@TodoActivity.adapter.data[position])
                                    })
                                })
                            }else if(index == 1){
                                mPresenter?.delTodo(adapter.data[position].id,position)
                            }else if(index == 2){
                                mPresenter?.updateTodo(adapter.data[position].id,position)
                            }
                        }
                    }

                }
            }

            //
            floatbtn.run{
                backgroundTintList = SettingUtil.getOneColorStateList(this@TodoActivity)
                setOnClickListener {
                    val layoutManger = swiperecyclerview.layoutManager as LinearLayoutManager
                    //如果当前RecyclerView最后一个视图索引大于40，直接返回顶部
                    if(layoutManger.findLastVisibleItemPosition() >= 40){
                        swiperecyclerview.scrollToPosition(0)
                    }else{
                        swiperecyclerview.smoothScrollToPosition(0) //缓慢到达顶部，有点慢
                    }
                }
            }
            //初始化Recycleview
            swipeRefreshLayout.run{
                setColorSchemeColors(SettingUtil.getColor(this@TodoActivity))
                setOnRefreshListener {
                    //刷新
                    pageNo = initPageNo
                    mPresenter?.getTodoData(pageNo)
                }
            }

            //初始化Recyclerview
            footView = RecyclerViewUtils().initRecyclerView(this@TodoActivity,
                swiperecyclerview,
                SwipeRecyclerView.LoadMoreListener {
                //加载更多
                mPresenter?.getTodoData(pageNo)
            })
            footView!!.setLoadViewColor(SettingUtil.getOneColorStateList(this@TodoActivity))

            //监听Recyclerview滑动到顶部的时候，需要把向上返回顶部按钮隐藏
            swiperecyclerview.run{
                adapter = this@TodoActivity.adapter
                addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(!canScrollVertically(-1)){
                            floatbtn.visibility = View.INVISIBLE
                        }
                    }
                })
            }

            //发起请求
            mPresenter?.getTodoData(pageNo)
        }

    }

    @Subscribe
    fun todoChange(event:AddEvent){
        //刷新
        if(event.code == AddEvent.TODO_CODE){
            binding.swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getTodoData(pageNo)
        }
    }

    override fun requestDataSuccess(ariticles: ApiPagerResponse<MutableList<TodoResponse>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo && ariticles.datas.size == 0){
            //如果是第一页并没有数据，页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话，floatbutton就要隐藏了，因为这时候肯定是要在顶部的
            binding.floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(ariticles.datas)
        } else {
            //不是第一页
            loadSir.showSuccess()
            adapter.addData(ariticles.datas)
        }
        pageNo++
        if (ariticles.pageCount >= pageNo) {
            //如果总条数大于当前页数时 还有更多数据
            binding.swiperecyclerview.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            binding.swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                binding.swiperecyclerview.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新，界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误，设置recyclerview加载错误，
            binding.swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }

    override fun updateTodoDataSuccess(position: Int) {
       //完成待办清单
        adapter.data[position].status = 1
        adapter.notifyItemChanged(position)
    }

    override fun deleteTodoDataSuccess(position: Int) {
        //删除待办事项
        adapter.remove(position)
        if(adapter.data.size == 0){
            pageNo = initPageNo
            mPresenter?.getTodoData(pageNo)
        }
    }

    override fun updateTodoDataFailed(errorMsg: String) {
        showMessage(errorMsg)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.todo_add -> {
                launchActivity(Intent(this, AddTodoActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}