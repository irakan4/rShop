package com.devrakan.rshop.ui.Fragment.Maneger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ProductAdapter
import com.devrakan.rshop.Model.Products
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var Adapter: ProductAdapter? = null
    private var mUser: MutableList<Products>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_dashboard2, container, false)

        recyclerView = view.findViewById(R.id.my_list_products)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(context, 2)

        mUser = ArrayList()
        Adapter = context?.let { ProductAdapter(it, mUser as ArrayList<Products>,true) }
        recyclerView?.adapter = Adapter

        retrieveUsers()




        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {


        }

    }

    private fun retrieveUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val usersRef = FirebaseDatabase.getInstance().getReference().child("Products")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(Products::class.java)
                    if (user != null) {
                        if (firebaseUserID == user.getPublisher()) {

                            mUser?.add(user)
                        }
                    }
                }
                Adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}