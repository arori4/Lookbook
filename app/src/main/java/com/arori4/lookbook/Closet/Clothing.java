package com.arori4.lookbook.Closet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Christopher Cabreros on 03-May-16.
 * This is a temporary class that contains images
 */
public class Clothing implements Parcelable {

    public static final String ACCESSORY = "Accessory";
    public static final String TOP = "Top";
    public static final String BOTTOM = "Bottom";
    public static final String SHOE = "Shoe";
    public static final String BODY = "Body";
    public static final String HAT = "Hat";
    public static final String JACKET = "Jacket";

    public static final String MINUS = "Minus";

    public static final String EXTRA_STRING = "clothing";
    public static final String EXTRA_TYPE_STRING = "TYPE";

    private boolean mWorn;
    private boolean mShared;
    private boolean mLost;

    private String mCategory;
    private String mColor;
    private String mSize;
    private String mOccasion;
    private String mStyle;
    private String mWeather;
    private String mNotes; //might change implementation
    private String mSecondaryColor;
    private String mId;

    private Bitmap mBitmap;

    public Clothing(){
    }
    public Clothing(String cat, String col, String weat, String occ, String not, boolean wor, boolean shar, boolean los, String id ) {

        mWorn = wor;
        mShared = shar;
        mLost = los;

        mCategory = cat;
        mColor = col;
        mOccasion = occ;
        mWeather = weat;
        mNotes = not;

        mId = id;


    }

    protected Clothing(Parcel in) {
        mWorn = in.readByte() != 0;
        mShared = in.readByte() != 0;
        mLost = in.readByte() != 0;
        mCategory = in.readString();
        mColor = in.readString();
        mSize = in.readString();
        mOccasion = in.readString();
        mStyle = in.readString();
        mWeather = in.readString();
        mNotes = in.readString();
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        mSecondaryColor = in.readString();
        mId = in.readString();
    }

    public static final Creator<Clothing> CREATOR = new Creator<Clothing>() {
        @Override
        public Clothing createFromParcel(Parcel in) {
            return new Clothing(in);
        }

        @Override
        public Clothing[] newArray(int size) {
            return new Clothing[size];
        }
    };

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public Boolean isWorn() {
        return mWorn;
    }

    public void setWorn(boolean worn) {
        mWorn = worn;
    }

    public Boolean isShared() {
        return mShared;
    }

    public void setShared(boolean shared) {
        mShared = shared;
    }

    public Boolean isLost() {
        return mLost;
    }

    public void setLost(boolean lost) {
        mWorn = lost;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getOccasion() {
        return mOccasion;
    }

    public void setOccasion(String occasion) {
        mOccasion = occasion;
    }

    public String getStyle() {
        return mStyle;
    }

    public void setStyle(String style) {
        mStyle = style;
    }

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getSecondaryColor() {return mSecondaryColor;}

    public String getId() { return mId;}

    public void setId(String id){ mId = id;}

    public void setSecondaryColor(String SecondaryColor) { mSecondaryColor = SecondaryColor; }

    public void updateClothing(Clothing copy) {
        mWorn = copy.isWorn();
        mShared = copy.isShared();
        mLost = copy.isLost();
        mCategory = copy.getCategory();
        mColor = copy.getColor();
        mSize = copy.getSize();
        mOccasion = copy.getOccasion();
        mStyle = copy.getStyle();
        mWeather = copy.getWeather();
        mNotes = copy.getNotes();
        mBitmap = copy.getBitmap();
        mSecondaryColor = copy.getSecondaryColor();
        mId = copy.getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mWorn ? 1 : 0));
        dest.writeByte((byte) (mShared ? 1 : 0));
        dest.writeByte((byte) (mLost ? 1 : 0));
        dest.writeString(mCategory);
        dest.writeString(mColor);
        dest.writeString(mSize);
        dest.writeString(mOccasion);
        dest.writeString(mStyle);
        dest.writeString(mWeather);
        dest.writeString(mNotes);
        dest.writeParcelable(mBitmap, flags);
        dest.writeString(mSecondaryColor);
        dest.writeString(mId);
    }


}
