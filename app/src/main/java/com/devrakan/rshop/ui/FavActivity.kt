package com.devrakan.rshop.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.devrakan.rshop.Model.Products
import com.devrakan.rshop.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*

class FavActivity : AppCompatActivity() {

    private var myUrl = ""
    private var uriImage: Uri? = null
    private var x = ""
    private var y = ""
    private var user: Boolean? = null
    private var mStorege: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)


        mStorege = FirebaseStorage.getInstance().reference.child("Products Pictures")
        user = intent.extras?.getBoolean("user")
        x = intent.extras?.getString("pro").toString()

        if (user == false) {

            add_product_btn_delete.visibility = View.VISIBLE
            add_product_btn_delete.text = "Delete Product"
            add_product_btn_update.text = "Update Product"
            add_product_btn_delete.setOnClickListener {
                val ref = FirebaseDatabase.getInstance().reference.child("Products")
                    .child(x)
                ref.removeValue()
                Toast.makeText(
                    this@FavActivity,
                    "Products uploaded successfully",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this@FavActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }


        } else if (user == true) {
            add_product_btn_delete.text = "Add Product"
            add_product_btn_delete.visibility = View.GONE


        }



        setUp()

        add_product_Image.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_GET_CONTENT)
            intentImage.type = "image/*"
            startActivityForResult(intentImage, 2)

        }


    }


    private fun uploadImageAdd() {


        when {

            uriImage == null -> Toast.makeText(
                this@FavActivity,
                "Please select image first",
                Toast.LENGTH_LONG
            )
                .show()

            TextUtils.isEmpty(add_product_name.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_price.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_quantity.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product quantity first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(add_product_description.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product description first",
                Toast.LENGTH_LONG
            ).show()
            else -> {
                val progressDialog = ProgressDialog(this@FavActivity)
                progressDialog.setTitle("Adding New Product")
                progressDialog.setMessage("Please wait , we are adding your picture Products....")
                progressDialog.show()
                val fileref =
                    mStorege!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadeTask: StorageTask<*>
                uploadeTask = fileref.putFile(uriImage!!)
                uploadeTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }

                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Products")
                        var postid = ref.push().key
                        val postMap = HashMap<String, Any>()
                        postMap["ProductId"] = postid!!
                        postMap["productName"] = add_product_name.text.toString().toLowerCase()
                        postMap["productPrice"] =
                            add_product_price.text.toString().toLowerCase()
                        postMap["productDescription"] =
                            add_product_description.text.toString().toLowerCase()
                        postMap["productCount"] =
                            add_product_quantity.text.toString().toLowerCase()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["ProductImage"] = myUrl

                        ref.child(postid).updateChildren(postMap)
                        Toast.makeText(
                            this@FavActivity,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@FavActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()

                    } else {
                        progressDialog.dismiss()
                    }

                })
            }


        }

    }

    private fun uploadImageCart() {


        when {

            uriImage == null -> Toast.makeText(
                this@FavActivity,
                "Please select image first",
                Toast.LENGTH_LONG
            )
                .show()

            TextUtils.isEmpty(add_product_name.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_price.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_quantity.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product quantity first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(add_product_description.text.toString()) -> Toast.makeText(
                this@FavActivity,
                "Please write the product description first",
                Toast.LENGTH_LONG
            ).show()
            else -> {
                val progressDialog = ProgressDialog(this@FavActivity)
                progressDialog.setTitle("Adding New Product")
                progressDialog.setMessage("Please wait , we are adding your picture Products....")
                progressDialog.show()
                val fileref =
                    mStorege!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadeTask: StorageTask<*>
                uploadeTask = fileref.putFile(uriImage!!)
                uploadeTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }

                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Products")
                            .child(x)
                        val postMap = HashMap<String, Any>()
                        postMap["productName"] = add_product_name.text.toString().toLowerCase()
                        postMap["productPrice"] =
                            add_product_price.text.toString().toLowerCase()
                        postMap["productDescription"] =
                            add_product_description.text.toString().toLowerCase()
                        postMap["productCount"] =
                            add_product_quantity.text.toString().toLowerCase()
                        postMap["ProductImage"] = myUrl
                        ref.updateChildren(postMap)
                        Toast.makeText(
                            this@FavActivity,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@FavActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })
            }
        }
    }

    private fun setUpUser() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Products").child(x)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product: Products = snapshot.getValue(Products::class.java)!!
                cart_product_name.text = product.getProductName()
                Picasso.get().load(product.getProductImage()).into(cart_product_Image)
                cart_product_price.text = product.getProductPrice()
                y = product.getPublisher()

                cart_product_btn_addToCart.setOnClickListener {
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    val usersRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Cart").child(currentUserId)
                            .child(x)
                    val postMap = HashMap<String, Any>()
                    postMap["productName"] = product.getProductName()
                    postMap["publisher"] = currentUserId
                    postMap["number"] = x
                    postMap["Case"] = "under review"
                    postMap["productPrice"] = product.getProductPrice()
                    postMap["productDescription"] = product.getProductName()
                    postMap["productCount"] = product.getProductCount()
                    postMap["ProductImage"] = product.getProductImage()
                    usersRef.setValue(postMap)

                    val usersRefO: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Order").child(y)
                            .child(x)
                    val postMapO = HashMap<String, Any>()
                    postMapO["productName"] = product.getProductName()
                    postMapO["productPrice"] = product.getProductPrice()
                    postMapO["productDescription"] = product.getProductName()
                    postMapO["productCount"] = product.getProductCount()
                    postMapO["ProductImage"] = product.getProductImage()
                    postMapO["publisher"] = y
                    postMapO["number"] = x
                    postMapO["Case"] = "under review"
                    postMapO["Id"] = currentUserId
                    usersRefO.setValue(postMapO)
                    Toast.makeText(
                        this@FavActivity,
                        "Products uploaded successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent =
                        Intent(this@FavActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun setUp() {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Users").child(currentUserId).child("Manager")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var business: Boolean = snapshot.getValue(Boolean::class.java)!!
                if (business == true) {

                    lay_show_manger.visibility = View.VISIBLE
                    if (user == false) {
                        val database = FirebaseDatabase.getInstance()
                        val ref2 = database.getReference("Products").child(x)
                        ref2.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.getValue(Products::class.java)
                                    ?.let {
                                        Picasso.get().load(it.getProductImage())
                                            .into(add_product_Image)
                                        add_product_name.setText(it.getProductName())
                                        add_product_description.setText(it.getProductName())
                                        add_product_quantity.setText(it.getProductCount())
                                        add_product_price.setText(it.getProductPrice())
                                    }


                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                    }



                    add_product_btn_update.setOnClickListener {
                        if (user == false) {

                            uploadImageCart()


                        } else if (user == true) {

                            uploadImageAdd()

                        }
                    }


                } else if (business == false) {
                    lay_show_user.visibility = View.VISIBLE
                    setUpUser()

                }
            }

            override fun onCancelled(error: DatabaseError) {

                val error = error.message
                Toast.makeText(this@FavActivity, error, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            uriImage = data?.data
            add_product_Image.setImageURI(uriImage)
        }
    }

}