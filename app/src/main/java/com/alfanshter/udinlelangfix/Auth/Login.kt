package com.alfanshter.udinlelangfix.Auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.alfanshter.udinlelangfix.HomeMenu
import com.alfanshter.udinlelangfix.R
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.alfanshter.udinlelangfix.admin.MenuAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Login : AppCompatActivity() {
    lateinit var progressdialog: ProgressDialog
    lateinit var userID: String
    lateinit var user: FirebaseUser
    lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sessionManager = SessionManager(this)

        progressdialog = ProgressDialog(this)

        login.setOnClickListener {
            login()
        }

        if (sessionManager.getLogin()!!) {
            startActivity<HomeMenu>()
            finish()
        }

        if (sessionManager.getLoginadmin()!!) {
            startActivity<MenuAdmin>()
            finish()
        }

        signup.setOnClickListener {
            startActivity<Register>()
        }

    }

    fun login() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sedang Login .....")
        progressDialog.show()
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Udang").child("Users")
        var userss = users.text.toString()
        var password = pass.text.toString()


        if (!TextUtils.isEmpty(userss) && !TextUtils.isEmpty(password)) {
            if (userss.equals("udin@udin.com") && password.equals("udin123"))
            {
                auth.signInWithEmailAndPassword(userss, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            sessionManager.setLoginadmin(true)
                            startActivity<MenuAdmin>()
                            progressDialog.dismiss()
                        }
                        else
                        {
                            toast("gagal login")

                        }
                    }

            }
            else if (!userss.equals("udin@udin.com") && !password.equals("udin123"))
            {
                auth.signInWithEmailAndPassword(userss, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            sessionManager.setLogin(true)
                            startActivity<HomeMenu>()
                            progressDialog.dismiss()
                        }
                        else
                        {
                            toast("gagal login")

                        }
                    }

            }



        }
        else {
            toast("masukkan username dan password")

        }

    }

}
