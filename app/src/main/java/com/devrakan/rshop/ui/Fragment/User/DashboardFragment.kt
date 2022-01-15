package com.devrakan.rshop.ui.Fragment.User

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

class DashboardFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: CartAdapter? = null
    private var mUser: MutableList<Cart>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_cart)
        recyclerView?.layoutManager = GridLayoutManager(context,2)

        mUser = ArrayList()
        userAdapter = context?.let { CartAdapter(it, mUser as ArrayList<Cart>) }
        recyclerView?.adapter = userAdapter

        retrieveUsers()
        
        return view
    }

    private fun retrieveUsers() {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(currentUserId)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(Cart::class.java)
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