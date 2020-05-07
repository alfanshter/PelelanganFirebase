package com.alfanshter.udinlelangfix.ui.money


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.alfanshter.udinlelangfix.Model.Produk
import com.alfanshter.udinlelangfix.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_money.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNREACHABLE_CODE")
class MoneyFragment : Fragment() {
    private lateinit var alertDialog: AlertDialog
    lateinit var reference: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_money, container, false)

        val btn_tambah : Button = root.find(R.id.tambah)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        reference = FirebaseDatabase.getInstance().reference.child("udang").child("Users").child(userID)
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var data = p0.getValue(Produk::class.java)
                if (data!=null)
                {
                        nav_saldo.text = "Rp. ${data.saldo}"
                        Picasso.get().load(data.foto.toString()).into(fotosaldo)
                        nama.text = data.name.toString()
                }
            }

        })

        btn_tambah.setOnClickListener {
            showPopUp()
        }

        return root
    }

    private fun showPopUp() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.input_saldo, null)

        val nilai_edt = dialogView.findViewById<EditText>(R.id.edt_saldo)
            nilai_edt.setText("0")
        val btn_tambah = dialogView.findViewById<Button>(R.id.btn_topup)

        btn_tambah.setOnClickListener {
            var database = FirebaseDatabase.getInstance().reference.child("udang").child("Users")
                .child(userID).child("saldo")
            database.setValue(nilai_edt.text.toString().toInt())
            alertDialog.dismiss()
        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {

            }
        })
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.window!!.getAttributes().windowAnimations = R.style.PauseDialogAnimation
        alertDialog.show()

    }


    }
