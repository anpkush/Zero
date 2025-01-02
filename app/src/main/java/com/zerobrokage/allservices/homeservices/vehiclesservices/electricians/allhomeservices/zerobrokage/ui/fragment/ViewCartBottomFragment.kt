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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCartBottomBinding.inflate(inflater, container, false)

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

    val cartItems = arguments?.getSerializable("cartItems") as? Map<Int, Int>
}
