package com.example.timofeev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timofeev.databinding.ActivityUserDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UserDetail : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailBinding
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val item= intent.getSerializableExtra("chosenRace") as Race
        Picasso.get().load(item.image).fit().into(binding.imageItem)

        binding.priceItem.setText(item.price)
        binding.departureItem.setText(item.departure)
        binding.landingItem.setText(item.landing)
        binding.fromItem.setText(item.from)
        binding.toItem.setText(item.to)

        binding.buyItem.setOnClickListener{

            val id = User.currentuser?.phone.toString()
            val ticket=ticketModel(id, item.price,item.departure,item.landing,item.from,item.to,item.image,item.id)
            database.child("Ticket").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(id).child(ticket.idRace.toString()).exists()){
                        Toast.makeText(this@UserDetail,"У вас уже куплен билет",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        database.child("Ticket").child(id).child(ticket.idRace.toString()).setValue(ticket)
                        Toast.makeText(this@UserDetail,"Вы купили билет",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}