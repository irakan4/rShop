package com.devrakan.rshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Model.Users
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserAdapter(
    private var mContext: Context,
    private var mUser: List<Users>,
    private var isFragment: Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.products_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var user = mUser[position]
        holder.userNameTextView.text = user.getUsername()


    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.tv_product_name)


    }

}