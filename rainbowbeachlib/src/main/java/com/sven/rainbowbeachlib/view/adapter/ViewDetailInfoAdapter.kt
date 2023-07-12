package com.sven.rainbowbeachlib.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.bean.ViewDetailInfoBean

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/10
 * @Version:        1.0
 */
class ViewDetailInfoAdapter(private var mData: MutableList<ViewDetailInfoBean>) :
    RecyclerView.Adapter<ViewDetailInfoAdapter.MyViewHolder>() {

    var mItemClickListener: ViewListInfoAdapter.OnViewInfoItemClickListener? = null
    var currentSelectItem: ViewDetailInfoBean? = null

    fun updateData(data: MutableList<ViewDetailInfoBean>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // 实例化展示的view
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.view_detail_item, parent, false)
        // 实例化viewholder
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 绑定数据
        val viewInfoBean = mData[position]
        holder.mTvName.text = viewInfoBean.name
        holder.mTvDetailInfo.text = viewInfoBean.desc
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemView: View
        var mTvDetailInfo: TextView
        var mTvName: TextView

        init {
            mItemView = itemView.findViewById(R.id.item_view)
            mTvDetailInfo = itemView.findViewById(R.id.tv_detail_info_str)
            mTvName = itemView.findViewById(R.id.tv_name)
        }
    }
}