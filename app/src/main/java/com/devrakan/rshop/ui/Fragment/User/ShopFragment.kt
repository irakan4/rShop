package com.devrakan.rshop.ui.Fragment.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.StoreAdapter
import com.devrakan.rshop.Model.Store
import com.devrakan.rshop.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var x = "Laptop Store"
    private var storeAdapter: StoreAdapter? = null
    private var mStore: MutableList<Store>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_shop, container, false)

        recyclerView = view!!.findViewById(R.id.store_products)
        recyclerView?.layoutManager = GridLayoutManager(context, 2)

        mStore = ArrayList()
        storeAdapter = context?.let { StoreAdapter(it, mStore as ArrayList<Store>) }
        recyclerView?.adapter = storeAdapter

        var options = arrayOf("Laptop Store", "Phone Store")

        view.spinner2.adapter =
            ArrayAdapter<String>(
                requireActivity().applicationContext,
                android.R.layout.simple_list_item_1,
                options
            )
        view.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                x = options.get(position)


                val usersRef =
                    FirebaseDatabase.getInstance().getReference().child("Business").child(x)
                usersRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        mStore?.clear()
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(Store::class.java)
                            if (user != null) {

                                mStore?.add(user)

                            }
                        }
                        storeAdapter?.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

        }

        return view

    }


}