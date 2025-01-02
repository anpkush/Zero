package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FaqCardViewBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.FAQsData

class FAQAdapter(private val faqsArrayList: List<FAQsData.FaqsData>) :
    RecyclerView.Adapter<FAQAdapter.MyFAQQViewHolder>() {

    class MyFAQQViewHolder(private val binding: FaqCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FAQsData.FaqsData, isExpanded: Boolean) {
            binding.tvFAQsQuestion.text = data.question
            binding.tvFAQsAnswer.text = data.answer

            binding.tvFAQsAnswer.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
    }

    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFAQQViewHolder {
        val binding = FaqCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFAQQViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return faqsArrayList.size
    }

    override fun onBindViewHolder(holder: MyFAQQViewHolder, position: Int) {
        val currentItem = faqsArrayList[position]

        val isExpanded = position == expandedPosition

        holder.bind(currentItem, isExpanded)

        holder.itemView.setOnClickListener {
            val previousExpandedPosition = expandedPosition
            if (previousExpandedPosition != -1) {
                notifyItemChanged(previousExpandedPosition)
            }
            expandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(position)
        }
    }
}
