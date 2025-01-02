import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.adapter.ItemClickListener
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.BottomsheetServicesBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ServiceMenuData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity.SubMenuActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.fragment.BottomMenuAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetServicesFragment : BottomSheetDialogFragment(), ItemClickListener {
    private var _binding: BottomsheetServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetServicesBinding.inflate(inflater, container, false)
        val view = binding.root

        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")

        if (id != null && name != null) {
            getApi(id, name)
        } else {
            Toast.makeText(context, "Invalid service ID or name", Toast.LENGTH_SHORT).show()
        }

        binding.ivClose.setOnClickListener { dismiss() }

        return view
    }

    private fun getApi(id: Int, name: String) {
        RetrofitInstance.apiService.getServicesMenuData(id)
            .enqueue(object : Callback<ServiceMenuData?> {
                override fun onResponse(
                    call: Call<ServiceMenuData?>,
                    response: Response<ServiceMenuData?>
                ) {

                    if (_binding == null) return

                    if (response.isSuccessful && response.body() != null) {
                        val menuList = response.body()?.data ?: emptyList()
                        if (menuList.isNotEmpty()) {
                            binding.rvMenuServices.apply {
                                layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter =
                                    BottomMenuAdapter(menuList, this@BottomSheetServicesFragment)
                            }
                            binding.tvTitleServiceName.text = name
                        } else {
                            Toast.makeText(context, "No services available", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Failed to load services", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ServiceMenuData?>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Error occurred: ${t.localizedMessage ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    companion object {
        fun newInstance(id: Int, name: String): BottomSheetServicesFragment {
            val fragment = BottomSheetServicesFragment()
            val args = Bundle()
            args.putInt("id", id)
            args.putString("name", name)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(id: Int, name: String) {
        val intent = Intent(requireContext(), SubMenuActivity::class.java).apply {
            putExtra("id", id)
            putExtra("name", name)
        }
        startActivity(intent)
    }
}
