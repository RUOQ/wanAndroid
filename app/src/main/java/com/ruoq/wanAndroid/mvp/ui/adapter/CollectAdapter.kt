package com.ruoq.wanAndroid.mvp.ui.adapter

import android.text.Html
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.mvp.model.entity.CollectResponse

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/8 19:22
 * @Description : 文件描述
 */
class CollectAdapter(data:ArrayList<CollectResponse>?):BaseQuickAdapter
<CollectResponse,BaseViewHolder>(data){
    private var mOnCollectViewClickListener:OnCollectViewClickListener ?= null
    private val mArticle = 1 //文章类型
    private val mProject = 2 //项目类型，本来打算不区分文章和项目不仅统一使用布局的，但是布局完之后发现差异化蛮大的


    init{
        multiTypeDelegate = object:MultiTypeDelegate<CollectResponse>(){
            override fun getItemType(entity: CollectResponse?): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                    return if (TextUtils.isEmpty(entity?.envelopePic)) mArticle else mProject
            }
        }

        //注册多布局
        multiTypeDelegate
            .registerItemType(mArticle, R.layout.item_article)
            .registerItemType(mProject, R.layout.item_project)
    }

    override fun convert(helper: BaseViewHolder, item: CollectResponse?) {
        if (item != null) {
            when (helper.itemViewType) {
                mArticle -> {
                    //文章布局的赋值
                    item.run{
                        helper.setText(R.id.item_home_author, if(author.isEmpty()) "匿名用户" else author)
                        helper.setText(R.id.item_home_content, Html.fromHtml(title))
                        helper.setText(R.id.item_home_type2, Html.fromHtml(chapterName))
                        helper.setText(R.id.item_home_date, niceDate)
                        helper.getView<CollectView>(R.id.item_home_collect).isChecked = true
                        //隐藏所有标签
                        helper.setGone(R.id.item_home_top, false)
                        helper.setGone(R.id.item_home_type1, false)
                        helper.setGone(R.id.item_home_new, false)
                    }
                    helper.getView<CollectView>(R.id.item_home_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }
                    })
                }
                mProject -> {
                    //项目布局的赋值
                    item.run{
                        helper.setText(R.id.item_project_author, if(author.isEmpty()) "匿名用户" else author)
                        helper.setText(R.id.item_project_title, Html.fromHtml(title))
                        helper.setText(R.id.item_project_content, Html.fromHtml(desc))
                        helper.setText(R.id.item_project_type, Html.fromHtml(chapterName))
                        helper.setText(R.id.item_project_date, niceDate)
                        //隐藏所有标签
                        helper.setGone(R.id.item_project_top, false)
                        helper.setGone(R.id.item_project_type1, false)
                        helper.setGone(R.id.item_project_new, false)
                        helper.getView<CollectView>(R.id.item_project_collect).isChecked = true
                        ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader().loadImage(mContext.applicationContext,
                            ImageConfigImpl
                                .builder()
                                .url(envelopePic)
                                .imageView(helper.getView(R.id.item_project_imageview))
                                .isCrossFade(true)
                                .imageRadius(8)
                                .build()
                        )
                    }
                    helper.getView<CollectView>(R.id.item_project_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }
                    })
                }
            }
        }
    }



    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }


    interface OnCollectViewClickListener {
        fun onClick(helper: BaseViewHolder, v: CollectView, position: Int)
    }
}