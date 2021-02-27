package com.example.mytable

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

        binding.check.setOnClickListener{
            if(binding.name.text.isEmpty()){
                Toast.makeText(this,"輸入姓名", Toast.LENGTH_SHORT).show()
            }
            else {
                jumpToOderPage()
            }
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                binding.order.text =
                        "飲料:${it.getString("drinksKey")}\n\n甜度:${it.getString("sugarKey")}\n\n冰塊:${it.getString("iceKey")}"
                val user = FirebaseAuth.getInstance().currentUser
                val userName = user?.email

                if(userName == null){
                    Toast.makeText(this, "尚未登入", Toast.LENGTH_SHORT).show()
                    finish()
                }

                val db = FirebaseDatabase.getInstance()
                val dbRef = db.reference.child("a").child("b")//.child(user.toString()).setValue("a") // /${userName.toString()}/Drinks/")

                val builder = AlertDialog.Builder(this)
                builder.setMessage("${db.toString()} \n\n ${dbRef.toString()}")
                builder.show()

                dbRef.child("Name").setValue(it.getString("drinksKey"))
                dbRef.child("Sugar").setValue(it.getString("sugarKey"))
                dbRef.child("Ice").setValue(it.getString("iceKey"))

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