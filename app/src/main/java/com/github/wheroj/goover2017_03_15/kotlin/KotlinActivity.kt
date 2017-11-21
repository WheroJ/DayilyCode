package com.github.wheroj.goover2017_03_15.kotlin

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Button
import android.widget.Toast
import com.github.wheroj.goover2017_03_15.R

class KotlinActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        System.out.println(add(3.5, 7.6))

        var  book:Book = Book("1", "")
        // Will print only if artist != null
        print(book?.bookName)

        // Only use it when we are sure it´s not null. Will throw an exception otherwise.
        print(book!!.bookName)

        // Use Elvis operator to give an alternative in case the object is null
        var name:String = (book?.bookName) ?:"empty"

        var art:Artist = null!!
        var art2:Artist = Artist(1, "", "", 1)

        var user:User = User()
    }

    fun onClickView(view:Button):String{
        view.setOnClickListener {
            toast("哈哈哈")
        }
        return ""
    }

    fun add(a:Double, b:Double):Double{
        return a+b
    }

    fun FragmentActivity.toast(content:String, time:Int=Toast.LENGTH_SHORT){
        Toast.makeText(this, content, time).show()
    }
}
