package com.github.wheroj.goover2017_03_15;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shopping on 2017/4/18 10:21.
 *
 * @description
 */

public class Book implements Parcelable {
    public String bookId;

    public String bookName;

    protected Book(Parcel in) {
        readFromParcel(in);
    }

    public Book(String bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(bookName);
    }

    //从Parcel中重建对象
    public void readFromParcel(Parcel in) {
        bookId = in.readString();
        bookName = in.readString();
    }
}
