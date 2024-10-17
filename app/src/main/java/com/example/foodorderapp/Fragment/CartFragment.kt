package com.example.foodorderapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.adaptar.CartAdaptar
import com.example.foodorderapp.PayOutActivity
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false)

        val cartFoodName = listOf("Burger","Sandwich","Momo","Pasta","Chicken","Chawmin")
        val cartPrice = listOf("$5","$7","$4","$6","$8","$10")
        val cartImage = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu4,
            R.drawable.menu2
        )
        val adapter = CartAdaptar(ArrayList(cartFoodName),ArrayList(cartPrice),ArrayList(cartImage))
        binding.cardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerView.adapter = adapter

        binding.proceedButton.setOnClickListener{
            val intent = Intent(requireContext(),PayOutActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {

    }
}