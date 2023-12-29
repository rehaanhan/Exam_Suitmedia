package com.example.examsuitmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.examsuitmedia.R
import com.example.examsuitmedia.databinding.ActivityFirstScreenBinding
import com.example.examsuitmedia.databinding.ActivitySecondScreenBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SecondScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val showName = intent.getStringExtra(USER_NAME)
        Log.d("SecondScreenActivity", "Received showName: $showName")
        val selectedUserName = sharedPreferences.getString("SELECTED_USER_NAME", "")

        binding.tvWelcome.text = getString(R.string.welcome)
        binding.tvUsername.text = intent.getStringExtra(USER_NAME)

        if (selectedUserName.isNullOrEmpty()) {
            binding.tvSelectedUser.text = getString(R.string.selectedUser)
        } else {
            binding.tvSelectedUser.text = selectedUserName
        }

        binding.btnSelectedUser.setOnClickListener {
            val intent = Intent(this@SecondScreenActivity, ThirdScreenActivity::class.java)
            intent.putExtra(USER_NAME, showName)
            startActivity(intent)
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_USER && resultCode == Activity.RESULT_OK) {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val selectedUserName = sharedPreferences.getString("SELECTED_USER_NAME", "")

            if (selectedUserName.isNullOrEmpty()) {
                binding.tvSelectedUser.text = getString(R.string.selectedUser)
            } else {
                binding.tvSelectedUser.text = selectedUserName
            }
        }
    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    companion object {
        const val USER_NAME = "SHOW_NAME"
        const val REQUEST_SELECT_USER = 1
    }

}
