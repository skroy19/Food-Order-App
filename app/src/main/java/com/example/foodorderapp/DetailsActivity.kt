package com.example.foodorderapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodorderapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName:String? = null
    private var foodImage:String? = null
    private var foodPrice:String? = null
    private var foodDescription:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text = foodName
            descriptionTextView.text = foodDescription

            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)

        }

        binding.imageButton.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}