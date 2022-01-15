package com.devrakan.rshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.devrakan.rshop.Model.Cart
import com.devrakan.rshop.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*
import java.util.HashMap

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val database = FirebaseDatabase.getInstance()

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = database.getReference("Users").child(currentUserId).child("Manager")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var business: Boolean = snapshot.getValue(Boolean::class.java)!!
                if (business == true) {
                    val Id = intent.extras?.getString("Id").toString()
                    val Id2 = intent.extras?.getString("Id2").toString()
                    val Number = intent.extras?.getString("Number").toString()

                    Toast.makeText(this@OrderActivity,Id, Toast.LENGTH_LONG).show()
                    Toast.makeText(this@OrderActivity,Number, Toast.LENGTH_LONG).show()

                    val ref2 = database.getReference("Order").child(Id).child(Number)
                    ref2.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val cart: Cart =
                                snapshot.getValue(Cart::class.java)!!
                            Picasso.get().load(cart.getProductImage())
                                .into(order_Image)
                            order_Store_Name.text = cart.getProductName()
                            order_product_number.text = cart.getProductCount()
                            order_phone_number.text = cart.getProductPrice()
                            order_Case.text = cart.getCase()

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                    order_btn_view.setOnClickListener {
                        val ref = FirebaseDatabase.getInstance().reference.child("Order")
                            .child(Id).child(Number)
                        val postMap = HashMap<String, Any>()
                        postMap["Case"] = "Your request has been accepted"

                        ref.updateChildren(postMap)

                        val ref2 = FirebaseDatabase.getInstance().reference.child("Cart")
                            .child(Id2).child(Number)
                        val postMap2 = HashMap<String, Any>()
                        postMap2["Case"] = "Your request has been accepted"

                        ref2.updateChildren(postMap2)
                        Toast.makeText(
                            this@OrderActivity,
                            "successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@OrderActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    order_btn_chat.setOnClickListener {
                        val ref = FirebaseDatabase.getInstance().reference.child("Order")
                            .child(Id).child(Number)
                        val postMap = HashMap<String, Any>()
                        postMap["Case"] = "Your request has been rejected"

                        ref.updateChildren(postMap)
                        val ref2 = FirebaseDatabase.getInstance().reference.child("Cart")
                            .child(Id2).child(Number)
                        val postMap2 = HashMap<String, Any>()
                        postMap2["Case"] = "Your request has been rejected"

                        ref2.updateChildren(postMap2)
                        Toast.makeText(
                            this@OrderActivity,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@OrderActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                } else if (business == false) {
                    val Id = intent.extras?.getString("Id").toString()
                    val Number = intent.extras?.getString("Number").toString()

                    val ref2 = database.getReference("Cart").child(Id).child(Number)
                    ref2.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val cart: Cart =
                                snapshot.getValue(Cart::class.java)!!
                            Picasso.get().load(cart.getProductImage())
                                .into(order_Image)
                            order_Store_Name.text = cart.getProductName()
                            order_product_number.text = cart.getProductCount()
                            order_phone_number.text = cart.getProductPrice()
                            order_Case.text = cart.getCase()

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    order_btn_view.visibility = View.GONE
                    order_btn_chat.visibility = View.GONE


                }
            }

            override fun onCancelled(error: DatabaseError) {

                val error = error.message
                Toast.makeText(this@OrderActivity, error, Toast.LENGTH_LONG).show()
            }

        })




    }
}