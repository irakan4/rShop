package com.devrakan.rshop.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.devrakan.rshop.MainActivity
import com.devrakan.rshop.R
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_product.*
import java.net.URI

class AddProductActivity : AppCompatActivity() {

    var myUri = ""
    var uriImg: Uri? = null
    var mStorage: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        mStorage = FirebaseStorage.getInstance().reference.child("Products Pictures")
        btn_add_product.setOnClickListener {
            uploadImage()
        }

        btn_add_img.setOnClickListener{
            var intentImg = Intent(Intent.ACTION_GET_CONTENT)
            intentImg.type = "image/*"
            startActivityForResult(intentImg,2)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK){
            uriImg = data?.data
            btn_add_img.setImageURI(uriImg)
        }
    }

    private fun uploadImage() {
        when {
            uriImg == null -> Toast.makeText(this, "Please select img first", Toast.LENGTH_LONG)
                .show()
            TextUtils.isEmpty(ed_product_name.text.toString()) -> Toast.makeText(
                this,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(ed_product_price.text.toString()) -> Toast.makeText(
                this,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()

            TextUtils.isEmpty(ed_product_quantity.text.toString()) -> Toast.makeText(
                this,
                "Please write the product quantity first",
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
                        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
                        val postMap = HashMap<String, Any>()
                        postMap["ProductId"] = postid!!
                        postMap["productName"] = ed_product_name.text.toString().toLowerCase()
                        postMap["productPrice"] = ed_product_price.text.toString().toLowerCase()
                        postMap["productCount"] = ed_product_quantity.text.toString().toLowerCase()
                        postMap["publisher"] = firebaseUserID
                        postMap["ProductImage"] = myUri

                        ref.child(postid).updateChildren(postMap)
                        Toast.makeText(
                            this,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@AddProductActivity, MainActivity::class.java)
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
}