// IBookManager.aidl
package com.github.wheroj.goover2017_03_15;

// Declare any non-default types here with import statements
import com.github.wheroj.goover2017_03_15.Book;
import com.github.wheroj.goover2017_03_15.IOnNewBookArrivedListener;
interface IBookManager {
    void addBook(in Book book);
    List<Book> getBookList();
    void registerListener(in IOnNewBookArrivedListener listener);
    void unRegisterListener(in IOnNewBookArrivedListener listener);
}
