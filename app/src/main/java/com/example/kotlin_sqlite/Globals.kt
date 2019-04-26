package com.example.kotlin_sqlite

import android.graphics.Bitmap

class Globals {


    companion object Choosen {

        var choosenImage: Bitmap? = null
        fun returnImage(): Bitmap{
            return choosenImage!!
        }

    }
}