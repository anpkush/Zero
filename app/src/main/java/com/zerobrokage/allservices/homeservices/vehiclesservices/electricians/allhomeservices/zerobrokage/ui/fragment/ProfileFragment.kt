package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.FragmentProfileBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.AddressActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.EditProfileActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.FAQsActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val name = sharedPref?.getString("name", "")
        val mobileNo = sharedPref?.getString("mobile_number", "")
        val id = sharedPref?.getInt("id", 0)



        binding.tvUserName.text = name

        binding.tvEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java).apply {
                putExtra("name", name)
                putExtra("mobileNo", mobileNo)
                putExtra("id", id)
            }
            startActivity(intent)

        }

        binding.tvSaveAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
        }


        binding.tvFAndQ.setOnClickListener {
            val intent = Intent(requireContext(), FAQsActivity::class.java)
            startActivity(intent)
        }

        binding.tvTermandCondition.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://zerobrokage.com/term-condition")
            startActivity(intent)
        }

        binding.tvDisclaimer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://zerobrokage.com/disclaimer")
            startActivity(intent)
        }

        binding.tvPrivacyAndPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://zerobrokage.com/privacy-policy")
            startActivity(intent)
        }

        binding.btLogOut.setOnClickListener {
            sharedPref?.edit()?.apply {
                clear()
                putBoolean("isLoggedIn", false)
                apply()
            }

            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
