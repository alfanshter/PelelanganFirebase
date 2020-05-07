package com.alfanshter.udinlelangfix

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.alfanshter.udinlelangfix.Auth.Login
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.alfanshter.udinlelangfix.admin.DataPelelang
import com.alfanshter.udinlelangfix.admin.Infolelang
import com.alfanshter.udinlelangfix.ui.Info.InfoDevelop
import com.alfanshter.udinlelangfix.ui.dashboard.DashboardFragment
import com.alfanshter.udinlelangfix.ui.Like.HomeFragment
import com.alfanshter.udinlelangfix.ui.Lokasi.Maps
import com.alfanshter.udinlelangfix.ui.money.MoneyFragment
import com.alfanshter.udinlelangfix.ui.notifications.NotificationsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_menu.*
import kotlinx.android.synthetic.main.drawer_header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HomeMenu : AppCompatActivity() {
    private lateinit var mainPresenter: MainPresenter
    private var mAuth: FirebaseAuth? = null
    lateinit var referencebaru: DatabaseReference
    private var mUserId: String? = null

    var namaprofil :String? = null
    var emailprofil:String? = null
    var gambarprofil :String? = null


    lateinit var sessionManager : SessionManager
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, DashboardFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, HomeFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_maps -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, Maps()
                ).commit()
                return@OnNavigationItemSelectedListener true

            }

            R.id.navigation_notifications -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame,
                    NotificationsFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menu)

        mAuth = FirebaseAuth.getInstance()
        mUserId = mAuth!!.currentUser!!.uid

        sessionManager = SessionManager(this)
        infonama()
        setSupportActionBar(dashboar_toolbar)

        val actionBar = supportActionBar
        actionBar?.title = "Pelelangan Udang"

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        drawerlayout()

        moveToFragment(DashboardFragment())




    }


    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frame, fragment)
        fragmentTrans.commit()
    }

    fun drawerlayout()
    {
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            container,
            dashboar_toolbar,
                 R.string.drawer_open,
            R.string.drawer_close
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        container.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_logout -> {
                    toast("halo")
                            mAuth!!.signOut()
                            sessionManager.setLogin(false)
                            startActivity<Login>()
                            finish()

                }

                R.id.settings -> {

                }

                R.id.info_drawable -> {
                  startActivity<Infolelang>()
                }

                R.id.nav_saldo ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        MoneyFragment()
                    ).commit()
                }




            }
            container.closeDrawer(GravityCompat.START)
            true

        }

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    fun infonama(){
        referencebaru = FirebaseDatabase.getInstance().reference.child("udang").child("Users").child(mUserId.toString())
        referencebaru.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var ambildata = p0.getValue(Produk::class.java)
                namaprofil = ambildata!!.name.toString()
                emailprofil = ambildata.email.toString()
                gambarprofil = ambildata.foto.toString()

                val placeholderOption =
                    RequestOptions()


                if (nama_drawer!=null)
                {
                    nama_drawer.text = namaprofil
                    Glide.with(container.context).setDefaultRequestOptions(placeholderOption).load(gambarprofil).into(gambardrawer)
                    email_drawer.text = emailprofil

                }


              }

        })
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk  keluar aplikasi", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

    }

}
