package com.devrakan.rshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.UserAdapterChat
import com.devrakan.rshop.Model.ChatList
import com.devrakan.rshop.Model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowMessageActivity : AppCompatActivity() {
    private var userAdapter: UserAdapterChat? = null
    private var mUser: List<Users>? = null
    private var usersChatList: List<ChatList>? = null
    lateinit var recycler_view_chatlist: RecyclerView
    private var firebaseUser : FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_message)

        recycler_view_chatlist = findViewById(R.id.recycler_view_chatlist)
        recycler_view_chatlist.setHasFixedSize(true)
        recycler_view_chatlist.layoutManager = LinearLayoutManager(this)


        firebaseUser = FirebaseAuth.getInstance().currentUser

        usersChatList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (usersChatList as ArrayList).clear()

                for (dataSnapshot in snapshot.children){
                    val chatlist = dataSnapshot.getValue(ChatList::class.java)
                    (usersChatList as ArrayList).add(chatlist!!)
                }
                retrieveChatLists()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }
    private fun retrieveChatLists() {
        mUser = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUser as ArrayList).clear()
                for (dataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(Users::class.java)
                    for (eachChatList in usersChatList!!){
                        if (user!!.getUID().equals(eachChatList.getId())){
                            (mUser as ArrayList).add(user!!)
                        }
                    }
                }
                userAdapter = UserAdapterChat(this@ShowMessageActivity,(mUser as ArrayList<Users>),true)
                recycler_view_chatlist.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}