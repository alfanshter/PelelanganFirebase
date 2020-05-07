package com.alfanshter.udinlelangfix.admin

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.alfanshter.udinlelangfix.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_timbangan.*

class Timbangan : AppCompatActivity() {
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timbangan)

        rv_timbanganpertama.visibility = View.INVISIBLE
        rv_timbangankedua.visibility = View.INVISIBLE

        var reference = FirebaseDatabase.getInstance().reference.child("alat").child("statustimbangan")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var ambildata = p0.value.toString()

                if (ambildata.equals("1"))
                {
                    rv_timbanganpertama.visibility = View.VISIBLE
                    rv_timbangankedua.visibility = View.INVISIBLE

                    var nilaigram = FirebaseDatabase.getInstance().reference.child("alat").child("berat").child("timbangan")
                    nilaigram.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            var ambildata = p0.value.toString().toInt()
                            nilaitimbangna.text = "Nilai : $ambildata + GRAM"
                             }

                    })

                }
                else
                {
                    rv_timbanganpertama.visibility = View.INVISIBLE
                    rv_timbangankedua.visibility = View.VISIBLE

                }

                 }

        })
        btn_aktiftimbangan.setOnClickListener {
            showaktif()
        }

        btn_matikantimbangan.setOnClickListener {
            showmati()
        }


    }

    private fun showaktif() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apakah anda ingin mengaktifkan timbangan ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("alat").child("aktifalat").child("hiduptimbangan")
                    database.setValue(2)
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

    private fun showmati() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apakah anda ingin menonaktifkan fungsi timbangan ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("alat").child("aktifalat").child("hiduptimbangan")
                    database.setValue(20)
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
