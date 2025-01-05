package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.ui.activity

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.databinding.ActivityEditProfileBinding
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditProfile
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.retrofitClient.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val id = intent.getIntExtra("id", 0)

        loadProfileData()

        binding.toolbar.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.tvTitle.text = "My Account"

        binding.etDate.setOnClickListener {

            showDatePicker()
        }

        binding.btUpdate.setOnClickListener {
            binding.btUpdate.setOnClickListener {
                if (validateFields()) {
                    editProfileData(id)
                    finish()
                }
            }

        }
    }

    private fun validateFields(): Boolean {

        val name = binding.etName.text.toString().trim()
        val mobileNumber = binding.etMobileNumber.text.toString().trim()
        val dob = binding.etDate.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobileNumber.isEmpty()) {
            Toast.makeText(this, "Mobile number cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (dob.isEmpty()) {
            Toast.makeText(this, "Date of birth cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

    private fun loadProfileData() {
        val name = sharedPref.getString("name", "")
        val mobileNumber = sharedPref.getString("mobile_number", "")
        val dob = sharedPref.getString("dob", "")
        val email = sharedPref.getString("email", "")
        val gender = sharedPref.getString("gender", "")

        binding.etName.setText(name)
        binding.etMobileNumber.setText(mobileNumber)
        binding.etDate.setText(dob)
        binding.etEmail.setText(email)
        when (gender) {
            "Male" -> binding.rbMale.isChecked = true
            "Female" -> binding.rbFemale.isChecked = true
            "Other" -> binding.rbOther.isChecked = true
            else -> binding.rbMale.isChecked = true
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val maxDateCalendar = Calendar.getInstance()
        maxDateCalendar.set(2005, Calendar.DECEMBER, 31)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDate.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis

        datePickerDialog.show()
    }

    private fun editProfileData(id: Int) {
        val dob = binding.etDate.text.toString()
        val email = binding.etEmail.text.toString()
        val gender = when (binding.rdGender.checkedRadioButtonId) {
            binding.rbMale.id -> "Male"
            binding.rbFemale.id -> "Female"
            binding.rbOther.id -> "Other"
            else -> ""
        }
        val name = binding.etName.text.toString()
        val mobileNumber = binding.etMobileNumber.text.toString()

        val editProfile = EditProfile(dob, email, gender, mobileNumber, name)


        RetrofitInstance.apiService.updateProfile(id, editProfile)
            .enqueue(object : Callback<EditProfile> {
                override fun onResponse(call: Call<EditProfile>, response: Response<EditProfile>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveProfileToSharedPrefs(editProfile)

                    } else {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Update failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<EditProfile>, t: Throwable) {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    private fun saveProfileToSharedPrefs(profile: EditProfile) {
        with(sharedPref.edit()) {
            putString("name", profile.name)
            putString("mobile_number", profile.mobile_number)

            apply()
        }
    }
}
