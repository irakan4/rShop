package com.devrakan.rshop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.devrakan.rshop.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Users").child(currentUserId).child("Manager")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var business: Boolean = snapshot.getValue(Boolean::class.java)!!
                if (business == true) {
                    setUpManger()

                } else if (business == false) {

                    setUpUser()

                }
                
            }

            override fun onCancelled(error: DatabaseError) {

                val error = error.message
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
            }

        })

    }

    fun setUpUser() {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.container.visibility = View.VISIBLE
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

    }

    fun setUpManger() {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navViewM

        binding.container2.visibility = View.VISIBLE

        val navController = findNavController(R.id.nav_host_fragment_activity_main_m)

        navView.setupWithNavController(navController)
    }
}