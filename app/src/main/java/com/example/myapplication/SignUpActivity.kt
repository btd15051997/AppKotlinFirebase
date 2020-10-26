package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_wellcome.*
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var  refUser : DatabaseReference
    private var userUid : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolbar_register : Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar_register)
        supportActionBar!!.title ="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_register.setNavigationOnClickListener {

            finish()

        }

        btn_register.setOnClickListener {

            onHandleRegister()

        }
    }

    override fun onStart() {
        super.onStart()

        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun onHandleRegister() {

        val  name : String  = edt_register_name.text.toString().trim()
        val  email : String  = edt_register_email.text.toString().trim()
        val  password : String  = edt_register_password.text.toString().trim()

        when {

            name == "" -> {

                Toast.makeText(this@SignUpActivity,"Pls Enter Name",Toast.LENGTH_SHORT).show()

                return

            }
            email == "" -> {

                Toast.makeText(this@SignUpActivity,"Pls Enter Email",Toast.LENGTH_SHORT).show()

                return

            }
            password == "" -> {

                Toast.makeText(this@SignUpActivity,"Pls Enter Password",Toast.LENGTH_SHORT).show()

                return

            }

            else -> {


                firebaseAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

                    task ->

                    if (task.isSuccessful){

                        Toast.makeText(this@SignUpActivity,"SignUp Success!",Toast.LENGTH_SHORT).show()

                        userUid = firebaseAuth!!.currentUser!!.uid

                        //create kotlin
                        val hashmap : HashMap<String,Any> = HashMap()

                        hashmap["uid"] = userUid
                        hashmap["name"] = name
                        hashmap["email"] = email
                        hashmap["search"] = name.toLowerCase()
                        hashmap["status"] = "offline"
                      //  hashmap["profile"] = "https://firebasestorage.googleapis.com/v0/b/appkotlin-7a567.appspot.com/o/profile.png?alt=media&token=a3dd71b9-5008-42dd-a596-ea4aa751132f"
                       // hashmap["cover"] = "https://firebasestorage.googleapis.com/v0/b/appkotlin-7a567.appspot.com/o/profile.png?alt=media&token=a3dd71b9-5008-42dd-a596-ea4aa751132f"
                        hashmap["profile"] = ""
                        hashmap["cover"] = ""

                        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(userUid)

                        refUser.updateChildren(hashmap)
                            .addOnCompleteListener {

                                task ->

                                if (task.isSuccessful){

                                    val intent : Intent = Intent(this@SignUpActivity,MainActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()

                                }

                            }


                    }else{

                        Toast.makeText(this@SignUpActivity,"Error "+ task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                    }

                }.addOnCanceledListener {


                }

            }
        }

    }
}
