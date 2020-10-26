package com.example.myapplication.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.Model.Users

import com.example.myapplication.R
import com.example.myapplication.adpater.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    var mainActivity : MainActivity? = null
    private var mUserAdapter : UserAdapter? = null
    private var mUserList : List<Users>? = null
    private var recyclerView : RecyclerView? = null
    private var edt_search : EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity?

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView  = view.findViewById(R.id.recycview_search)
        edt_search  = view.findViewById(R.id.edt_search)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        mUserList = ArrayList()

        onLoadAllUsers()

        edt_search!!.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                var string : String = s.toString()

                Log.d("Log_Ne", string.toString())

                var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

                val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

                refUsers!!.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        (mUserList as ArrayList<Users>).clear()

                        for (snapshot in p0.children){

                            val users : Users? = snapshot.getValue(Users::class.java)

                            if (!(users!!.getUid()).equals(firebaseUserID)){

                                if (users.getSearch().contains(string)){

                                    (mUserList as ArrayList<Users>).add(users!!)


                                }

                            }

                            mUserAdapter = UserAdapter(context!!, mUserList!!,false)
                            recyclerView!!.adapter = mUserAdapter
                        }

                    }
                })

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



            }


        })



        return view
    }

    private fun onLoadAllUsers() {


        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                (mUserList as ArrayList<Users>).clear()

                for (snapshot in p0.children){

                    val users : Users? = snapshot.getValue(Users::class.java)

                    if (!(users!!.getUid()).equals(firebaseUserID)){

                        (mUserList as ArrayList<Users>).add(users!!)

                        Log.d("Log_Ne", (mUserList as ArrayList<Users>).size.toString())


                    }

                    mUserAdapter = UserAdapter(context!!, mUserList!!,false)
                    recyclerView!!.adapter = mUserAdapter
                }

            }
        })

    }


}
