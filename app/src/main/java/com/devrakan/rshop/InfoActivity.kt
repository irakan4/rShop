package com.devrakan.rshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devrakan.rshop.Model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val x = intent.extras?.getString("user")


        val database = FirebaseDatabase.getInstance()
        val id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref2 = database.getReference("Users").child(x.toString())
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: Users = snapshot.getValue(Users::class.java)!!
                Picasso.get().load(user.getImage()).into(info_Image)
                info_user_name.text = user.getUsername()
                info_phone_number.text = user.getPhone()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        info_btn_view.setOnClickListener {

            val intent = Intent(this,WorkActivity::class.java)
            intent.putExtra("work",x)
            startActivity(intent)
        }

        info_btn_chat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("visit_id",x)
            startActivity(intent)
        }


    }
}