package com.devrakan.rshop.ui.Fragment.Maneger

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devrakan.rshop.Model.Users
import com.devrakan.rshop.R
import com.devrakan.rshop.ShowMessageActivity
import com.devrakan.rshop.ui.Authentication.LoginActivity
import com.devrakan.rshop.ui.CartActivity
import com.devrakan.rshop.ui.MainActivity
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
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_notifications2.*
import kotlinx.android.synthetic.main.fragment_notifications2.view.*
import java.util.*


class NotificationsFragment : Fragment() {

    var x = true
    private var myUrl = ""
    private var uriImage: Uri? = null
    private var mStorege: StorageReference? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_notifications2, container, false)

        view.chat.setOnClickListener {

            val intent = Intent(context, ShowMessageActivity::class.java)
            startActivity(intent)

        }

        mStorege = FirebaseStorage.getInstance().reference.child("Products Pictures")
        if (x == true) {
            view.profile.visibility = View.VISIBLE
            x = false
        }

        view.add_product.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("user", true);
            startActivity(intent)
        }

        val database = FirebaseDatabase.getInstance()
        val id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref2 = database.getReference("Users").child(id)
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: Users = snapshot.getValue(Users::class.java)!!
                Picasso.get().load(user.getImage()).into(profile_Image)
                profile_user_name.text = user.getUsername()
                profile_phone_number.text = user.getPhone()
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

            view.settings_Image.setOnClickListener {
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
                        Picasso.get().load(user2.getImage()).into(settings_Image)
                        settings_user_name.setText(user2.getUsername())
                        settings_phone_number.setText(user2.getPhone())
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                view.settings_btn_settings.setOnClickListener {
                    val name = view.settings_user_name.text.trim().toString()
                    val phone = view.settings_phone_number.text.trim().toString()

                    if (name.isEmpty()) {
                        edText_name_sign_up.error = "Name Required"
                        edText_name_sign_up.requestFocus()
                        return@setOnClickListener
                    }
                    if (phone.isEmpty()) {
                        edText_phone_sign_up.error = "phone Required"
                        edText_phone_sign_up.requestFocus()
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
                        val database = FirebaseDatabase.getInstance()
                        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                        val ref3 = database.getReference("Users").child(currentUserId)
                        ref3.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user: Users =
                                    snapshot.getValue(Users::class.java)!!

                                val x = user.getWork()

                                val ref = FirebaseDatabase.getInstance().reference.child("Users")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                val postMap = HashMap<String, Any>()
                                postMap["phone"] = phone.toLowerCase()
                                postMap["username"] = name.toLowerCase()
                                postMap["image"] = myUrl

                                ref.updateChildren(postMap)

                                val ref2 =
                                    FirebaseDatabase.getInstance().reference.child("Business")
                                        .child(x)
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                val postMap2 = HashMap<String, Any>()
                                postMap2["phone"] = phone.toLowerCase()
                                postMap2["username"] = name.toLowerCase()
                                postMap2["image"] = myUrl

                                ref2.updateChildren(postMap2)
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


                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

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
            settings_Image.setImageURI(uriImage)
        }
    }


}
