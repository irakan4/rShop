package com.devrakan.rshop.ui.Fragment.Manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ProductAdapter
import com.devrakan.rshop.Model.ProductU
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OrderFragmentManager : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var Adapter: ProductAdapter? = null
    private var mList: MutableList<ProductU>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_manager, container, false)
        recyclerView = view.findViewById(R.id.manager_list_products)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(context, 2)
        mList = ArrayList()
        Adapter = context?.let { ProductAdapter(it, mList as ArrayList<ProductU>) }
        recyclerView?.adapter = Adapter
        retrieveProductManager()
        return view
        
    }
    private fun retrieveProductManager() {

        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().getReference().child("Products")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList?.clear()
                for (snapshot in snapshot.children) {
                    val manager = snapshot.getValue(ProductU::class.java)
                    if (manager != null) {
                        if (firebaseUserId == manager.getPublisher()) {
                            mList?.add(manager)
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