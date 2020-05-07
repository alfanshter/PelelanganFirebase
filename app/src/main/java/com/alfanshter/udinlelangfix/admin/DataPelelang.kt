package com.alfanshter.udinlelangfix.admin

import android.content.DialogInterface
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
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_data_pelelang.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*

class DataPelelang : AppCompatActivity(),AnkoLogger {
    private lateinit var recyclerView: RecyclerView
    lateinit var refinfo: DatabaseReference

    var bulan: String? = null
    var hari: String? = null
    var jam: String? = null
    var menit: String? = null
    var seconds: String? = null
    lateinit var dialog: AlertDialog
    var refid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelelang)

        val auth = FirebaseAuth.getInstance()
        val userid = auth.currentUser!!.uid

        val LayoutManager = LinearLayoutManager(this)

        LayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_datapelelang.layoutManager = LayoutManager

        refinfo = FirebaseDatabase.getInstance().reference.child("products")


        val option = FirebaseRecyclerOptions.Builder<Produk>().setQuery(refinfo, Produk::class.java)
            .build()
        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<Produk, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val itemView = LayoutInflater.from(this@DataPelelang)
                        .inflate(R.layout.card_view, parent, false)
                    return MyViewHolder(itemView)

                }

                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Produk) {
                    refid = getRef(position).key.toString()
                    refinfo.child(refid!!).addValueEventListener(object : ValueEventListener {

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
                                    holder.status.setText("EXPIRED")
                                    a = 1
                                } else if (mont == bulanfirebase) {
                                    if (day > harifirebase) {
                                        holder.status.setText("EXPIRED")
                                        a = 1

                                    } else if (day == harifirebase) {
                                        if (hours > jamfirebase) {
                                            holder.status.setText("EXPIRED")
                                            a = 1

                                        } else if (hours == jamfirebase) {
                                            if (minutes > menitfirebase) {
                                                holder.status.setText("EXPIRED")
                                                a = 1

                                            } else {
                                                holder.status.setText("ONPROGESS")
                                                a = 2

                                            }
                                        } else {
                                            holder.status.setText("ONPROGESS")
                                            a = 2

                                        }
                                    } else {
                                        holder.status.setText("ONPROGESS")
                                        a = 2

                                    }
                                } else {
                                    holder.status.setText("ONPROGESS")
                                    a = 2

                                }
                            }

                            holder.hapus.setOnClickListener {

                                showdialog()

                            }

                            holder.mtitle.setText("Nama barang : ${model.name}")
                            holder.mprice.setText("Harga : ${model.price}")
                            holder.mOwner.setText("Nama Penjual : ${model.owner.toString()}")
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

        rv_datapelelang.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mtitle: TextView = itemView.findViewById(R.id.product_name)
        var mimage: ImageView = itemView.findViewById(R.id.product_image)
        var mprice: TextView = itemView.findViewById(R.id.product_price)
        var status: TextView = itemView.findViewById(R.id.status)
        var mOwner: TextView = itemView.findViewById(R.id.product_owner)
        var hapus: ImageView = itemView.findViewById(R.id.hapus)

    }

    fun showdialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apakah anda ingin menghapus lelang ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("products")
                    database.child(refid!!).removeValue().addOnCompleteListener {
                        if (it.isSuccessful){
                            toast("berhasil terhapus")
                        }
                        else{
                            toast("coba ulangi lagi")
                        }
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                }
                DialogInterface.BUTTON_NEUTRAL -> {
                }
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES", dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO", dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL", dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

}
