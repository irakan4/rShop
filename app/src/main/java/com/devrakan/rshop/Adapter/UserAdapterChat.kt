package com.devrakan.rshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devrakan.rshop.ChatActivity
import com.devrakan.rshop.Model.Users
import com.devrakan.rshop.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapterChat(
    mContext: Context,
    mUser: List<Users>,
    isChatCheck: Boolean
) : RecyclerView.Adapter<UserAdapterChat.ViewHolder?>() {
    private val mContext: Context
    private val mUser: List<Users>
    private var isChatCheck: Boolean

    init {
        this.mUser = mUser
        this.mContext = mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, parent, false)
        return UserAdapterChat.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users = mUser[position]
        holder.userNameTxt.text = user!!.getUsername()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.person)
            .into(holder.profileImageView)
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("visit_id", user.getUID())
            mContext.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var userNameTxt: TextView
        var profileImageView: CircleImageView
        var onlineTxt: CircleImageView
        var ofllineTxt: CircleImageView
        var lastMessageTxt: TextView

        init {
            userNameTxt = itemView.findViewById(R.id.username_chat_search)
            profileImageView = itemView.findViewById(R.id.profile_image_chat_search)
            onlineTxt = itemView.findViewById(R.id.image_online)
            ofllineTxt = itemView.findViewById(R.id.image_offline)
            lastMessageTxt = itemView.findViewById(R.id.message_last)

        }

    }


}