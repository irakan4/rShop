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
        user = intent.extras?.getBoolean("user")
        adapter = intent.extras?.getString("adapter").toString()
        if (user == false) {
            add_product_btn_delete.visibility = View.VISIBLE
            add_product_btn_delete.text = "Delete Product"
            add_product_btn_update.text = "Update Product"
            Toast.makeText(this@CartActivity,adapter.toString(),Toast.LENGTH_LONG).show()

            add_product_btn_delete.setOnClickListener {
                Toast.makeText(this@CartActivity,adapter.toString(),Toast.LENGTH_LONG).show()

                var reference =
                    FirebaseDatabase.getInstance().reference.child("Products").child(adapter)
                reference.removeValue()
                Toast.makeText(this@CartActivity, "product has been delete", Toast.LENGTH_SHORT)
                    .show()

                var intent = Intent(this@CartActivity, MainActivity::class.java)
                startActivity(intent)
                finish()


            }


        } else if (user == true) {
            add_product_btn_delete.text = "add Product"
            add_product_btn_delete.visibility = View.GONE


        }

        setUp()

        add_product_Image.setOnClickListener {
            val intentImg = Intent(Intent.ACTION_GET_CONTENT)
            intentImg.type = "image/*"
            startActivityForResult(intentImg, 2)

        }
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
                    Toast.makeText(this@CartActivity,adapter.toString(),Toast.LENGTH_LONG).show()
//                    if (user == false) {
//
//                    }

                  //  add_product_btn_update.setOnClickListener {
                        if (user == false) {

                   //         uploadImgCart()

                            var database2 = FirebaseDatabase.getInstance()
                            var ref2 = database2.getReference("Products").child(adapter)
                            ref2.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot2: DataSnapshot) {
                                    val product2: ProductU =
                                        snapshot2.getValue(ProductU::class.java)!!
                                    Picasso.get().load(product2.getProductImage())
                                        .into(add_product_Image)
                                    add_product_name.setText(product2.getProductName())
                                    add_product_description.setText(product2.getProductName())
                                    add_product_quantity.setText(product2.getProductCount())
                                    add_product_price.setText(product2.getProductPrice())
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })


                        } else if (user == true) {
                            uploadImgAdd()

                        }
                   // }


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

    private fun setUpUser() {
        val database = FirebaseDatabase.getInstance()
        var reference = database.getReference("Products").child(adapter)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products: ProductU = snapshot.getValue(ProductU::class.java)!!
                product_details_name.text = products.getProductName()
                product_details_price.text = products.getProductPrice()
                Picasso.get().load(products.getProductImage()).into(product_details_img)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun uploadImgAdd() {

        when {
            uriImg == null -> Toast.makeText(this, "Please Select img first", Toast.LENGTH_LONG)
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
                "Please write the product description",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("adding new Product")
                progressDialog.setMessage("Please wait, we are adding your Product ")
                progressDialog.show()

                val filRef = mStorage!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = filRef.putFile(uriImg!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }

                    }
                    return@Continuation filRef.downloadUrl

                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        myUri = downloadUri.toString()
                        val ref =
                            FirebaseDatabase.getInstance().reference.child("Products")
                        val productId = ref.push().key
                        val productMap = HashMap<String, Any>()
                        productMap["ProductId"] = productId!!
                        productMap["productName"] = add_product_name.text.toString().lowercase()
                        productMap["ProductImage"] = myUri
                        productMap["productCount"] =
                            add_product_quantity.text.toString().lowercase()
                        productMap["productPrice"] = add_product_price.text.toString().lowercase()
                        productMap["description"] =
                            add_product_description.text.toString().lowercase()
                        productMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        ref.child(productId!!).updateChildren(productMap)
                        Toast.makeText(this, "product was updated successfully", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@CartActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }


                }

            }

        }

    }

    private fun uploadImgCart() {
        when {
            uriImg == null -> Toast.makeText(this, "Please Select img first", Toast.LENGTH_LONG)
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
                "Please write the product description",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("updating Product details")
                progressDialog.setMessage("Please wait, we are updating your Product details ")
                progressDialog.show()

                val filRef = mStorage!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask: StorageTask<*>
                uploadTask = filRef.putFile(uriImg!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }

                    }
                    return@Continuation filRef.downloadUrl

                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        myUri = downloadUri.toString()
                        val ref =
                            FirebaseDatabase.getInstance().reference.child("Products").child(adapter)
                        val productMap = HashMap<String, Any>()

                        productMap["productName"] = add_product_name.text.toString().lowercase()
                        productMap["ProductImage"] = myUri
                        productMap["productCount"] =
                            add_product_quantity.text.toString().lowercase()
                        productMap["productPrice"] = add_product_price.text.toString().lowercase()
                        productMap["description"] =
                            add_product_description.text.toString().lowercase()
                        ref.updateChildren(productMap)
                        Toast.makeText(this, "product was updated successfully", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@CartActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }


                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            uriImg = data?.data
            add_product_Image.setImageURI(uriImg)
        }
    }


}
