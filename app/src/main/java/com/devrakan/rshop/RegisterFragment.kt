package com.devrakan.rshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlin.concurrent.timerTask


class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.btn_register.setOnClickListener {
            val userName = view.usernameSingup.text.toString()
            val email = view.email_singup.text.toString()
            val password = view.password_singup.text.toString()
            val phone = view.phone_singup.text.toString()

            when {
                TextUtils.isEmpty(phone) ->
                    Toast.makeText(
                        activity?.applicationContext,
                        "phone number is required",
                        Toast.LENGTH_LONG
                    ).show()
                TextUtils.isEmpty(userName) ->
                    Toast.makeText(
                        activity?.applicationContext,
                        "user name is required",
                        Toast.LENGTH_LONG
                    ).show()
                TextUtils.isEmpty(password) ->
                    Toast.makeText(
                        activity?.applicationContext,
                        "password is required",
                        Toast.LENGTH_LONG
                    ).show()
                TextUtils.isEmpty(email) ->
                    Toast.makeText(
                        activity?.applicationContext,
                        "Email is required",
                        Toast.LENGTH_LONG
                    ).show()
                else -> {
                    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val currentUserId =
                                    FirebaseAuth.getInstance().currentUser?.uid.toString()
                                val usersRef: DatabaseReference =
                                    FirebaseDatabase.getInstance().reference.child("Users")
                                var userMap = HashMap<String, Any>()
                                userMap["uid"] = currentUserId
                                userMap["username"] = userName.toLowerCase()
                                userMap["phone"] = phone.toLowerCase()
                                userMap["email"] = email.toLowerCase()
                                usersRef.child(currentUserId).setValue(userMap)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {

                                            Toast.makeText(
                                                activity?.applicationContext,
                                                "account has been created successfully",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                            val intent =
                                                Intent(
                                                    activity?.applicationContext,
                                                    MainActivity::class.java
                                                )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)

                                        } else {
                                            val msg = task.exception!!.toString()
                                            Toast.makeText(
                                                activity?.applicationContext,
                                                "Error: $msg",
                                                Toast.LENGTH_LONG
                                            )
                                            mAuth.signOut()


                                        }
                                    }


                            }

                        }
                }

            }
        }
        return view
    }

}