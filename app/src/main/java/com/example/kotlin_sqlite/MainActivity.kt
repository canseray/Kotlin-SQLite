package com.example.kotlin_sqlite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieNameArray = ArrayList<String>()
        val movieImageArray = ArrayList<Bitmap>()

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,movieNameArray)
        listView.adapter = arrayAdapter


        try{
            //veriyi çekmek
            val database = this.openOrCreateDatabase("Movies", Context.MODE_PRIVATE,null)
            database.execSQL("CREATE TABLE IF  NOT EXISTS movies (name VARCHAR, image BLOB)")

            val cursor = database.rawQuery("SELECT * FROM movies", null)

            val nameX = cursor.getColumnIndex("name")
            val imageX = cursor.getColumnIndex("image")

            cursor.moveToFirst()

            while (cursor != null){

                movieNameArray.add(cursor.getString(nameX))

                val byteArray = cursor.getBlob(imageX)
                val image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

                movieImageArray.add(image)

                cursor.moveToNext()

                arrayAdapter.notifyDataSetChanged()

                cursor.close()


            }


        }catch (e: Exception){
            e.printStackTrace()

        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val intent = Intent(applicationContext,DetailActivity::class.java)
            intent.putExtra("name",movieNameArray[position])
            intent.putExtra("info","old") //yeni eklemeyle eskiye bakma farkı eskiye bakıcm

            val choosen = Globals.Choosen
            choosen.choosenImage = movieImageArray[position]



            startActivity(intent)


        }






    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.add_movie,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.add_movie){

            val intent = Intent(applicationContext,DetailActivity::class.java)

            intent.putExtra("info","new") //yeni ekliyrum

            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}