package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private var firebaseUser : FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar : Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title ="Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        toolbar.setNavigationOnClickListener {

            finish()

        }

        btn_login.setOnClickListener {

           onHandleLogin()

        }
    }

    private fun onHandleLogin() {


        val email :String = edt_login_email.text.toString().trim()
        val pass :String = edt_login_pass.text.toString()

        when {
            email == "" -> {

                Toast.makeText(this@LoginActivity,"Pls Enter Name", Toast.LENGTH_SHORT).show()

                return

            }
            pass == "" -> {

                Toast.makeText(this@LoginActivity,"Pls Enter Pass", Toast.LENGTH_SHORT).show()

                return

            }
            else -> {

                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {

                    task ->

                    if (task.isSuccessful){

                        Toast.makeText(this@LoginActivity,"Success", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()

                    }else{


                        Toast.makeText(this@LoginActivity,"Error "+task.exception.toString(), Toast.LENGTH_SHORT).show()

                    }

                }.addOnCanceledListener {



                }

            }
        }
    }

    override fun onStart() {
        super.onStart()

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null){

            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            this.finish()

        }else{

        }

    }
}
