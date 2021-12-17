package com.devrakan.rshop

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(),TextWatcher {
    var x: String? = null
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        back.setOnClickListener {
            finish()
        }
        var business = intent.extras?.getBoolean("isManager")

        if (business == true) {

            l1.visibility = View.VISIBLE
            tv_signup.text = "Create an account for your business"

            var options = arrayOf("Dental Clinic", "Law Office")

            spinner.adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    x = options.get(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

            }

        } else if (business == false) {
            l1.visibility = View.GONE

        }

        //TextWatcher
        edText_name_sign_up.addTextChangedListener(this@SignUpActivity)
        edText_email_sign_up.addTextChangedListener(this@SignUpActivity)
        edText_password_sign_up.addTextChangedListener(this@SignUpActivity)
        //*********//


        //signUp
        btn_sign_up.setOnClickListener {
            val name = edText_name_sign_up.text.trim().toString()
            val email = edText_email_sign_up.text.trim().toString()
            val password = edText_password_sign_up.text.trim().toString()

            if (name.isEmpty()) {
                edText_name_sign_up.error = "Name Required"
                edText_name_sign_up.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                edText_email_sign_up.error = "Email Required"
                edText_email_sign_up.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edText_email_sign_up.error = "Please Enter a valid email"
                edText_email_sign_up.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 8) {
                edText_password_sign_up.error = " 8 char required"
                edText_password_sign_up.requestFocus()
                return@setOnClickListener
            }

            createNewAccount(name, email, password)

        }


    }


    // create New Account
    private fun createNewAccount(name: String, email: String, password: String) {
        val progreessDialog = ProgressDialog(this@SignUpActivity)
        progreessDialog.setTitle("SignUp")
        progreessDialog.setMessage("Please whit, this may take a while...")
        progreessDialog.setCanceledOnTouchOutside(false)
        progreessDialog.show()

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                saveUserInfo(name, email, progreessDialog)
                var business = intent.extras?.getBoolean("isManager")
                if (business == true) {
                    saveBusinessInfo(name, email)
                }
            } else {
                val message = task.exception!!.toString()
                Toast.makeText(
                    this,
                    "Error: $message",
                    Toast.LENGTH_LONG
                ).show()
                mAuth.signOut()
                progreessDialog.dismiss()
            }
        }
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btn_sign_up.isEnabled =
            edText_name_sign_up.text.trim().isNotEmpty()
                    && edText_email_sign_up.text.trim().isNotEmpty()
                    && edText_password_sign_up.text.trim().isNotEmpty()

    }

    override fun afterTextChanged(s: Editable?) {
    }

    private fun saveUserInfo(


        userName: String,
        email: String,
        progreessDialog: ProgressDialog
    ) {
        var business = intent.extras?.getBoolean("isManager")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")


        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "hey i am using this app"
        if (business == true) {
            userMap["Manager"] = true
        }

        if (business == false) {


            userMap["Manager"] = false
        }

        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/an-appointment-fdf7a.appspot.com/o/profile%2Fprofile.png?alt=media&token=463899a7-fbdf-49c6-ab40-1d7764aa8299"



        usersRef.child(currentUserId).setValue(userMap)


            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progreessDialog.dismiss()
                    Toast.makeText(
                        this,
                        "Account has been created successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    if (business == true) {
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else if (business == false) {
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(
                        this,
                        "Error: $message",
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    progreessDialog.dismiss()
                }
            }


    }

    fun saveBusinessInfo(
        userName: String,
        email: String
    ) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Business").child(x.toString())
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "hey i am using this app"
        userMap["work"] = x.toString()
        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/an-appointment-fdf7a.appspot.com/o/profile%2Fprofile.png?alt=media&token=463899a7-fbdf-49c6-ab40-1d7764aa8299"

        usersRef.child(currentUserId).setValue(userMap)


    }


}

