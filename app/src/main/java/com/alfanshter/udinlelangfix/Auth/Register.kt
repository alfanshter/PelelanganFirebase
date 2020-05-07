package com.alfanshter.udinlelangfix.Auth

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alfanshter.udinlelangfix.HomeMenu
import com.alfanshter.udinlelangfix.MainActivity
import com.alfanshter.udinlelangfix.R
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.HashMap

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var mAuth: FirebaseAuth? = null
    private var imageUri: Uri? = null
    private var myUrl = ""
    lateinit var databaseReference: DatabaseReference
    private var mStorage: StorageReference? = null
    private val PICK_IMAGE = 1
    lateinit var progressDialog : ProgressDialog
    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sessionManager = SessionManager(this)
        imageUri = null
        progressDialog = ProgressDialog(this)
        mStorage = FirebaseStorage.getInstance().reference.child("images")

        mAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        signup.setOnClickListener {
            daftar()
        }

        register_image_btn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),PICK_IMAGE
            )
        }

    }

    private fun daftar() {
        if (imageUri!=null){

            registerProgressBar.visibility = View.VISIBLE
            val email = email.text.toString().trim()
            val username = user.text.toString().trim()
            val password = pass.text.toString().trim()
            val nomor = mob.text.toString().trim()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && nomor.isNotEmpty()) {

                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user_id = mAuth!!.currentUser!!.uid
                            val user_profile = mStorage!!.child("$user_id.jpg") //filerife
                            var uploadTask : StorageTask<*>
                            uploadTask = user_profile.putFile(imageUri!!)
                            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                                if (!task.isSuccessful){
                                    task.exception?.let {
                                        throw  it
                                        registerProgressBar.visibility = View.INVISIBLE
                                    }
                                }
                                return@Continuation user_profile.downloadUrl
                            }).addOnCompleteListener(OnCompleteListener{
                                task ->
                                if (uploadTask.isSuccessful)
                                {
                                    val downloadUrl = task.result
                                    myUrl = downloadUrl.toString()

                                    val usermap : MutableMap<String,Any?> = HashMap()
                                    usermap["email"] = email
                                    usermap["name"] = username
                                    usermap["password"] = password
                                    usermap["nomor"] = nomor
                                    usermap["foto"] = myUrl


                                    databaseReference = FirebaseDatabase.getInstance().getReference("udang").child("Users")
                                    databaseReference.child(user_id).setValue(usermap)
                                    registerProgressBar.visibility = View.INVISIBLE
                                    sendToMain()

                                }
                                else{
                                    registerProgressBar.visibility = View.INVISIBLE
                                    Toast.makeText(
                                        this@Register,
                                        "Error : " + uploadTask.exception!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            })



                        }
                        else {
                            toast("gagal")
                            registerProgressBar.visibility = View.INVISIBLE

                        }

                    }


            }
        }

        else{
            toast("Upload foto terlebih dahulu")
        }

    }
    private fun sendToMain() {
        startActivity<HomeMenu>()
        sessionManager.setLogin(true)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE)
        {
            imageUri = data!!.data
            register_image_btn!!.setImageURI(imageUri)

        }
    }


}
