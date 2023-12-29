package com.example.examsuitmedia.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examsuitmedia.R
import com.example.examsuitmedia.databinding.ActivityFirstScreenBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FirstScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            val inputText = binding.inputPalindrome.editText?.text.toString().trim()
            val isPalindrome = checkPalindrome(inputText)

            val message = if (isPalindrome) "isPalindrome" else "Not Palindrome"
            showMessageDialog(message)
        }

        binding.buttonNext.setOnClickListener {
            val inputText = binding.tvInputName.editText?.text.toString().replace("\\s".toRegex(), "")
            if (inputText.isNotEmpty()){
                val intent = Intent(this, SecondScreenActivity::class.java)
                intent.putExtra("SHOW_NAME", inputText)
                startActivity(intent)
            }else{
                showMessageDialog("Please enter a name")
            }
        }
    }

    private fun checkPalindrome(text: String): Boolean {
        val cleanText = text.lowercase()
        val reversedText = cleanText.reversed()
        return cleanText == reversedText
    }

    private fun showMessageDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}