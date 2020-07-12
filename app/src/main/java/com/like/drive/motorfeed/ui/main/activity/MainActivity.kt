package com.like.drive.motorfeed.ui.main.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            FirebaseFirestore.getInstance().collection("MotorType").get()
                .addOnSuccessListener { documentReference ->

                    val list = ArrayList<MotorTypeData>()
                    documentReference.forEach {
                       list.add(it.toObject(MotorTypeData::class.java))
                    }

                    if(list.isNotEmpty()){
                        val brandName = list.distinctBy { it.brandCode }.size
                        Log.i(
                            this.javaClass.name,
                            "BrandListSize: $brandName"
                        )

                    }
                }
                .addOnFailureListener { e ->
                    Log.w(this.javaClass.name, "Error adding document", e)
                }
        }
}

data class Motor(val name:String,val code:Int)