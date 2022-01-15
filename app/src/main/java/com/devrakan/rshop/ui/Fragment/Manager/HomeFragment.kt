package com.devrakan.rshop.ui.Fragment.Maneger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.CartAdapter
import com.devrakan.rshop.Model.Cart
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var Adapter: CartAdapter? = null
    private var mUser: MutableList<Cart>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home2, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(context, 2)

        mUser = ArrayList()
        Adapter = context?.let { CartAdapter(it, mUser as ArrayList<Cart>) }
        recyclerView?.adapter = Adapter

        retrieveUsers()




        return view
    }


    private fun retrieveUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val usersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(firebaseUserID)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(Cart::class.java)
                    if (user != null) {

                        mUser?.add(user)

                    }
                }
                Adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}