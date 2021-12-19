package com.devrakan.rshop.Adapter.Maneger

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.R

class ProductAdapterM(
    private var mContext: Context,
    private var mProduct: List<ProductsM>,
) : RecyclerView.Adapter<ProductAdapterM.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list_m, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var product = mProduct[position]
        Picasso.get().load(product.getProductImage()).into(holder.productImage)


        holder.productNameTextView.text = product.getProductName()
        holder.productPriceTextView.text = "P: ${product.getProductPrice()} $"
        holder.productCauntView.text = "Q: ${product.getProductCount()}"
        holder.productImageEdt.setOnClickListener {

            val inflater = LayoutInflater.from(mContext)
            val view = inflater.inflate(R.layout.activity_add_product,null)

            val builder = AlertDialog.Builder(mContext)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()


//            val ref = FirebaseDatabase.getInstance().reference.child("Products").child(product.getProductId())
//            val postMap = HashMap<String, Any>()
//            postMap["productName"] = "m"
//            postMap["productPrice"] = "m"
//            postMap["productCount"] = "m"
//            postMap["ProductImage"] = "m"

            //  ref.updateChildren(postMap)


        }

    }



    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView = itemView.findViewById(R.id.image_date_R)
        var productImageEdt: ImageView = itemView.findViewById(R.id.edt_image)
        var productNameTextView: TextView = itemView.findViewById(R.id.tv_product_name)
        var productPriceTextView: TextView = itemView.findViewById(R.id.textView4)
        var productCauntView: TextView = itemView.findViewById(R.id.textView5)


    }


}