package com.alfanshter.udinlelangfix.ui.Like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.alfanshter.udinlelangfix.ui.dashboard.DashboardFragment
import com.alfanshter.udinlelangfix.ui.dashboard.detail.Detail
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var refinfo: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = root.find(R.id.rv_product)

        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        val auth = FirebaseAuth.getInstance()
        val userid = auth.currentUser!!.uid
        LayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("users").child(userid).child("like")


        val option = FirebaseRecyclerOptions.Builder<Produk>().setQuery(refinfo, Produk::class.java)
            .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<Produk, DashboardFragment.MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardFragment.MyViewHolder {
                    val itemView = LayoutInflater.from(context?.applicationContext)
                        .inflate(R.layout.card_view, parent, false)
                    return DashboardFragment.MyViewHolder(itemView)
                }

                override fun onBindViewHolder(holder: DashboardFragment.MyViewHolder, position: Int, model: Produk) {
                    val refid = getRef(position).key.toString()
                    refinfo.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            holder.mtitle.setText(model.name)
                            holder.mprice.setText(model.price.toString())
                            Picasso.get().load(model.image).fit().centerCrop().into(holder.mimage)
                            holder.itemView.setOnClickListener {
                                startActivity<Detail>(
                                    "id" to model.id,
                                    "owner" to model.owner,
                                    "name" to model.name,
                                    "owner" to model.owner,
                                    "price" to model.price.toString(),
                                    "deskripsi" to model.description,
                                    "gambar" to model.image,
                                    "nilai" to model.nilai,
                                    "hari" to model.hari
                                )
                            }
                        }

                    })
                }
            }


        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
        return root
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mtitle: TextView = itemView.findViewById(R.id.product_name)
        var mimage: ImageView = itemView.findViewById(R.id.product_image)
        var mprice : TextView = itemView.findViewById(R.id.product_price)

    }

}