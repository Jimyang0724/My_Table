package com.example.mytable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var bindingL: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindingL = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingL.root)

        mAuth = FirebaseAuth.getInstance()

        bindingL.loginButton.setOnClickListener{
            if(check()){
                login()
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, FirstPage::class.java))
        finish()
    }

    private fun check():Boolean{
        var checkNum: Int = 0
        if(bindingL.emailAddr.text.isNullOrEmpty()){
            bindingL.emailAddrLayout.error = "請輸入Email"
            checkNum++
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingL.emailAddr.text).matches()){
            bindingL.emailAddrLayout.error = "Email 格式錯誤"
            checkNum++
        }
        else bindingL.emailAddrLayout.isErrorEnabled = false

        if(bindingL.password.text.isNullOrEmpty()){
            bindingL.passwordLayout.error = "請輸入密碼"
            checkNum++
        }
        else bindingL.passwordLayout.isErrorEnabled = false

        if(checkNum !=0) return false
        return true
    }

    private fun login(){
        mAuth.signInWithEmailAndPassword(bindingL.emailAddr.text.toString(), bindingL.password.text.toString()).addOnCompleteListener(){
            if(it.isSuccessful){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                toNextPage()
                finish()
            }
            else{
                Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toNextPage(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}