package com.devrakan.rshop


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var login = LoginFragment()
        var register = RegisterFragment()

        // change the color of btn login //
        btn_login_login.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))

        // make the login fragment the first fragment //
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,login,"Login")
            .add(R.id.fragment_container,register,"Register")
            .show(login)
            .hide(register)
            .commit()
        // btn login . On Click , && show login fragment, hide register fragment, change the btn color & toolbar title//
        // and also change the color of btn registerColor
        btn_login_login.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .show(login)
                .hide(register)
                .commit()
            toolbar.text = "Login"
            btn_login_login.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))
            btn_login_register.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_700))
        }
        // btn register . On Click , && show register fragment, hide login fragment, change the btn color & toolbar title//
        // and also change the color of btn registerColor

        btn_login_register.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .show(register)
                .hide(login)
                .commit()
            btn_login_login.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_700))
            btn_login_register.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))

        }
    }
}