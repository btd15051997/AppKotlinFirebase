package com.example.myapplication.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.Model.Users

import com.example.myapplication.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.lang.Exception


class SettingFragment : Fragment() {

    var mainActivity : MainActivity? = null

    var refUser : DatabaseReference? = null
    var storeRef : StorageReference? = null

    var txt_name_setting : TextView? = null
    var txt_status_setting : TextView? = null
    var txt_email_setting : TextView? = null
    private var img_cover_setting : ImageView? = null
    private var img_avt_setting : CircleImageView? = null

    var coverChecker : String? = ""

    var imageUri : Uri? = null

     val REQUEST_CODE : Int  = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_setting, container, false)

        txt_name_setting = view.findViewById(R.id.txt_name_setting)
        txt_status_setting = view.findViewById(R.id.txt_status_setting)
        txt_email_setting = view.findViewById(R.id.txt_email_setting)
        img_cover_setting = view.findViewById(R.id.img_cover_setting)
        img_avt_setting = view.findViewById(R.id.img_avt_setting)

        storeRef = FirebaseStorage.getInstance().reference.child("User Images")

        val userId : String = FirebaseAuth.getInstance().currentUser!!.uid

        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        refUser!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    val users : Users? = p0.getValue(Users::class.java)

                    txt_name_setting!!.text = users!!.getName()
                    txt_email_setting!!.text = users!!.getEmail()
                    txt_status_setting!!.text = users!!.getStatus()

                    if (context!= null){
                        try {


                            if (users.getProfile() == ""){
                                txt_imghome.setBackgroundResource(R.drawable.ic_launcher_background);
                            }else{
                                Log.d("Setting","getInfo : ${users.getCover().toString()} :Profile ${users.getProfile().toString()}")
                                Picasso.get().load(users.getProfile()).placeholder(R.drawable.ic_launcher_background).into(view.img_avt_setting)
                            }

                            if (users.getCover() == ""){
                                txt_imghome.setBackgroundResource(R.drawable.ic_launcher_background);
                            }else{
                                Log.d("Setting","getInfo : ${users.getCover().toString()} :Profile ${users.getProfile().toString()}")
                                Picasso.get().load(users.getCover()).placeholder(R.drawable.ic_launcher_background).into(view.img_cover_setting)
                            }


                        }catch (e : Exception){


                        }
                    }
                }
            }
        })

        view.img_avt_setting.setOnClickListener {

            coverChecker = "profileAvtImage"
            onPickImage()

        }

        return view
    }

    private fun onPickImage() {

        val intent = Intent()
        intent.type ="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data!!.data != null){

            if (data!!.data != null){

                imageUri = data.data

                Toast.makeText(context,"Uploading....",Toast.LENGTH_LONG).show()

                onUploadImageToDatabase()

            }

        }

    }

    private fun onUploadImageToDatabase() {

        val progressBar = ProgressDialog(context)
        progressBar.setMessage("image is uploading, please wait...")
        progressBar.show()

        if (imageUri!=null){

            val fileRef = storeRef!!.child(System.currentTimeMillis().toString()+".jpg")

            var uploadTask : StorageTask<*>

            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{
                
                task ->

                if (task.isSuccessful){

                    task.exception?.let {

                        throw it
                    }
                }

                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->

                if(task.isSuccessful){

                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if(coverChecker == "profileAvtImage"){
                        Log.d("Datbt","onUploadImageToDatabase : profileAvtImage")
                        val mapCoverImg = HashMap<String,Any> ()
                        mapCoverImg["profile"] = url
                        refUser!!.updateChildren(mapCoverImg)
                        coverChecker = ""

                    }else{
                        Log.d("Datbt","onUploadImageToDatabase : coverImage")
                        val mapProfileImg = HashMap<String,Any> ()
                        mapProfileImg["cover"] = url
                        refUser!!.updateChildren(mapProfileImg)
                        coverChecker = ""

                    }
                    progressBar.dismiss()

                }

            }



        }else{


        }

    }

}
