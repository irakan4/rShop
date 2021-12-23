package com.devrakan.rshop.ui.Fragment.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.User.ProductAdapterUser
import com.devrakan.rshop.Model.ProductU
import com.devrakan.rshop.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var mProductAdapterUser: ProductAdapterUser? = null
    private var mProductU: MutableList<ProductU>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home,container,false)
        recyclerView = view.findViewById(R.id.list_product)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(context, 2)
        mProductU = ArrayList()
        mProductAdapterUser = context?.let { ProductAdapterUser(it, mProductU as ArrayList<ProductU>) }
        recyclerView?.adapter = mProductAdapterUser
        retrieveProductManager()
        return view
    }
    private fun retrieveProductManager() {

        val userRef = FirebaseDatabase.getInstance().getReference().child("Products")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mProductU?.clear()
                for (snapshot in snapshot.children) {
                    val product = snapshot.getValue(ProductU::class.java)
                    if (product != null) {
                        mProductU?.add(product)


                    }

                }

                mProductAdapterUser?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}