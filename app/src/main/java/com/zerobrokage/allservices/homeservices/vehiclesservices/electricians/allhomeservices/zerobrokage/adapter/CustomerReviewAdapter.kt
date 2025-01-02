package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CustomerReviewData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ReviewCustomerItemcardBinding

class CustomerReviewAdapter(private val customerArrayList: List<CustomerReviewData>) :
    RecyclerView.Adapter<CustomerReviewAdapter.CustomerViewHolder>() {

    class CustomerViewHolder(private val binding: ReviewCustomerItemcardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CustomerReviewData) {
            binding.tvCustomerName.text = data.name
            binding.tvCustomerReview.text = data.description
            binding.tvCustomerPost.text = data.profession
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ReviewCustomerItemcardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentItem = customerArrayList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return customerArrayList.size
    }
}
