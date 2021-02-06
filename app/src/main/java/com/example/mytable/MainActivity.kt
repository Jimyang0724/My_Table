package com.example.mytable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityMainBinding
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
            finish()
        }
        else{
            Toast.makeText(this, "再按一次登出", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK)
                binding.order.text =
                    "飲料:${it.getString("drinksKey")}\n\n甜度:${it.getString("sugarKey")}\n\n冰塊:${it.getString("iceKey")}"
        }
    }

    fun jumpToOderPage(){
        val intent = Intent(this, Main2Activity::class.java)
        val name = binding.name.text.toString()
        intent.putExtra("nameKey", name)
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
    }
}