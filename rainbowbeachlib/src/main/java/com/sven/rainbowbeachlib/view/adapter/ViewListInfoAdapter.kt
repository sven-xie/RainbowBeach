package com.sven.rainbowbeachlib.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.bean.ViewInfoBean

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/10
 * @Version:        1.0
 */
class ViewListInfoAdapter(private var mData: MutableList<ViewInfoBean>) :
    RecyclerView.Adapter<ViewListInfoAdapter.MyViewHolder>() {

    var mItemClickListener: OnViewInfoItemClickListener? = null
    var currentSelectItem: ViewInfoBean? = null

    fun updateData(data: MutableList<ViewInfoBean>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // 实例化展示的view
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.view_info_rv_item, parent, false)
        // 实例化viewholder
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 绑定数据
        val viewInfoBean = mData[position]
        holder.mTvIndex.text = position.toString()
        holder.mTvIdStr.text = "ID：${viewInfoBean.id}"
        holder.mTvAttachParentName.text = "依附：${viewInfoBean.attachPageName}"

        holder.mItemView.setOnClickListener {
            currentSelectItem?.checkStatus = ViewInfoBean.CHECK_STATUS_UNCHECK
            viewInfoBean.checkStatus = ViewInfoBean.CHECK_STATUS_CHECKED
            currentSelectItem = viewInfoBean
            mItemClickListener?.onItemClick(viewInfoBean)
        }

        holder.mTvDetailInfo.visibility = View.GONE
        holder.mTvDetailInfo.text = ""
        holder.mTvQueryDetail.text = "点击查看详情"

        holder.mTvQueryDetail.setOnClickListener {
            mItemClickListener?.onItemDetailClick(viewInfoBean)
//            if (holder.mTvDetailInfo.visibility == View.VISIBLE) {
//                holder.mTvDetailInfo.visibility = View.GONE
//                holder.mTvDetailInfo.text = ""
//                holder.mTvQueryDetail.text = "点击查看详情"
//            } else {
//                val curView = viewInfoBean.view
//                curView?.let { view ->
//                    holder.mTvDetailInfo.visibility = View.VISIBLE
//                    holder.mTvDetailInfo.text = "父控件：${view.parent} ；  背景色：${view.background}"
//                    holder.mTvQueryDetail.text = "关闭详情"
//                }
//            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemView: View
        var mTvDetailInfo: TextView
        var mTvQueryDetail: TextView
        var mTvIdStr: TextView
        var mTvIndex: TextView
        var mTvAttachParentName: TextView

        init {
            mItemView = itemView.findViewById(R.id.item_view)
            mTvDetailInfo = itemView.findViewById(R.id.tv_detail_info_str)
            mTvQueryDetail = itemView.findViewById(R.id.tv_query_detail)
            mTvIndex = itemView.findViewById(R.id.tv_index)
            mTvIdStr = itemView.findViewById(R.id.tv_view_id_str)
            mTvAttachParentName = itemView.findViewById(R.id.tv_view_attach_parent_name)
        }
    }

    interface OnViewInfoItemClickListener {
        fun onItemClick(viewInfoBean: ViewInfoBean)
        fun onItemDetailClick(viewInfoBean: ViewInfoBean)
    }
}