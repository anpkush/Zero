package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FragmentViewCartBottomBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.CartActivity


class ViewCartBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentViewCartBottomBinding? = null
    private val binding get() = _binding
    private var cartItems: Map<Int, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            cartItems = it.getSerializable("cartItems") as? Map<Int, Int>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCartBottomBinding.inflate(inflater, container, false)

        // Count the number of unique items in the cart
        val uniqueItemsCount = cartItems?.keys?.size ?: 0
        binding?.tvCartItemCount?.text = "$uniqueItemsCount"

        binding?.btViewCart?.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            startActivity(intent)
        }

        return _binding?.root
    }

    companion object {
        fun newInstance(cartItems: Map<Int, Int>): ViewCartBottomFragment {
            val fragment = ViewCartBottomFragment()
            val args = Bundle()
            args.putSerializable("cartItems", HashMap(cartItems))
            fragment.arguments = args
            return fragment
        }
    }
}

