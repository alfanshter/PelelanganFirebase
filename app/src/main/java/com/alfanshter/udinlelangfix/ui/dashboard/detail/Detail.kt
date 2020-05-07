@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.alfanshter.udinlelangfix.ui.dashboard.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class Detail : AppCompatActivity(), AnkoLogger {
    var dialog_bidding: Dialog? = null
    var tertinggi = 0
    var EXTRA_ID = "extra_id"
    lateinit var sessionManager: SessionManager
    var key: String? = null
    var owner: String? = null
    var name: String? = null
    var id: String? = null
    var nilai: String? = null
    var price: String? = null
    var gambar: String? = null
    var hari: String? = null
    var deskripsi: String? = null
    var namapemenang: String? = null
    lateinit var dbaselelang: DatabaseReference
    private var mDatabase: DatabaseReference? = null
    var logiclike = false
    var namalelang = ""
    var nilailelang : Int? = null
    var namamember = ""
    var saldo: Int? = null
    lateinit var reference: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        sessionManager = SessionManager(this)
        dialog_bidding = Dialog(this)
        dialog_bidding!!.setContentView(R.layout.layout_lelang)

        val proseslelanglayout = findViewById<View>(R.id.lyt_proseslelang) as ConstraintLayout
        val lelangselesailayout = findViewById<View>(R.id.lyt_lelangselesai) as ConstraintLayout

        mDatabase = FirebaseDatabase.getInstance().reference
        val key_user = FirebaseAuth.getInstance().currentUser
        val tes = key_user!!.uid
        key = tes

        val bundle: Bundle? = intent.extras
        gambar = bundle!!.getString("gambar")
        deskripsi = bundle.getString("deskripsi")
        price = bundle.getString("price")
        owner = bundle.getString("owner")
        name = bundle.getString("name")
        id = bundle.getString("id")
        nilai = bundle.getString("nilai")
        namapemenang = bundle.getString("nameMember")
        hari = bundle.getString("hari")
        ambildata()
        nilaibaru()

        if (nilai.equals("1")) {
            proseslelanglayout.visibility = View.INVISIBLE
            lelangselesailayout.visibility = View.VISIBLE
            txt_nama.setText("Nama Pemenang : $namapemenang")
            txt_barang.setText("Barang : $name")
            txt_penjual.setText("Penjual : $owner")

        } else if (nilai.equals("2")) {
            proseslelanglayout.visibility = View.VISIBLE
            lelangselesailayout.visibility = View.INVISIBLE
        }

        Picasso.get().load(gambar).into(foto)
        txt_waktu.text = "batas waktu $hari"
        namaproduk.text = "Nama Produk : ${name.toString()}"
        text_owner.text = "Nama Penjual : ${owner.toString()}"
        text_deskripsi.text = "Deskripsi : ${deskripsi.toString()}"

        if (id != null) {
            var dbaselike =
                FirebaseDatabase.getInstance().reference.child("users").child(tes).child("like")
                    .child(id!!)
            dbaselike.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        logiclike = true
                        btn_like.setImageResource(R.drawable.ic_favorite_red_24dp)
                    } else {
                        logiclike = false
                        btn_like.setImageResource(R.drawable.ic_favorite_black_24dp)
                    }
                }

            })

        }

        recyclerviewfun()

        btn_lelang.setOnClickListener {
            showPopUp()
        }

        btn_like.setOnClickListener {

            if (logiclike == false) {
                like()
                logiclike = true
            } else if (logiclike == true) {
                dislike()
                logiclike = false
            }

        }
    }

    private fun nilaibaru() {
        var ambilnilai = FirebaseDatabase.getInstance().reference.child("products").child(id.toString())
            .child("price")
        ambilnilai.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                nilailelang = p0.value!!.toString().toInt()
                tertinggi = nilailelang!!.toInt()
                harga.text = "Rp. ${nilailelang.toString()}"
                txt_harga.setText("Rp. $nilailelang")

            }
        })

    }


    private fun showPopUp() {
        val textClose: TextView
        val amount_bidding: EditText
        val btnBidding: Button
        amount_bidding = dialog_bidding!!.findViewById(R.id.edt_harga)
        textClose = dialog_bidding!!.findViewById(R.id.txtclose)
        btnBidding = dialog_bidding!!.findViewById(R.id.btn_lelang)

        var userID =""
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid


        btnBidding.setOnClickListener {
            amount_bidding.error = null
            var cancel = false
            var focusView: View? = null
            var logic =0
            var database = FirebaseDatabase.getInstance().reference.child("udang").child("Users")
                .child(userID).child("saldo")
            database.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    saldo = p0.value!!.toString().toInt()
                    var nilaisaldo = saldo
                    if (TextUtils.isEmpty(amount_bidding.text.toString())) {
                        amount_bidding.error = getString(R.string.error_field_required)
                        focusView = amount_bidding
                        cancel = true
                    }
                    else if (amount_bidding.text.toString().toInt() > nilaisaldo!!) {
                        amount_bidding.error = "Saldo tidak cukup"
                        focusView = amount_bidding
                        cancel = true
                    }

                    else if (amount_bidding.text.toString().toInt() <= tertinggi)
                    {
                        amount_bidding.error = "Masukkan angka yang lebih besar"
                        focusView = amount_bidding
                        cancel = true
                    }



                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView!!.requestFocus()
                    } else {
                        val auth = FirebaseAuth.getInstance()
                        val userid = auth.currentUser!!.uid
                        val hopperRef: DatabaseReference = mDatabase!!.child("products").child(id!!)
                        val hopperRefdaftar: DatabaseReference =
                            mDatabase!!.child("products").child(id!!).child("pelelang").child(userid)
                        val daftarlelang: MutableMap<String, Any> = HashMap()
                        daftarlelang["namalelang"] = namalelang
                        daftarlelang["price"] = amount_bidding.text.toString().toInt()
                        daftarlelang["image"] = gambar!!
                        daftarlelang["namabarang"] = name!!
                        hopperRefdaftar.updateChildren(daftarlelang).addOnSuccessListener {

                        }
                        val hopperUpdates: MutableMap<String, Any> =
                            HashMap()
                        hopperUpdates["price"] = amount_bidding.text.toString().toInt()
                        hopperUpdates["idMember"] = key!!
                        hopperUpdates["nameMember"] = namalelang
                        hopperRef.updateChildren(hopperUpdates).addOnSuccessListener {
                            Toast.makeText(this@Detail, "Success", Toast.LENGTH_SHORT)
                                .show()
                            dialog_bidding!!.dismiss()
                        }
                    }
                }

            })
            // Check for a valid password, if the user entered one.


        }

        textClose.setOnClickListener { dialog_bidding!!.dismiss() }
        dialog_bidding!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_bidding!!.show()

    }

    fun recyclerviewfun() {
        //     ===============  RecyclerView =========
        val LayoutManager = LinearLayoutManager(this)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rcyr_daftarlelang.layoutManager = LayoutManager

        dbaselelang =
            FirebaseDatabase.getInstance().reference.child("products").child(id!!).child("pelelang")
        val option =
            FirebaseRecyclerOptions.Builder<Produk>().setQuery(dbaselelang, Produk::class.java)
                .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<Produk, Detail.MyViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): Detail.MyViewHolder {
                    val itemView = LayoutInflater.from(this@Detail)
                        .inflate(R.layout.daftarpelelang, parent, false)
                    return Detail.MyViewHolder(itemView)
                }

                override fun onBindViewHolder(
                    holder: Detail.MyViewHolder,
                    position: Int,
                    model: Produk
                ) {
                    val refid = getRef(position).key.toString()
                    dbaselelang.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(p0: DataSnapshot) {
                            holder.mnama.text = "Nama : ${model.namalelang}"
                            holder.mharga.text = "Harga : ${model.price}"
                            holder.mbarang.text = "barang : ${model.namabarang.toString()}"
                            Picasso.get().load(model.image).fit()
                                .placeholder(R.drawable.ic_launcher_background).into(holder.mgambar)
                        }

                    })
                }
            }


        rcyr_daftarlelang.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

//    ===============  RecyclerView ===============
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mnama: TextView = itemView.findViewById(R.id.namapelelang)
        var mgambar: ImageView = itemView.findViewById(R.id.gambarpelelang)
        var mharga: TextView = itemView.findViewById(R.id.hargalelang)
        var mbarang: TextView = itemView.findViewById(R.id.txt_namabarang)
    }

    /*    gambar = bundle!!.getString("gambar")
        val deskripsi = bundle.getString("deskripsi")
        price = bundle.getString("price")
        owner = bundle.getString("owner")
        name = bundle.getString("name")
        id = bundle.getString("id")
        nilai = bundle.getString("nilai")
        namapemenang = bundle.getString("nameMember")
        hari = bundle.getString("hari")*/
    fun like() {
        val key_user = FirebaseAuth.getInstance().currentUser
        val uid = key_user!!.uid
        val reference =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("like")
                .child(id!!)
        val like: MutableMap<String, Any> = HashMap()
        like["description"] = deskripsi!!
        like["price"] = price!!.toInt()
        like["owner"] = owner!!
        like["name"] = name!!
        like["id"] = id!!
        like["nilai"] = nilai!!
        like["nameMember"] = namalelang!!
        like["hari"] = hari!!

        like["like"] = "true"
        like["image"] = gambar!!
        like[""]
        reference.setValue(like)
        btn_like.setImageResource(R.drawable.ic_favorite_red_24dp)
    }

    fun dislike() {
        val key_user = FirebaseAuth.getInstance().currentUser
        val uid = key_user!!.uid
        val reference =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("like")
                .child(id!!)
        reference.removeValue()
        btn_like.setImageResource(R.drawable.ic_favorite_black_24dp)
    }

    fun ambildata() {
        var auth = FirebaseAuth.getInstance().currentUser!!.uid
        var reference = FirebaseDatabase.getInstance().reference.child("udang").child("Users")
            .child(auth)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                namalelang = p0.child("name").value!!.toString()
                var foto = p0.child("foto").value!!.toString()
                Picasso.get().load(foto).fit().into(img_fotopemenang)
                info { "hasil ${namalelang}" }
            }

        })
    }

}
