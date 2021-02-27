package com.example.mytable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.example.mytable.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var bindingR: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bindingR = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingR.root)

        mAuth = FirebaseAuth.getInstance()

        bindingR.regiButton.setOnClickListener{
            if(check()){
                signUp()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, FirstPage::class.java))
        finish()
    }

    private fun check(): Boolean{
        var checkNum: Int = 0
        if(bindingR.emailAddr.text.isNullOrEmpty()) {
            bindingR.emailAddrLayout.error = "請輸入Email"
            checkNum++
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(bindingR.emailAddr.text).matches()){
            bindingR.emailAddrLayout.error = "Email 格式錯誤"
            checkNum++
        }
        else{
            bindingR.emailAddrLayout.isErrorEnabled = false
        }

        if(bindingR.password.text.isNullOrEmpty()) {
            bindingR.passwordLayout.error = "請輸入密碼"
            checkNum++
        }
        else{
            bindingR.passwordLayout.isErrorEnabled = false
        }

        if(bindingR.password.text.toString() != bindingR.passeordAgain.text.toString()) {
            bindingR.passwordAgainLayout.error = "密碼與確認密碼不符"
            checkNum++
        }
        else{
            bindingR.passwordAgainLayout.isErrorEnabled = false
        }

        if(checkNum == 0) return true
        return false
    }

    private fun signUp(){
        mAuth.createUserWithEmailAndPassword(bindingR.emailAddr.text.toString(), bindingR.password.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    toNextPage()
                    finish()
                }
                else{
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun toNextPage(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}