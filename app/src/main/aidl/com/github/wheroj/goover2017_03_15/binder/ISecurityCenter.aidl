// ISecurityCenter.aidl
package com.github.wheroj.goover2017_03_15.binder;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    /**
    * 加密
    * */
    String encrypt(String pass);

    String decrypt(String pass);
}
