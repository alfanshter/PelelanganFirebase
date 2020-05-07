package com.alfanshter.udinlelangfix.Model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Produk : Parcelable {
    var id: String? = null
    var name: String? = null
    var image: String? = null
    var price = 0
    var owner: String? = null
    var idOwner: String? = null
    var description: String? = null
    var idMember: String? = null
    var nameMember: String? = null
    var batastanggal : Date? = null
    var bataswaktu : Date? = null
    var email : String? = null
    var foto : String? = null
    var namalelang : String? = null
    var namabarang : String? = null
    var nilai : String? = null
    var hari : String? = null
    var saldo = 0
    var value : String? = null



    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        name = `in`.readString()
        image = `in`.readString()
        price = `in`.readInt()
        owner = `in`.readString()
        idOwner = `in`.readString()
        description = `in`.readString()
        idMember = `in`.readString()
        nameMember = `in`.readString()
    }

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(
        id: String?,
        name: String?,
        image: String?,
        price: Int,
        owner: String?,
        idOwner: String?,
        description: String?,
        idMember: String?,
        nameMember: String?,
        batastanggal: Date?,
        bataswaktu: Date?,
        email:String?,
        foto : String?,
        namalelang: String?,
        namabarang:String?,
        nilai:String?,
        hari : String?,
        saldo : Int,
        value: String?
    ) {
        this.id = id
        this.name = name
        this.image = image
        this.price = price
        this.owner = owner
        this.idOwner = idOwner
        this.description = description
        this.idMember = idMember
        this.nameMember = nameMember
        this.batastanggal = batastanggal
        this.bataswaktu = bataswaktu
        this.email = email
        this.foto = foto
        this.namabarang = namabarang
        this.namalelang = namalelang
        this.nilai = nilai
        this.hari = hari
        this.saldo = saldo
        this.value = value
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeInt(price)
        parcel.writeString(owner)
        parcel.writeString(idOwner)
        parcel.writeString(description)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Produk?> = object : Parcelable.Creator<Produk?> {
            override fun createFromParcel(`in`: Parcel): Produk? {
                return Produk(`in`)
            }

            override fun newArray(size: Int): Array<Produk?> {
                return arrayOfNulls(size)
            }
        }
    }
}
