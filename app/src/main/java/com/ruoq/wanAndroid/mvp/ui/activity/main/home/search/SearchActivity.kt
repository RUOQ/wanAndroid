package com.ruoq.wanAndroid.mvp.ui.activity.main.home.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.CacheUtil
import com.ruoq.wanAndroid.app.utils.ColorUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.ShowUtils
import com.ruoq.wanAndroid.databinding.ActivitySearchBinding
import com.ruoq.wanAndroid.di.component.home.search.DaggerSearchComponent
import com.ruoq.wanAndroid.di.module.main.home.search.SearchModule
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchContract
import com.ruoq.wanAndroid.mvp.model.entity.SearchResponse
import com.ruoq.wanAndroid.mvp.presenter.main.home.search.SearchPresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.SearchHistoryAdapter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import okhttp3.Cache

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:05
 * @Description : 搜索Activity
 */
class SearchActivity:BaseActivity<SearchPresenter>(),SearchContract.View {
    val mTagData = mutableListOf<SearchResponse>()  //搜索热词数据
    val historyData = mutableListOf<String>()   //搜索历史数据
    private var _binding: ActivitySearchBinding?= null
    private val binding get() = _binding!!

    lateinit var adapter :SearchHistoryAdapter //搜索历史适配器
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchComponent.builder()
            .appComponent(appComponent)
            .searchModule(SearchModule(this))
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    //返回的时候收起输入法
                    ShowUtils.hideSoftKeyboard(this@SearchActivity)
                    finish()
                }
            }
            searchText1.setTextColor(SettingUtil.getColor(this@SearchActivity))
            searchText2.setTextColor(SettingUtil.getColor(this@SearchActivity))
            searchClear.setOnClickListener {
                MaterialDialog(this@SearchActivity).show{
                    title(text = "温馨提示")
                    message(text="确定清空搜索历史吗？")
                    positiveButton(text="清空"){
                        historyData.clear()
                        adapter.setNewData(historyData)
                        CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
                    }
                    negativeButton(R.string.cancel)
                }
            }

            searchFlowlayout.run{
                setOnTagClickListener{view, position,parent ->
                    val name = mTagData[position].name
                    if(historyData.contains(name)){
                        //当搜索历史中包含该数据时，则删除添加
                        historyData.remove(name)
                    }else if(historyData.size >= 10){
                        historyData.removeAt(historyData.size -1)
                    }
                    historyData.add(0,name)
                    this@SearchActivity.adapter.setNewData(historyData)
                    CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
                    launchActivity(Intent(this@SearchActivity,SearchResultActivity::class.java).apply{
                        putExtra("searchKey",name)
                    })
                    false
                }
            }

            historyData.clear()
            CacheUtil.getSearchHistoryData().forEach {
                historyData.add(it)
            }
            adapter =  SearchHistoryAdapter(historyData).apply {
                //设置空布局
                emptyView = LayoutInflater.from(this@SearchActivity)
                    .inflate(R.layout.search_empty_view,null)
                //删除单个搜索历史
                setOnItemChildClickListener{adapter,view,position->
                    adapter.remove(position)
                    CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
                }
                //点击了搜素历史中的一个
                setOnItemClickListener{adapter,view ,position ->
                    launchActivity(Intent(this@SearchActivity,SearchResultActivity::class.java).apply {
                        putExtra("searchKey",historyData[position])
                    })

                }
            }

            searchRecyclerview.run{
                layoutManager = LinearLayoutManager(this@SearchActivity)
                setHasFixedSize(true)
                adapter = this@SearchActivity.adapter
                isNestedScrollingEnabled = false
            }

            mPresenter?.getHotData()
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run{
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            queryHint = "输入关键字搜素"
            setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                //searchView的监听
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //当点击搜素时，输入法的搜索，和右边的搜索都会触发
                    query?.let{
                        if(historyData.contains(it)){
                            //当搜索历史包含该数据时，删除
                            historyData.remove(it)
                        }else if(historyData.size >= 10){
                            //如果集合的size有10个以上了，删除最后一个
                            historyData.removeAt(historyData.size - 1)
                        }
                        launchActivity(Intent(this@SearchActivity,SearchResultActivity::class.java).apply {
                            putExtra("searchKey",it)
                        })
                        historyData.add(0,it) //添加数据到第一条
                        this@SearchActivity.adapter.setNewData(historyData) //刷新适配器
                        CacheUtil.setSearchHistoryData(Gson().toJson(historyData)) //保存到本地
                    }
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })

            isSubmitButtonEnabled = true  //右边是否展示搜索图标
            val field = javaClass.getDeclaredField("mGoButton")
            field.run{
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun requestSearchSuccess(tagData: MutableList<SearchResponse>) {
        mTagData.addAll(tagData)
        binding.searchFlowlayout.adapter = object:TagAdapter<SearchResponse>(mTagData){
            override fun getView(parent: FlowLayout?, position: Int, t: SearchResponse?): View {
                return LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout,binding.searchFlowlayout,false).apply {
                     val flowTag = this.findViewById<TextView>(R.id.flow_tag).apply {
                         text = Html.fromHtml(t?.name)
                         setTextColor(ColorUtil.randomColor())
                     }
                }
            }
        }
    }

    override fun showMessage(message: String) {

    }


}