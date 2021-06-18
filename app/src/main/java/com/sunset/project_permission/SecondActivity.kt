package com.sunset.project_permission

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstudy.R
import com.sunset.project_permission.permission.requestPermission

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.applyCamera).setOnClickListener {
            requestPermission(Manifest.permission.CAMERA){
                requestCode = 1001
                resultCallback = {
                    Log.i(">>>>> second", "permissionResult is ${this.requestCode}-${this}")
                }
            }
        }

        findViewById<Button>(R.id.applyStorage).setOnClickListener {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE){
                requestCode = 1002
                resultCallback = {
                    Log.i(">>>>> second", "permissionResult is ${this.requestCode}-${this}")
//                    when{
//                        this is PermissionResult.PermissionGranted -> {
//
//                        }
//                        this is PermissionResult.PermissionDenied -> {
//
//                        }
//                        th
//                    }
                }
            }
        }

        findViewById<Button>(R.id.applyAll).setOnClickListener {
            requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE){
                requestCode = 1003
                resultCallback = {
                    Log.i(">>>>> second", "permissionResult is ${this.requestCode}-${this}")
                }
            }
        }

        findViewById<Button>(R.id.toNext).visibility = View.GONE

    }

}