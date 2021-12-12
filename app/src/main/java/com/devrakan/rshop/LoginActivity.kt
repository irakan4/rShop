package com.devrakan.rshop


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var login = LoginFragment()
        var register = RegisterFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,login,"Login")
            .add(R.id.fragment_container,register,"Register")
            .show(login)
            .hide(register)
            .commit()
        btn_login_login.setOnClickListener {
            supportFragmentManager
        }
    }
}