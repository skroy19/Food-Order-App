package com.example.foodorderapp.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.databinding.MenuItemBinding
import com.example.foodorderapp.DetailsActivity
import com.example.foodorderapp.Model.MenuItem

class MenuAdaptar(
//    private val menuItemsName:MutableList<String>,
//    private val menuItemPrice:MutableList<String>,
//    private val MenuImage: MutableList<Int>,
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdaptar.MenuViewHolder>() {

    //private val itemClickListener: OnClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder((binding))
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                    //itemClickListener?.onItemClick(position)
                }

//                //sets on clicklistener to open details
//
//                val intent = Intent(requireContext, DetailsActivity::class.java)
//                intent.putExtra("MenuItemName", menuItemsName.get(position))
//                intent.putExtra("MenuItemImage", MenuImage.get(position))
//                requireContext.startActivity((intent))
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem: MenuItem = menuItems[position]
            //a intent to open details activity and pass data
            val intent: Intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemImage", menuItem.foodImage)
            }
            //start the details activity
            requireContext.startActivity(intent)
        }


        //set data into recyclerview items
        fun bind(position: Int) {
            val menuItem:MenuItem = menuItems[position]
            binding.apply {
                menuFoodName.text = menuItem.foodName
                menuPrice.text = menuItem.foodPrice

                val uri:Uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)


            }
        }


    }


}


