package com.devrakan.rshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Model.Store
import com.devrakan.rshop.R
import com.squareup.picasso.Picasso

class StoreAdapter (
    private var mContext: Context,
    private var mStore: List<Store>
): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list_m, parent, false)
        return StoreAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = mStore[position]

        holder.storeNameTextView.text = product.getUsername()
        Picasso.get().load(product.getImage()).into(holder.image)
        holder.image.setOnClickListener {}


        holder.textView.text = product.getUsername()
        holder.cauntTextView.text = product.getUsername()
    }

    override fun getItemCount(): Int {
        return mStore.size
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storeNameTextView: TextView = itemView.findViewById(R.id.item_product_name)
        var cauntTextView: TextView = itemView.findViewById(R.id.item_product_quantity)
        var textView: TextView = itemView.findViewById(R.id.item_product_price)
        var image: ImageView = itemView.findViewById(R.id.item_product_img)


    }

}