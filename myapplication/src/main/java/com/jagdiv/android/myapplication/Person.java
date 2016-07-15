package com.jagdiv.android.myapplication;

import java.util.Date;

/**
 * Created by Dell on 15/07/2016.
 */
public class Person {
    public String mName;
    public String mNumber;
    public boolean mIsSeparator;
    public Date mEnterDt;

    public Person(String name, String number, boolean isSeparator,Date enterDt ) {
        mName = name;
        mNumber = number;
        mIsSeparator = isSeparator;
    mEnterDt=enterDt;
    }

    public void setName(String name) {
        mName = name;
    }
    public String getName() {
        return mName;
    }
    public void setEnterDt(Date enterDt) {
        this.mEnterDt = enterDt;
    }
    public Date getEnterDt() {
        return mEnterDt;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public void setIsSection(boolean isSection) {
        mIsSeparator = isSection;
    }
}
