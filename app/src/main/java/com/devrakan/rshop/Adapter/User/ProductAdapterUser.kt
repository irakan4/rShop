package com.devrakan.rshop.Adapter.User

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
import com.devrakan.rshop.CartActivity
import com.devrakan.rshop.Model.User.ProductU
import com.devrakan.rshop.R
import com.squareup.picasso.Picasso

class ProductAdapterUser(private var mContext: Context, private var mUserProduct: List<ProductU>) :
    RecyclerView.Adapter<ProductAdapterUser.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapterUser.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list_m, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapterUser.ViewHolder, position: Int) {
        val product = mUserProduct[position]
        holder.productName.text = product.getProductName()
        holder.productPrice.text = "P: ${product.getProductPrice()} $"
        holder.productQuantity.text = "Q: ${product.getProductCount()}"
        Picasso.get().load(product.getProductImage()).into(holder.productImg)

        holder.productImg.setOnClickListener {
            val intent = Intent(mContext,CartActivity::class.java)
            intent.putExtra("ProductId",product.getProductId())
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mUserProduct.size
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName:TextView = itemView.findViewById(R.id.item_product_name)
        val productPrice:TextView = itemView.findViewById(R.id.item_product_price)
        val productQuantity:TextView = itemView.findViewById(R.id.item_product_quantity)
        val productImg:ImageView = itemView.findViewById(R.id.item_product_img)


    }

}