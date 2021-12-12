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
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        view.btn_login.setOnClickListener {
            val email = view.email_login.text.toString()
            val password = view.password_login.text.toString()
            when {
                TextUtils.isEmpty(email) -> Toast.makeText(
                    activity?.applicationContext, "Email is required",
                    Toast.LENGTH_SHORT
                ).show()
                TextUtils.isEmpty(password) -> Toast.makeText(
                    activity?.applicationContext, "Password is required",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent =
                                    Intent(activity?.applicationContext, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {
                                val msg = task.exception!!.toString()
                                Toast.makeText(
                                    activity?.applicationContext, "Error: $msg",
                                    Toast.LENGTH_SHORT
                                ).show()
                                FirebaseAuth.getInstance().signOut()

                            }
                        }


                }
            }
        }
        return view




    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent =
                Intent(activity?.applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}