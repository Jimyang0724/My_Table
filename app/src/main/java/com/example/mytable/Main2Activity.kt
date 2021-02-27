package com.example.mytable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytable.databinding.ActivityMain2Binding
import com.example.mytable.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Main2Activity : AppCompatActivity() {
    lateinit var binding2: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        binding2 = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding2.root)

//        val user = FirebaseAuth.getInstance().currentUser

        val nameGet = intent.getStringExtra("nameKey")
        binding2.name.text = "訂購人姓名：$nameGet"

        binding2.checkButtom.setOnClickListener{
            if(binding2.drinkName.text.isNullOrEmpty()){
                Toast.makeText(this,"飲料名稱", Toast.LENGTH_SHORT).show()
            }
            else{
                calculateSugarIce()
            }
        }
    }

    fun calculateSugarIce(){
        val bundle = Bundle()

        // Get DrinkName
        bundle.putString("drinksKey", binding2.drinkName.text.toString())

        // Get Sugar
        val sugarId = binding2.sugarOption.checkedRadioButtonId
        val sugar: String = when(sugarId) {
            R.id.noSugar -> binding2.noSugar.text.toString()
            R.id.littleSugar -> binding2.littleSugar.text.toString()
            R.id.halfSugar -> binding2.halfSugar.text.toString()
            else -> binding2.regularSugar.text.toString()
        }

        // Get Ice
        val iceId = binding2.iceOption.checkedRadioButtonId
        val ice: String = when(iceId) {
            R.id.noIce -> binding2.noIce.text.toString()
            R.id.littleIce -> binding2.littleIce.text.toString()
            R.id.lessIce -> binding2.lessIce.text.toString()
            else -> binding2.regularIce.text.toString()
        }

        bundle.putString("sugarKey", sugar)
        bundle.putString("iceKey", ice)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}