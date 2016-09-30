package com.arori4.lookbook.Closet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Cabreros on 05-May-16.
 */
public class PreferenceList implements Serializable {

    public static final String EXTRA_STRING = "PreferenceList181881";

    private Boolean mWorn;

    private String mCategory;

    private String mColor;
    private String mSecondaryColor;
    private String mSize;
    private List<String> mOccasion;
    private String mStyle;
    private String mWeather;

    /* No argument constructor */
    public PreferenceList() {
        this.mWorn = false;
        this.mCategory = null;
        this.mColor = null;
        this.mSize = null;
        this.mOccasion = new ArrayList<String>();
        this.mStyle = null;
        this.mWeather = null;
        this.mSecondaryColor = null;
    }

    public PreferenceList(Boolean mWorn, String mCategory, String mColor, String mSize, List<String> mOccasion, String mStyle, String mWeather, String mSecondaryColor) {
        this();
        this.mWorn = mWorn;
        this.mCategory = mCategory;
        this.mColor = mColor;
        this.mSize = mSize;
        this.mOccasion = mOccasion;
        this.mStyle = mStyle;
        this.mWeather = mWeather;
        this.mSecondaryColor = mSecondaryColor;
    }

    public PreferenceList(boolean mWorn, String mCategory, String mColor, String mSize, List<String> mOccasion, String mStyle, String mWeather, String mSecondaryColor) {
        this(Boolean.valueOf(mWorn), mCategory, mColor, mSize, mOccasion, mStyle, mWeather, mSecondaryColor);
    }

    /* Copy Constructor */
    public PreferenceList(PreferenceList pref) {
        this.mWorn = pref.mWorn;
        this.mCategory = pref.mCategory;
        this.mColor = pref.mColor;
        this.mSize = pref.mSize;
        this.mOccasion = pref.mOccasion;
        this.mStyle = pref.mStyle;
        this.mWeather = pref.mWeather;
        this.mSecondaryColor = pref.mSecondaryColor;
    }

    /* Constructor that modifies one preference */
    public PreferenceList(PreferenceList pref, String attributeType, Object attribute) {
        this(pref);
        System.out.println("PL mod worn " + mWorn);
        System.out.println("attribute " + attribute + " attriType " + attributeType);
        switch(attributeType){
            case "worn":
                mWorn = (Boolean)attribute;
                break;
            case "category":
                mCategory = (String)attribute;
                break;
            case "color":
                mColor = (String)attribute;
                break;
            case "size":
                mSize = (String)attribute;
                break;
            case "occasion":
                mOccasion = (List<String>)attribute;
                break;
            case "style":
                mStyle = (String)attribute;
                break;
            case "weather":
                mStyle = (String)attribute;
                break;
            default: break;
        }
    }

    public PreferenceList(Clothing clothing) {
        this();

        if (clothing != null) {
            this.mWorn = clothing.isWorn();
            this.mCategory = clothing.getCategory();
            this.mColor = clothing.getColor();
            this.mSize = clothing.getSize();
            this.mOccasion.add(clothing.getOccasion());
            this.mStyle = clothing.getStyle();
            this.mWeather = clothing.getWeather();
            this.mSecondaryColor = clothing.getSecondaryColor();
        }
    }

    public Boolean isWorn() {
        return this.mWorn;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getSecondaryColor(){return mSecondaryColor;}

    public String getColor() {
        return mColor;
    }

    public String getSize() {
        return mSize;
    }

    public List<String> getOccasion() {
        return mOccasion;
    }

    public String getStyle() {
        return mStyle;
    }

    public String getWeather() {
        return mWeather;
    }

}