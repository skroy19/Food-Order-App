package com.example.foodorderapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapp.databinding.ActivityPayOutBinding
import com.example.foodorderapp.databinding.FragmentCongratsSheetBinding

class PayOutActivity : AppCompatActivity() {

    lateinit var binding: ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)


        enableEdgeToEdge()
        setContentView(binding.root)

        binding.placeMyOrder.setOnClickListener {
            val bottomSheetDialog = CongratsFragmentSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}