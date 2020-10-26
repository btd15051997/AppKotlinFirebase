package com.example.myapplication.adpater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Users
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


class UserAdapter(
    mContext: Context,
    mUsers: List<Users>,
    isChatCheck: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHodel?>() {

    private var mContext: Context
    private var mUsers: List<Users>
    private var isChatCheck: Boolean

    init {
        this.mContext = mContext
        this.mUsers = mUsers
        this.isChatCheck = isChatCheck

    }


    class ViewHodel(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var userName: TextView
        var profileImage: CircleImageView

        init {

            userName = itemview.findViewById(R.id.item_name_user)
            profileImage = itemview.findViewById(R.id.item_img_user)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {

        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.item_search_users, parent, false)

        return UserAdapter.ViewHodel(view)
    }

    override fun getItemCount(): Int {


        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {

        var users: Users = mUsers[position]

        holder.userName.text = users.getName()

        Log.d("Log_Ne", users.getName()+"")

        try {

            Picasso.get().load(users.getProfile()).placeholder(R.drawable.ic_launcher_background)
                .into(holder.profileImage)

        } catch (e: Exception) {


        }

    }
}