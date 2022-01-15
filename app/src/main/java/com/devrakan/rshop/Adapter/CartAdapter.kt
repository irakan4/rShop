package com.devrakan.rshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Model.Cart
import com.devrakan.rshop.OrderActivity
import com.devrakan.rshop.R
import com.squareup.picasso.Picasso

class CartAdapter(
    private var mContext: Context,
    private var mProduct: ArrayList<Cart>,
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var product = mProduct[position]
        Picasso.get().load(product.getProductImage()).into(holder.productImage)
        holder.productImage.setOnClickListener {
            val intent = Intent(mContext, OrderActivity::class.java)
            intent.putExtra("Id", product.getPublisher())
            intent.putExtra("Number", product.getNumber())
            intent.putExtra("Id2", product.getId())
            mContext.startActivity(intent)
        }
        holder.productNameTextView.text = product.getProductName()
        holder.productPriceTextView.text = product.getProductPrice()
        holder.productCauntView.text = product.getProductCount()


    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView = itemView.findViewById(R.id.image_date_R)
        var productNameTextView: TextView = itemView.findViewById(R.id.tv_product_name)
        var productPriceTextView: TextView = itemView.findViewById(R.id.textView4)
        var productCauntView: TextView = itemView.findViewById(R.id.textView5)


    }

}