package com.example.mytable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityFirstPageBinding
import kotlin.system.exitProcess

class FirstPage : AppCompatActivity() {
    lateinit var bindingF: ActivityFirstPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)
        bindingF = ActivityFirstPageBinding.inflate(layoutInflater)
        setContentView(bindingF.root)

        bindingF.register.setOnClickListener{
            register()
        }

        bindingF.login.setOnClickListener{
            login()
        }
    }

    var exitTime:Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - exitTime < 2000){
            super.onBackPressed()
//            System.exit(0)
        }
        else{
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        }
    }

    fun register(){
        startActivity(Intent(this, Register::class.java))
    }

    fun login(){
        startActivity(Intent(this, Login::class.java))
    }
}