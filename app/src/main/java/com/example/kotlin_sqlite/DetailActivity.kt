package com.example.kotlin_sqlite

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.ByteArrayOutputStream


class DetailActivity : AppCompatActivity() {

    var selectedImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val intent = intent

        val info = intent.getStringExtra("info")

        if (info.equals("new")){
            val background = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.tapto)
            imageView.setImageBitmap(background)
            button.visibility = View.VISIBLE
            editText.setText("")
        } else {
            val name = intent.getStringExtra("name")
            editText.setText(name)

            val chosen = Globals.Choosen
            val bitmap = chosen.returnImage()

            imageView.setImageBitmap(bitmap)

            button.visibility = View.INVISIBLE
        }




    }

    fun tapto(view: View){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)

        } else {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode ==2){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,1)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            val image = data.data

            try{

                val selectedImage = MediaStore.Images.Media.getBitmap(this.contentResolver,image)
                imageView.setImageBitmap(selectedImage
                )

            } catch (e : Exception){
                e.printStackTrace()

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }





    fun save(view: View){

        val movieName = editText.text.toString()

        val outputStream = ByteArrayOutputStream()

        selectedImage?.compress(Bitmap.CompressFormat.PNG,50,outputStream)

        val byteArray = outputStream.toByteArray()



        try{

            val database = this.openOrCreateDatabase("Movies", Context.MODE_PRIVATE,null)

            database.execSQL("CREATE TABLE IF NOT EXISTS movies (name VARCHAR, image BLOG)")

            val sqlString = "INSERT INTO movies (name, image) VALUES (?, ?)"
            val statement = database.compileStatement(sqlString)

            statement.bindString(1,movieName)
            statement.bindBlob(2, byteArray)
            statement.execute()


        } catch (e: Exception){
            e.printStackTrace()

        }

        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)



    }
}