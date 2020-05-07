package com.alfanshter.udinlelangfix.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.udinlelangfix.Auth.Login
import com.alfanshter.udinlelangfix.R
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu_admin.*
import org.jetbrains.anko.startActivity

class MenuAdmin : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    private var mUserId: String? = null
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)
        sessionManager = SessionManager(this)
        mAuth = FirebaseAuth.getInstance()
        mUserId = mAuth!!.currentUser!!.uid

        btn_pemenanglelang.setOnClickListener {
            startActivity<PemenangLelangAdmin>()
        }
        btn_homedatapelelangan.setOnClickListener {
            startActivity<DataPelelang>()
        }
        logoutadmin.setOnClickListener {
            if (mUserId != null) {
                mAuth!!.signOut()
                sessionManager.setLoginadmin(false)
                startActivity<Login>()
                finish()
            }

        }

        btn_homelelang.setOnClickListener {
            startActivity<UploadAdmin>()
        }

        btn_timbangan.setOnClickListener {
            startActivity<Timbangan>()
        }

        btn_info.setOnClickListener {
            startActivity<Infolelang>()
        }











    }
}
