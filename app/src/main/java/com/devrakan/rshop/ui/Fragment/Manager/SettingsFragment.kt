package com.devrakan.rshop.ui.Fragment.Manager

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devrakan.rshop.CartActivity
import com.devrakan.rshop.MainActivity
import com.devrakan.rshop.Model.Users
import com.devrakan.rshop.R
import com.devrakan.rshop.ui.Authentication.LoginActivity
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
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {
    var x = true
    private var myUrl = ""
    private var uriImage: Uri? = null
    private var mStorege: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.btn_add_product_intint.setOnClickListener{
          val intent =  Intent(activity?.applicationContext,CartActivity::class.java)
            intent.putExtra("adapter",true)
            startActivity(intent)

        }

        mStorege = FirebaseStorage.getInstance().reference.child("Profile Pictures")
        if (x == true ){
            view.profile.visibility = View.VISIBLE
            x = false
        }

        val database = FirebaseDatabase.getInstance()
        val id = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val ref2 = database.getReference("Users").child(id)
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: Users = snapshot.getValue(Users::class.java)!!
                Picasso.get().load(user.getImage()).into(view.profile_img)
                view.profile_userN.text = user.getUsername()
                view.profile_phonen_Num.text = user.getPhone()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        view.profile_btn_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        view.profile_btn_settings.setOnClickListener {

            view.settings_store_img.setOnClickListener {
                val intentImage = Intent(Intent.ACTION_GET_CONTENT)
                intentImage.type = "image/*"
                startActivityForResult(intentImage, 3)

            }
            if (x == false) {
                view.settings.visibility = View.VISIBLE
                view.profile.visibility = View.GONE

                val database2 = FirebaseDatabase.getInstance()
                val id2 = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val ref22 = database2.getReference("Users").child(id2)
                ref22.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user2: Users = snapshot.getValue(Users::class.java)!!
                        Picasso.get().load(user2.getImage()).into(settings_store_img)
                        settings_store_username.setText(user2.getUsername())
                        settings_phone_number.setText(user2.getPhone())
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                view.settings_btn_update.setOnClickListener {
                    val name = view.settings_store_username.text.trim().toString()
                    val phone = view.settings_phone_number.text.trim().toString()

                    if (name.isEmpty()) {
                        settings_store_username.error = "Name Required"
                        settings_store_username.requestFocus()
                        return@setOnClickListener
                    }
                    if (phone.isEmpty()) {
                        settings_phone_number.error = "phone Required"
                        settings_phone_number.requestFocus()
                        return@setOnClickListener
                    }
                    update(phone, name)


                }



                x = true

            } else if (x == true) {
                view.profile.visibility = View.VISIBLE
                view.settings.visibility = View.GONE

            }
        }

        return view

    }

    private fun update(phone: String, name: String) {

        when {

            uriImage == null -> Toast.makeText(
                activity,
                "Please select image first",
                Toast.LENGTH_LONG
            )
                .show()

            TextUtils.isEmpty(name) -> Toast.makeText(
                activity,
                "Please write the product name first",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(phone) -> Toast.makeText(
                activity,
                "Please write the product price first",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(activity)
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

                        val ref = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        val postMap = HashMap<String, Any>()
                        postMap["phone"] = phone.toLowerCase()
                        postMap["username"] = name.toLowerCase()
                        postMap["image"] = myUrl

                        ref.updateChildren(postMap)
                        Toast.makeText(
                            activity,
                            "Products uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        progressDialog.dismiss()

                    } else {
                        progressDialog.dismiss()
                    }

                })
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == RESULT_OK) {
            uriImage = data?.data
            settings_store_img.setImageURI(uriImage)
        }
    }



}
