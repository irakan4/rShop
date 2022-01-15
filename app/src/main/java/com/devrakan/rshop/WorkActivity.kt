package com.devrakan.rshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ProductAdapter
import com.devrakan.rshop.Model.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WorkActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var Adapter: ProductAdapter? = null
    private var mUser: MutableList<Products>? = null
    var x = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        x = intent.extras?.getString("work").toString()

        recyclerView = findViewById(R.id.work_list_products)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(this, 2)

        mUser = ArrayList()
        Adapter = this?.let { ProductAdapter(it, mUser as ArrayList<Products>, true) }
        recyclerView?.adapter = Adapter

        retrieveUsers()


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
                        if (x == user.getPublisher()) {

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