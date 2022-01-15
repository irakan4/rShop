package com.devrakan.rshop
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.Adapter.ChatsAdapter
import com.devrakan.rshop.Model.Chat
import com.devrakan.rshop.Model.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var chatsAdapter: ChatsAdapter? = null
    var mChateList: List<Chat>? = null
    var reference: DatabaseReference? = null
    lateinit var recycler_view_chats: RecyclerView
    var notify = false
    var userIdVisit: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        userIdVisit = intent.extras?.getString("visit_id").toString()

        recycler_view_chats = findViewById(R.id.recycler_view_chats)
        recycler_view_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycler_view_chats.layoutManager = linearLayoutManager

        reference = FirebaseDatabase.getInstance().reference.child("Users").child(userIdVisit!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user: Users? = snapshot.getValue(Users::class.java)
                username_message_chat.text = user!!.getUsername()
                Picasso.get().load(user!!.getImage()).into(profile_image_message_chat)

                retrieveMessages(firebaseUser!!.uid, userIdVisit!!, user!!.getImage())

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        send_message_btn.setOnClickListener {
            notify = true
            val message = text_message.text.toString()
            if (message == "") {
                Toast.makeText(this, "Please write a message, first...", Toast.LENGTH_LONG).show()
            } else {
                sendMessageToUser(firebaseUser!!.uid, userIdVisit!!, message)
            }
            text_message.setText("")
        }
        attact_image_file_btn.setOnClickListener {
            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Pick Image"), 438)
        }

        seenMeassage(userIdVisit!!)
    }


    private fun sendMessageToUser(senderId: String, receiverId: String?, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey
        reference.child("Chats").child(messageKey!!).setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chatListReference =
                        FirebaseDatabase.getInstance().reference.child("ChatLists")
                            .child(firebaseUser!!.uid).child(userIdVisit!!)
                    chatListReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (!snapshot.exists()) {
                                chatListReference.child("id").setValue(userIdVisit!!)

                            }

                            val chatListReceiverRef =
                                FirebaseDatabase.getInstance().reference.child("ChatLists")
                                    .child(userIdVisit!!).child(firebaseUser!!.uid)
                            chatListReceiverRef.child("id").setValue(firebaseUser!!.uid)

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })


                }
            }
        val userReference = FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser!!.uid)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(Users::class.java)

                if (notify) {
                }
                notify = false

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data!!.data != null) {

            val loadingBar = ProgressDialog(this)
            loadingBar.setMessage("Please wait, image  is sending...")
            loadingBar.show()

            var fileUri = data.data
            var storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")

            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")
            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {

                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "sent you an image."
                    messageHashMap["receiver"] = userIdVisit!!
                    messageHashMap["isseen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId
                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadingBar.dismiss()
                                val reference =
                                    FirebaseDatabase.getInstance().reference.child("Users")
                                        .child(firebaseUser!!.uid)
                                reference.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {

                                        val user = snapshot.getValue(Users::class.java)

                                        if (notify) {

                                        }
                                        notify = false

                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            }
                        }
                }

            }
        }
    }

    private fun retrieveMessages(senderId: String, receiverId: String?, receiverImageUrl: String) {

        mChateList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mChateList as ArrayList<Chat>).clear()
                for (snapshot in snapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(senderId) && chat.getSender()
                            .equals(receiverId) || chat.getReceiver()
                            .equals(receiverId) && chat.getSender().equals(senderId)
                    ) {
                        (mChateList as ArrayList<Chat>).add(chat)

                    }
                    chatsAdapter = ChatsAdapter(
                        this@ChatActivity,
                        (mChateList as ArrayList<Chat>),
                        receiverImageUrl!!
                    )

                    recycler_view_chats.adapter = chatsAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    var seenListner: ValueEventListener? = null

    private fun seenMeassage(userId: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        seenListner = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && chat!!.getSender()
                            .equals(userId)
                    ) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isseen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(seenListner!!)
    }
}