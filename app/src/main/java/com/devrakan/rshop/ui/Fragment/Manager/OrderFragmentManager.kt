package com.devrakan.rshop.ui.Fragment.Manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.Maneger.ProductAdapterM
import com.devrakan.rshop.Model.Manager.ProductsM
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OrderFragmentManager : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var managerAdapter: ProductAdapterM? = null
    private var mManager: MutableList<ProductsM>? = null

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
        mManager = ArrayList()
        managerAdapter = context?.let { ProductAdapterM(it, mManager as ArrayList<ProductsM>) }

        recyclerView?.adapter = managerAdapter
        retrieveProductManager()
        return view

    }
    private fun retrieveProductManager() {

        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().getReference().child("Products")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mManager?.clear()
                for (snapshot in snapshot.children) {
                    val manager = snapshot.getValue(ProductsM::class.java)
                    if (manager != null) {
                        if (firebaseUserId == manager.getPublisher()) {
                            mManager?.add(manager)
                        }

                    }

                }
                managerAdapter?.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}