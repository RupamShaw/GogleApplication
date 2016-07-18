package com.jagdiv.android.gogleapplication;

import java.util.Date;

/**
 * Created by Dell on 15/07/2016.
 */
public class PDFEntity {
    public String mName;
   // public String mNumber;
    public boolean mIsSeparator;
    public Date mEnterDt;

    public PDFEntity(String name, boolean isSeparator, Date enterDt) {
        mName = name;
       // mNumber = number;
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


    public void setIsSection(boolean isSection) {
        mIsSeparator = isSection;
    }
}
