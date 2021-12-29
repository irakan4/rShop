package com.devrakan.rshop

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.devrakan.rshop.Model.ProductU
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.products_list_m.*
import java.util.concurrent.ThreadLocalRandom.current

class CartActivity : AppCompatActivity() {
    private var myUri = ""
    private var uriImg: Uri? = null
    private var adapter = ""

    private var user: Boolean? = null
    private var mStorage: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        mStorage = FirebaseStorage.getInstance().reference.child("Products Pictures")
        user = intent.extras?.getBoolean("adapter")
        adapter = intent.extras?.getString("pro").toString()

        if (user == false) {


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

            uriImg == null -> Toast.makeText(
                this,
                "Please select image first",
                Toast.LENGTH_LONG
            )
                .show()

            TextUtils.isEmpty(add_product_name.text.toString()) -> Toast.makeText(
                this,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_price.text.toString()) -> Toast.makeText(
                this,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_quantity.text.toString()) -> Toast.makeText(
                this,
                "Please write the product quantity first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(add_product_description.text.toString()) -> Toast.makeText(
                this,
                "Please write the product description first",
                Toast.LENGTH_LONG
            ).show()
            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding New Product")
                progressDialog.setMessage("Please wait , we are adding your picture Products....")
                progressDialog.show()
                val fileref =
                    mStorage!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadeTask: StorageTask<*>
                uploadeTask = fileref.putFile(uriImg!!)
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
                        myUri = downloadUrl.toString()

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
                        postMap["ProductImage"] = myUri

                        ref.child(postid).updateChildren(postMap)
                        Toast.makeText(
                            this,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@CartActivity, MainActivity::class.java)
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

            uriImg == null -> Toast.makeText(
                this,
                "Please select image first",
                Toast.LENGTH_LONG
            )
                .show()

            TextUtils.isEmpty(add_product_name.text.toString()) -> Toast.makeText(
                this,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_price.text.toString()) -> Toast.makeText(
                this,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(add_product_quantity.text.toString()) -> Toast.makeText(
                this,
                "Please write the product quantity first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(add_product_description.text.toString()) -> Toast.makeText(
                this,
                "Please write the product description first",
                Toast.LENGTH_LONG
            ).show()
            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding New Product")
                progressDialog.setMessage("Please wait , we are adding your picture Products....")
                progressDialog.show()
                val fileref =
                    mStorage!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadeTask: StorageTask<*>
                uploadeTask = fileref.putFile(uriImg!!)
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
                        myUri = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Products")
                            .child(adapter)
                        val postMap = HashMap<String, Any>()
                        postMap["productName"] = add_product_name.text.toString().toLowerCase()
                        postMap["productPrice"] =
                            add_product_price.text.toString().toLowerCase()
                        postMap["productDescription"] =
                            add_product_description.text.toString().toLowerCase()
                        postMap["productCount"] =
                            add_product_quantity.text.toString().toLowerCase()
                        postMap["ProductImage"] = myUri
                        ref.updateChildren(postMap)
                        Toast.makeText(
                            this,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@CartActivity, MainActivity::class.java)
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
        val ref = database.getReference("Products").child(adapter)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product: ProductU = snapshot.getValue(ProductU::class.java)!!
                item_product_name.text = product.getProductName()
                Picasso.get().load(product.getProductImage()).into(item_product_img)
                item_product_price.text = product.getProductPrice()
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


//                            val database = FirebaseDatabase.getInstance()
//                            val ref2 = database.getReference("Products").child(adapter)
//                            ref2.addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    val product: ProductU =
//                                        snapshot.getValue(ProductU::class.java)!!
//                                    Picasso.get().load(product.getProductImage())
//                                        .into(add_product_Image)
//                                    add_product_name.setText(product.getProductName())
//                                    add_product_description.setText(product.getProductName())
//                                    add_product_quantity.setText(product.getProductCount())
//                                    add_product_price.setText(product.getProductPrice())
//
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//
//                                    val error = error.message
//                                    Toast.makeText(this@CartActivity, error, Toast.LENGTH_LONG).show()
//
//                                }
//                            })


                    }

                    add_product_btn_delete.visibility = View.VISIBLE
                    add_product_btn_delete.text = "Delete Product"
                    add_product_btn_update.text = "Update Product"
                    add_product_btn_delete.setOnClickListener {
                        val ref = FirebaseDatabase.getInstance().reference.child("Products")
                            .child(adapter)
                        ref.removeValue()
                        Toast.makeText(
                            this@CartActivity,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@CartActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                    add_product_btn_update.setOnClickListener {
                        if (user == false) {

                            uploadImageCart()


                        } else if (user == true) {
                            uploadImageAdd()

                        }
                    }


                } else if (business == false) {
                    product_Lay_user.visibility = View.VISIBLE

                    setUpUser()

                }


            }

            override fun onCancelled(error: DatabaseError) {

                val error = error.message
                Toast.makeText(this@CartActivity, error, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            uriImg = data?.data
            add_product_Image.setImageURI(uriImg)
        }
    }

}
