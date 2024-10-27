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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class PayOutActivity : AppCompatActivity() {

    lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)


        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase and user details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        //set User data
        setUserData()

        //get user details from firebase
//        val intent = intent
//        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String> //exact same name that we provided on CartFragment
//
//        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
//        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
//        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
//        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>
//
//        totalAmount = calculateTotalAmount().toString() + "$"
//        binding.totalAmount.isEnabled = false
//        binding.totalAmount.setText(totalAmount)

        binding.placeMyOrder.setOnClickListener {
            val bottomSheetDialog = CongratsFragmentSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount =0
        for(i in 0 until foodItemPrice.size){
            var price:String = foodItemPrice[i]
            val lastChar: Char = price.last()
            val priceIntValue: Int = if(lastChar == '$' ){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            //val priceIntValue = price.toInt()
            var quantity:Int = foodItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user != null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresses = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


}