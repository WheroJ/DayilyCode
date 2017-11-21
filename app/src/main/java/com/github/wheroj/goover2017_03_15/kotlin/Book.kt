package com.github.wheroj.goover2017_03_15.kotlin

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by shopping on 2017/4/18 10:21.

 * @description
 */

class Book : Parcelable {
    var bookId: String = ""

    var bookName: String = ""

    protected constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    constructor(bookId: String, bookName: String) {
        this.bookId = bookId
        this.bookName = bookName
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(bookId)
        dest.writeString(bookName)
    }

    //从Parcel中重建对象
    fun readFromParcel(`in`: Parcel) {
        bookId = `in`.readString()
        bookName = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Book> = object : Parcelable.Creator<Book> {
            override fun createFromParcel(`in`: Parcel): Book {
                return Book(`in`)
            }

            override fun newArray(size: Int): Array<Book?> {
                return arrayOfNulls(size)
            }
        }
    }
}
