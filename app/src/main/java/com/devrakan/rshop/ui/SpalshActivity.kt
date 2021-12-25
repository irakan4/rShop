package com.devrakan.rshop.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.devrakan.rshop.R
import com.devrakan.rshop.ui.Authentication.LoginActivity
import kotlinx.android.synthetic.main.activity_spalsh.*

class SpalshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        // this is Splash in , in logo //
        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splah_in))
        Handler().postDelayed({
            //  Splash Out //
            ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_out))

            Handler().postDelayed({

                // hide logo :) //
                ic_logo.visibility = View.GONE

                // to start login Activity //
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // to close the Splash activity //
            }, 1000)


        }, 1500)
    }
}