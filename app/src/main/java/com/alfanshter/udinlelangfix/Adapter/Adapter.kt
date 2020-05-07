package com.alfanshter.udinlelangfix.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R


class Adapter(
    private val mCtx: Context,
    artistList: List<Produk>
) :
    RecyclerView.Adapter<Adapter.ArtistViewHolder?>() {
    private val artistList: List<Produk>

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view: View =
            LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_artists, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ArtistViewHolder, position: Int) {
        val artist: Produk = artistList[position]
        holder.textViewName.text = "Nama Pemenang : ${artist.nameMember}"
        holder.textViewGenre.text = "Harga " + artist.price
        holder.textViewAge.text = "Nama Barang " + artist.name
        holder.textViewCountry.text = "Penjual: " + artist.owner
    }


    inner class ArtistViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var textViewGenre: TextView
        var textViewAge: TextView
        var textViewCountry: TextView

        init {
            textViewName = itemView.findViewById(R.id.text_view_name)
            textViewGenre = itemView.findViewById(R.id.text_view_genre)
            textViewAge = itemView.findViewById(R.id.text_view_age)
            textViewCountry = itemView.findViewById(R.id.text_view_country)
        }
    }

    init {
        this.artistList = artistList
    }

    override fun getItemCount(): Int {
        return artistList.size
    }
}
