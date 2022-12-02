package com.ruoq.wanAndroid.app.weight

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 18:38
 * @Description : 这是这个类的主角，如何自定义LoadMoreView。
 */
class DefineLoadMoreView(context: Context): LinearLayout(context),
SwipeRecyclerView.LoadMoreView, View.OnClickListener{
    public val mProgressBar:ProgressBar
    private val mTvMessage: TextView
    private var mLoadMoreListener:SwipeRecyclerView.LoadMoreListener ?= null

    init{
        layoutParams = ViewGroup.LayoutParams(-1,-2)
        gravity = Gravity.CENTER
        visibility = View.GONE

        val displayMetrics = resources.displayMetrics

        val minHeight = (displayMetrics.density * 60  +  0.5).toInt()
        minimumHeight = minHeight

        View.inflate(context, R.layout.layout_footer_loadmore,this)
        mProgressBar = findViewById(R.id.loading_view)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mProgressBar.indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
            mProgressBar.indeterminateTintList = SettingUtil.getOneColorStateList(context)

        }
        mTvMessage = findViewById(R.id.tv_message)
        setOnClickListener(this)
    }

    fun setLoadMoreListener(mLoadMoreListener:SwipeRecyclerView.LoadMoreListener){
        this.mLoadMoreListener = mLoadMoreListener
    }

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    override fun onLoading() {
        visibility = View.VISIBLE
        mProgressBar.visibility = View.VISIBLE
        with(mTvMessage){
            visibility = View.VISIBLE
            text = "正在努力加载，请稍后..."
        }
    }

    /**
     * 加载完成
     * @param dataEmpty 是否请求到空数据
     * @param hasMore  是否还有更多的数据等待请求
     */
    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        if(!hasMore){
            visibility = View.VISIBLE
            if(dataEmpty){
                mProgressBar.visibility = View.GONE
                with(mTvMessage){
                    visibility = View.VISIBLE
                    text = "暂时没有数据"
                }
            }else{
                mProgressBar.visibility = View.GONE
                with(mTvMessage){
                    visibility = View.VISIBLE
                    text = "没有更多数据了"
                }
            }
        }else{
            visibility = View.INVISIBLE
        }
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，
     * 这个方法会被调用，并传入加载更多的listener。
     */
    override fun onWaitToLoadMore(loadMoreListener: SwipeRecyclerView.LoadMoreListener?) {
        this.mLoadMoreListener = loadMoreListener
        visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "点我加载更多"
    }

    /**
     * 加载出错误了，下面的错误吗和错误信息二选一
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    @SuppressLint("LogNotTimber")
    override fun onLoadError(errorCode: Int, errorMessage: String?) {
        visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = errorMessage
        Log.i("ruoq","加载失败了")
    }

    /**
     * 非自动加载更多时，mLoadMoreListener才不为空
     */
    override fun onClick(v: View) {
        mLoadMoreListener?.let {
            if(mTvMessage.text != "没有更多数据啦" && mProgressBar.visibility != View.VISIBLE){
                it.onLoadMore()
            }
        }
    }

    fun setLoadViewColor(colorStateList: ColorStateList){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mProgressBar.indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
            mProgressBar.indeterminateTintList = colorStateList
        }
    }
}