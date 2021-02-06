package com.example.mytable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    lateinit var bindingR: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bindingR = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingR.root)

        bindingR.regiButton.setOnClickListener{
            if(checkEmail() && checkPassword()){
                toNextPage()
                finish()
            }

        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, FirstPage::class.java))
        finish()
    }

    fun checkEmail(): Boolean{
        if(bindingR.emailAddr.text.isNullOrEmpty()) {
            Toast.makeText(this, "Email 不可為空", Toast.LENGTH_SHORT).show()
            return false
        }

        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingR.emailAddr.text).matches()){
            Toast.makeText(this, "Email 格式錯誤", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun checkPassword(): Boolean{
        if(bindingR.passeord.text.isNullOrEmpty()) {
            Toast.makeText(this, "密碼不可為空", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(bindingR.passeord.text.toString() != bindingR.passeordAgain.text.toString()) {
            Toast.makeText(this, "密碼與確認密碼不符", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun toNextPage(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}