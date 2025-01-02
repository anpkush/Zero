package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.FAQAdapter
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityFaqsBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.FAQsData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.tvTitle.text = "FAQs"



        faqsgetApi()

    }

    private fun faqsgetApi() {
        RetrofitInstance.apiService.getFaqs().enqueue(object : Callback<FAQsData?> {
            override fun onResponse(call: Call<FAQsData?>, response: Response<FAQsData?>) {
                if (!this@FAQsActivity.isDestroyed) {
                    val faqs = response.body()?.faqs_data ?: emptyList()

                    val myAdapter = FAQAdapter(faqs)
                    binding.rvFAQsRecyclerView.apply {
                        adapter = myAdapter
                        layoutManager = LinearLayoutManager(this@FAQsActivity)
                    }
                }
            }

            override fun onFailure(call: Call<FAQsData?>, t: Throwable) {
                Toast.makeText(this@FAQsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}