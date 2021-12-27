package com.devrakan.rshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ProductAdapter.ViewHolder
import com.devrakan.rshop.CartActivity
import com.devrakan.rshop.Model.ProductU
import com.devrakan.rshop.R
import com.squareup.picasso.Picasso

class ProductAdapter(private val mContext: Context, private val mProduct: List<ProductU>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list_m,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder , position: Int) {
        val product = mProduct[position]
        Picasso.get().load(product.getProductImage()).into(holder.productImage)
        holder.productImage.setOnClickListener {
            val intent = Intent(mContext,CartActivity::class.java)
            intent.putExtra("pro",product.getProductId())
            intent.putExtra("adapter",false)
            mContext.startActivity(intent)

        }

        holder.productName.text = product.getProductName()
        holder.productPrice.text = product.getProductPrice()
        holder.productQuantity.text = product.getProductCount()

            }

    override fun getItemCount(): Int {
        return mProduct.size
    }

    class ViewHolder(@NonNull itemView:View):RecyclerView.ViewHolder(itemView) {
        val productImage:ImageView = itemView.findViewById(R.id.item_product_img)
        val productName:TextView = itemView.findViewById(R.id.item_product_name)
        val productPrice:TextView = itemView.findViewById(R.id.item_product_price)
        val productQuantity:TextView = itemView.findViewById(R.id.item_product_quantity)

    }



}

