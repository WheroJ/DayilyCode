// IOnNewBookArrivedListener.aidl
package com.github.wheroj.goover2017_03_15;

// Declare any non-default types here with import statements

import com.github.wheroj.goover2017_03_15.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
