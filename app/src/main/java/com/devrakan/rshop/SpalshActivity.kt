package com.devrakan.rshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_spalsh.*

class SpalshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        // this is Splash in , in logo //
        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splah_in))
        Handler().postDelayed({
            // this is Splash Out //
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