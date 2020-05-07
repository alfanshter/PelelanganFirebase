package com.alfanshter.udinlelangfix.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.udinlelangfix.Adapter.Adapter
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pemenang_lelang_admin.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class PemenangLelangAdmin : AppCompatActivity(), AnkoLogger {
    private lateinit var recyclerView: RecyclerView
    lateinit var refinfo: DatabaseReference
    private var adapter: Adapter? = null
    lateinit var artistList: ArrayList<Produk>

    companion object{
        var alfan : String? = null
    }
        var logic = 0
    var bulan: String? = null
    var hari: String? = null
    var jam: String? = null
    var menit: String? = null
    var seconds: String? = null
    lateinit var dialog: AlertDialog
    var refid: String? = null
    var myArray2 : MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemenang_lelang_admin)
        val auth = FirebaseAuth.getInstance()
        val userid = auth.currentUser!!.uid

        val LayoutManager = LinearLayoutManager(this)
        LayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_pemenanglelang.layoutManager = LayoutManager
        rv_pemenang.visibility = View.INVISIBLE

        refinfo = FirebaseDatabase.getInstance().reference.child("products")


        val option = FirebaseRecyclerOptions.Builder<Produk>().setQuery(refinfo, Produk::class.java)
            .build()
        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<Produk, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val itemView = LayoutInflater.from(this@PemenangLelangAdmin)
                        .inflate(R.layout.card_view, parent, false)
                    return MyViewHolder(itemView)

                }

                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Produk) {
                    val refid = getRef(position).key.toString()
                    refinfo.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            info { "refid $refid" }

                            bulan = p0.child("batastanggal/month").value.toString()
                            hari = p0.child("batastanggal/date").value.toString()
                            jam = p0.child("bataswaktu/hours").value.toString()
                            menit = p0.child("bataswaktu/minutes").value.toString()
                            seconds = p0.child("bataswaktu/seconds").value.toString()


                            val calendar = Calendar.getInstance()
                            val mont = calendar[Calendar.MONTH]
                            val day = calendar[Calendar.DAY_OF_MONTH]
                            val hours = calendar[Calendar.HOUR_OF_DAY]
                            val minutes = calendar[Calendar.MINUTE]
                            val second = calendar[Calendar.SECOND]
                            val tahun = calendar[Calendar.YEAR]

                            val bulanfirebase: Int? = bulan!!.toIntOrNull()
                            val harifirebase: Int? = hari!!.toIntOrNull()
                            val jamfirebase: Int? = jam!!.toIntOrNull()
                            val menitfirebase: Int? = menit!!.toIntOrNull()
                            val secondfirebase: Int? = seconds!!.toIntOrNull()

                            val tanggalberakhir =
                                "$harifirebase/${bulanfirebase?.plus(1)}/$tahun  $jamfirebase:$menitfirebase:$secondfirebase"

                            var a = 0

                            if (bulanfirebase != null && harifirebase != null && jamfirebase != null && menitfirebase != null) {

                                if (mont > bulanfirebase) {

        var reference = FirebaseDatabase.getInstance().reference
            .child("products")
            .child(refid.toString())
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var ambildata = p0.child("nameMember").value.toString()
                var myArray2 = arrayOf<String>(ambildata)
                toast(myArray2[0])
            }

        })
                                    holder.status.text = "EXPIRED"
                                    a = 1
                                } else if (mont == bulanfirebase) {
                                    if (day > harifirebase) {
                                        var reference = FirebaseDatabase.getInstance().reference
                                            .child("products")
                                            .child(refid.toString())
                                        reference.addValueEventListener(object :ValueEventListener{
                                            override fun onCancelled(p0: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                var ambildata = p0.child("nameMember").value.toString()
                                            }

                                        })

//                                        data()
                                        holder.status.text = "EXPIRED"
                                        a = 1

                                    } else if (day == harifirebase) {
                                        if (hours > jamfirebase) {

//                                            data()
                                            var reference = FirebaseDatabase.getInstance().reference
                                                .child("products")
                                                .child(refid).child("statuslelang")

                                            reference.setValue("EXPIRED")
                                            holder.status.text = "EXPIRED"


/*

*/
                                            a = 1

                                        } else if (hours == jamfirebase) {
                                            if (minutes > menitfirebase) {
//                                                data()
                                                var reference = FirebaseDatabase.getInstance().reference
                                                    .child("products")
                                                    .child(refid.toString())
                                                reference.addValueEventListener(object :ValueEventListener{
                                                    override fun onCancelled(p0: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onDataChange(p0: DataSnapshot) {
                                                        var ambildata = p0.child("nameMember").value.toString()
                                                        myArray2 = arrayListOf(ambildata)

                                                    }

                                                })
                                                holder.status.text = "EXPIRED"
                                                a = 1

                                            } else {
                                                holder.status.text = "ONPROGESS"
                                                a = 2

                                            }
                                        } else {
                                            holder.status.text = "ONPROGESS"
                                            a = 2

                                        }
                                    } else {
                                        holder.status.text = "ONPROGESS"
                                        a = 2

                                    }
                                } else {
                                    holder.status.text = "ONPROGESS"
                                    a = 2

                                }
                            }



                            holder.mtitle.text = "Nama barang : ${model.name}"
                            holder.mprice.text = "Harga : ${model.price}"
                            holder.mOwner.text = "Nama Penjual : ${model.owner.toString()}"
                            Picasso.get().load(model.image).fit().centerCrop().into(holder.mimage)
                            holder.itemView.setOnClickListener {
                                /*    startActivity<Detail>(
                                        "id" to model.id,
                                        "owner" to model.owner,
                                        "name" to model.name,
                                        "owner" to model.owner,
                                        "price" to model.price.toString(),
                                        "deskripsi" to model.description,
                                        "gambar" to model.image,
                                        "nilai" to a.toString(),
                                        "nameMember" to model.nameMember,
                                        "hari" to  tanggalberakhir
                                    )*/
                            }


                        }

                    })
                }
            }

        rv_pemenanglelang.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
        rv_daftarpemenanglelang.setHasFixedSize(true)
        rv_daftarpemenanglelang.layoutManager = LinearLayoutManager(this)
        artistList = ArrayList()
        adapter = Adapter(this, artistList)
        rv_daftarpemenanglelang.adapter = adapter

        var query = FirebaseDatabase.getInstance().getReference("products").orderByChild("statuslelang").equalTo("EXPIRED")
        query.addListenerForSingleValueEvent(valueEventListener)


     }

    val valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            artistList.clear()
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    var artist: Produk? = snapshot.getValue(Produk::class.java)
                        info { "hasil ${artist!!.name}" }
                    artistList.add(artist!!)
                }

                adapter!!.notifyDataSetChanged()
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    }

//    fun data()
//    {
//
//        var reference = FirebaseDatabase.getInstance().reference
//            .child("products")
//            .child(refid.toString())
//        reference.addValueEventListener(object :ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                var ambildata = p0.child("nameMember").value.toString()
//                var myArray2 = arrayOf<String>(ambildata)
//                toast(myArray2[0])
//            }
//
//        })
//    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nilai : View = itemView
        var mtitle: TextView = itemView.findViewById(R.id.product_name)
        var mimage: ImageView = itemView.findViewById(R.id.product_image)
        var mprice: TextView = itemView.findViewById(R.id.product_price)
        var status: TextView = itemView.findViewById(R.id.status)
        var mOwner : TextView = itemView.findViewById(R.id.product_owner)

    }

    class holderbaru(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mnama: TextView = itemView.findViewById(R.id.namapelelang)
        var mgambar: ImageView = itemView.findViewById(R.id.gambarpelelang)
        var mharga: TextView = itemView.findViewById(R.id.hargalelang)
        var mbarang: TextView = itemView.findViewById(R.id.txt_namabarang)
    }

    fun recyclerbaru()
    {
    }

}
