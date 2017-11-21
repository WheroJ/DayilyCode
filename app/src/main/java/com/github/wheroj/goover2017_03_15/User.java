package com.github.wheroj.goover2017_03_15;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shopping on 2017/4/13 16:51.
 *
 * @description
 */

public class User implements Parcelable {

    public int userId;

    public String userName;

    public boolean isMale;

    protected User(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(isMale?1:0);
    }
    //从Parcel中重建对象
    public void readFromParcel(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
    }
}