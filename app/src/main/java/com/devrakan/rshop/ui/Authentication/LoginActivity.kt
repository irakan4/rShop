package com.devrakan.rshop.ui.Authentication


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.devrakan.rshop.MainActivity
import com.devrakan.rshop.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.manager_dialog.view.*

class LoginActivity : AppCompatActivity(), TextWatcher {
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edText_email_sign_in.addTextChangedListener(this@LoginActivity)
        edText_password_sign_in.addTextChangedListener(this@LoginActivity)

        btn_sign_in.setOnClickListener {


            var email = edText_email_sign_in.text.trim().toString()
            var password = edText_password_sign_in.text.trim().toString()

            if (email.isEmpty()) {
                edText_email_sign_in.error = "Email Required"
                edText_email_sign_in.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edText_email_sign_in.error = "Please Enter a valid email"
                edText_email_sign_in.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 8) {
                edText_password_sign_in.error = " 8 char required"
                edText_password_sign_in.requestFocus()
                return@setOnClickListener
            }
            signIn(email, password)
        }


        tv_create_account.setOnClickListener {
            showCustomAlert()
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth?.currentUser != null) {
            intent()
        }

    }


    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                intent()
            }


        }
    }


    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.manager_dialog, null)
        val customDialog = AlertDialog.Builder(this)
        customDialog.setTitle("Choose an account")
        customDialog.setMessage("Create an account for yourself or your business")
            .setView(dialogView)
            .show()

        val forMe = dialogView.findViewById<Button>(R.id.btn_forme)
        forMe.setOnClickListener {
            var intentSignUpActivity = Intent(this, SignUpActivity::class.java)
            intentSignUpActivity.putExtra("isManager", false)
            startActivity(intentSignUpActivity)
        }

        val forMyBusiness = dialogView.findViewById<Button>(R.id.btn_forMyBusiness)
        forMyBusiness.setOnClickListener {

            var intentSignUpBusinessActivity = Intent(this, SignUpActivity::class.java)
            intentSignUpBusinessActivity.putExtra("isManager", true)
            startActivity(intentSignUpBusinessActivity)

        }


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btn_sign_in.isEnabled = edText_email_sign_in.text.trim().toString().isNotEmpty() &&
                edText_password_sign_in.text.trim().toString().isNotEmpty()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun intent() {


        val intentMainActivity =
            Intent(this@LoginActivity, MainActivity::class.java)
        intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intentMainActivity)


    }


}