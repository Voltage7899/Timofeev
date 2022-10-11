package com.example.timofeev

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.timofeev.databinding.ActivitySignBinding
import com.google.firebase.database.*

class Sign : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding
    private val parentDataBaseName = "User"
    private var database: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        sing()
    }

    private fun sing() {
        binding.button.setOnClickListener {
            val phone_field = binding.Phone.text.toString()
            val pass_field = binding.Pass.getText().toString()
            if (TextUtils.isEmpty(phone_field) && TextUtils.isEmpty(pass_field)) {
                Toast.makeText(this, "Введите все данные", Toast.LENGTH_SHORT).show()
            } else {

                if (parentDataBaseName == "User") {
                    if (TextUtils.isEmpty(phone_field) && TextUtils.isEmpty(pass_field)) {
                        Toast.makeText(this@Sign, "Введите все данные", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        database?.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.child("User").child(phone_field).exists()) {

                                    val userCurrentData =
                                        snapshot.child("User").child(phone_field).getValue(
                                            User::class.java
                                        )
                                    if (userCurrentData!!.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field) && userCurrentData.name.equals("admin") ) {
                                        Toast.makeText(this@Sign, "Вы вошли как админ", Toast.LENGTH_SHORT).show()

                                        User.currentuser=userCurrentData
                                        val intent = Intent(this@Sign, AdminMode::class.java)
                                        startActivity(intent)
                                    }
                                    else if(userCurrentData!!.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                        Toast.makeText(this@Sign, "Вы вошли как юзер", Toast.LENGTH_SHORT).show()
                                        User.currentuser=userCurrentData
                                        val intent = Intent(this@Sign, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else {
                                        Toast.makeText(this@Sign, "Wrong Data", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
        }
    }
}