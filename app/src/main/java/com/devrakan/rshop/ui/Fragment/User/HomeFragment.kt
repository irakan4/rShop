package com.devrakan.rshop.ui.Fragment.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ProductAdapter
import com.devrakan.rshop.Model.Products
import com.devrakan.rshop.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: ProductAdapter? = null
    private var mUser: MutableList<Products>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.list_products)
        recyclerView?.layoutManager = GridLayoutManager(context,2)

        mUser = ArrayList()
        userAdapter = context?.let { ProductAdapter(it, mUser as ArrayList<Products>) }
        recyclerView?.adapter = userAdapter

        retrieveUsers()


        return view
    }

    private fun retrieveUsers() {

        val usersRef = FirebaseDatabase.getInstance().getReference().child("Products")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(Products::class.java)
                    if (user != null) {

                            mUser?.add(user)

                    }
                }
                userAdapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



}