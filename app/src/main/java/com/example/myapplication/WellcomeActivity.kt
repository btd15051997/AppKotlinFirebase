package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_wellcome.*

class WellcomeActivity : AppCompatActivity() {

    var firebase: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellcome)

        btn_getlogin.setOnClickListener {

            startActivity(Intent(this@WellcomeActivity, LoginActivity::class.java))
            finish()

        }

        btn_getRegister.setOnClickListener {

            startActivity(Intent(this@WellcomeActivity, SignUpActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        firebase = FirebaseAuth.getInstance().currentUser

//        if (firebase != null) {
//
//            startActivity(Intent(this@WellcomeActivity, LoginActivity::class.java))
//            finish()
//
//        }

    }
}
