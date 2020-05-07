package com.alfanshter.udinlelangfix.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_halaman_utama.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadAdmin : AppCompatActivity(),AnkoLogger {
    lateinit var dbaselelang: DatabaseReference
    private var btn: Button? = null
    private var upload: Button? = null

    lateinit var dialog: AlertDialog



    private var datePickerDialog: DatePickerDialog? = null
    private var dateFormatter: SimpleDateFormat? = null

    private var imageview: ImageView? = null
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1
    private var myUrl = ""
    lateinit var database: DatabaseReference
    private var mAuth: FirebaseAuth? = null
    private var mUserId: String? = null
    var namauser = "Gus Udin"
    private var mDatabase: DatabaseReference? = null

    var batastanggal : Date? = null
    var batasjam : Date? = null

    var duapuluhgram :Int? = null
    var dualimagram :Int? = null
    var tigapuluhgram :Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halaman_utama)
        layout_berhasil.visibility = View.INVISIBLE
        layout_gagal.visibility = View.INVISIBLE
        mAuth = FirebaseAuth.getInstance()
        mUserId = mAuth!!.currentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference

        dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)


        storageReference = FirebaseStorage.getInstance().reference.child("images")

        dbaselelang = FirebaseDatabase.getInstance().reference.child("alat")
        dbaselelang.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                 duapuluhgram = p0.child("berat/duapuluhgram").value.toString().toInt()
                 dualimagram = p0.child("berat/dualimagram").value.toString().toInt()
                 tigapuluhgram = p0.child("berat/tigapuluhgram").value.toString().toInt()

                info { "hasil $dualimagram" }
                var ambildata = p0.child("statuslelang").getValue(Produk::class.java)
                var pembukaan = ambildata!!.value
                if (pembukaan.equals("1"))
                {
                     layout_berhasil.visibility = View.VISIBLE
                    layout_gagal.visibility = View.INVISIBLE
                }
                else
                {
                    layout_berhasil.visibility = View.INVISIBLE
                    layout_gagal.visibility = View.VISIBLE
                }


                txt_duapuluhkg.text = "Udang 20 GRAM  : $duapuluhgram"
                txt_duapuluhlimakg.text = "Udang 25 GRAM  : $dualimagram"
                txt_tigapuluhkg.text = "Udang 30 GRAM   : $tigapuluhgram"
                txt_jumlahudang.text = "Jumlah Total Udang  : ${duapuluhgram!! + dualimagram!! + tigapuluhgram!!}"
            }


        })


        btn_aktif.setOnClickListener {
            showaktif()
        }

        tanggal.setOnClickListener {
            showDateDialog()
        }

        jam_lelang.setOnClickListener {
            showtimeDialog()
        }
        fotolelang.setOnClickListener {
            pilihfile()
        }

        uploadlelang.setOnClickListener {
            uploadFile()
        }

        btn_keluarlelang.setOnClickListener {
            showkeluar()
        }
    }

    private fun showaktif() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apakah anda ingin menghapus lelang ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("alat").child("aktifalat").child("hiduptimbangan")
                    database.setValue(1)
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

    private fun showkeluar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apakah anda ingin menghapus lelang ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("alat").child("aktifalat").child("hiduptimbangan")
                    database.setValue(0)
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

    private fun showtimeDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            jam_lelang.setText(SimpleDateFormat("HH:mm").format(cal.time))
            batasjam = cal.time

        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


    }

    private fun showDateDialog() {
        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth

                tanggal!!.setText(dateFormatter!!.format(newDate.time))
                batastanggal = newDate.time
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog!!.show()

    }

    private fun pilihfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "PILIH GAMBAR"), PICK_IMAGE_REQUEST)

    }
    private fun uploadFile() {

        when {
            filePath == null -> toast("ambil gambar telebih dahulu ")
            TextUtils.isEmpty(txt_namabaranglelang.text.toString()) -> toast("masukkan nama terlebih dahulu")
            batasjam == null -> toast("masukkan batas jam terlebih dahulu")
            batastanggal == null -> toast("masukkan batas tanggal terlebih dahulu")
            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Akun Setting")
                progressDialog.setMessage("Tunggu , sedang update")
                progressDialog.show()
                val fileref =
                    storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileref.putFile(filePath!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw  it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val key =
                            FirebaseDatabase.getInstance().reference.child("products").push().key

                        val usermap:MutableMap<String,Any?> = HashMap()

                        var hargaduapuluh = txt_hargaduapuluh.text.toString().toInt()
                        var hargaduapuluhlima = txt_hargaduapuluhlima.text.toString().toInt()
                        var hargatigapuluh = txt_hargatigapuluh.text.toString().toInt()

                        var hargatotal = (hargaduapuluh * duapuluhgram!!) + (hargaduapuluhlima * dualimagram!!) + (hargatigapuluh * tigapuluhgram!!)

                        usermap["id"] = key
                        usermap["name"] = txt_namabaranglelang.text.toString()
                        usermap["image"] = myUrl
                        usermap["price"] = hargatotal
                        usermap["owner"] = namauser
                        usermap["idowner"] = mUserId
                        usermap["description"] = txt_deskripsi.text.toString()
                        usermap["idMember"] = "-"
                        usermap["nameMember"] = "-"
                        usermap["batastanggal"] =batastanggal
                        usermap["bataswaktu"] =batasjam

                        mDatabase!!.child("products").child(key!!).setValue(usermap)
                            .addOnCompleteListener(OnCompleteListener<Void?> {
                                finish()
                                startActivity<MenuAdmin>()

                            })

                        toast("upload sukses")
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })

            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                fotolelang!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
