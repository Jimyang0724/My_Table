package com.example.mytable

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.mytable.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val LAUNCH_SECOND_ACTIVITY = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkOrder()

        binding.check.setOnClickListener{
            if(binding.name.text.isEmpty()){
                Toast.makeText(this,"輸入姓名", Toast.LENGTH_SHORT).show()
            }
            else {
                jumpToOderPage()
            }
        }

        binding.cancelButton.setOnClickListener{
            deleteOrder()
        }
    }


    var exitTime:Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - exitTime < 2000){
            startActivity(Intent(this, FirstPage::class.java))
            FirebaseAuth.getInstance().signOut()
            finish()
        }
        else{
            Toast.makeText(this, "再按一次登出", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        }
    }

    private fun checkOrder() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null){
            Toast.makeText(this, "尚未登入", Toast.LENGTH_SHORT).show()
            finish()
        }

        val ref = FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object: ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.cancelButton.visibility = View.VISIBLE
                val drinkPath = dataSnapshot.child(user?.email.toString().split('@')[0])
                if(drinkPath.child("Name").getValue() != null) {
                    binding.order.text =
                            "姓名:${drinkPath.child("Name").getValue()}\n" +
                                    "\n飲料:${drinkPath.child("DrinkName").getValue()}\n" +
                                    "\n甜度:${drinkPath.child("Sugar").getValue()}\n" +
                                    "\n冰塊:${drinkPath.child("Ice").getValue()}"
                }
                else{
                    binding.cancelButton.visibility = View.INVISIBLE
                    binding.order.text = null
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
                binding.cancelButton.visibility = View.INVISIBLE
            }
        })

    }

    private fun deleteOrder(){
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null){
            Toast.makeText(this, "尚未登入", Toast.LENGTH_SHORT).show()
            finish()
        }

        val ref = FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.child(user?.email.toString().split('@')[0]).removeValue()
        binding.order.text = null
        binding.cancelButton.visibility = View.INVISIBLE

    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                binding.order.text =
                        "飲料:${it.getString("drinksKey")}\n\n甜度:${it.getString("sugarKey")}\n\n冰塊:${it.getString("iceKey")}"

                val user = FirebaseAuth.getInstance().currentUser
                if(user == null){
                    Toast.makeText(this, "尚未登入", Toast.LENGTH_SHORT).show()
                    finish()
                }

                val userID = user?.uid
                val userMail = user?.email

                val db = FirebaseDatabase.getInstance().reference
                val dbRef = db.child(userID.toString()).child((userMail.toString().split('@'))[0])

                dbRef.child("Name").setValue(binding.name.text.toString())
                dbRef.child("DrinkName").setValue(it.getString("drinksKey"))
                dbRef.child("Sugar").setValue(it.getString("sugarKey"))
                dbRef.child("Ice").setValue(it.getString("iceKey"))

//                val builder = AlertDialog.Builder(this)
//                builder.setMessage("${db} \n\n ${dbRef}")
//                builder.show()

            }
        }
    }

    fun jumpToOderPage(){
        val intent = Intent(this, Main2Activity::class.java)
        val name = binding.name.text.toString()
        intent.putExtra("nameKey", name)
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
    }
}