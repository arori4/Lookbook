package com.arori4.lookbook.Lookbook;

import com.arori4.lookbook.Closet.Closet;
import com.arori4.lookbook.Closet.Clothing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Cabreros on 05-May-16.
 */
public class Outfit {

    private List<Clothing> mTops = new ArrayList<>();
    private List<Clothing> mBottoms = new ArrayList<>();
    private Clothing mShoes;
    private List<Clothing> mAccessories = new ArrayList<>();
    private Clothing mHat;

    private String mName;
    private String mOccasion;
    private String mWeather;

    private List<String> mSerializedClothingList = new ArrayList<>();


    public List<String> getSerializedClothingList() {
        return mSerializedClothingList;
    }

    public void setSerializedClothingList(List<String> serializedClothingList) {
        mSerializedClothingList = serializedClothingList;
    }

    public void updateSerializedList() {
        //empty the list
        mSerializedClothingList.clear();

        for (int index = 0; index < mTops.size(); index++) {
            mSerializedClothingList.add(mTops.get(index).getId());
        }
        for (int index = 0; index < mBottoms.size(); index++) {
            mSerializedClothingList.add(mBottoms.get(index).getId());
        }
        if (mShoes != null) {
            mSerializedClothingList.add(mShoes.getId());
        }
        for (int index = 0; index < mAccessories.size(); index++) {
            mSerializedClothingList.add(mAccessories.get(index).getId());
        }
        if (mHat != null) {
            mSerializedClothingList.add(mHat.getId());
        }
    }

    public void initializeFromSerializedList(Closet currentCloset) {
        for (int index = 0; index < mSerializedClothingList.size(); index++) {
            //find the clothing in the fragment_closet
            Clothing currentClothing = currentCloset.findClothingByID(mSerializedClothingList.get(index));

            if (currentClothing == null) {
                System.err.println("Couldn't find clothing with id of " + mSerializedClothingList.get(index));
                System.err.println("Try making sure that the fragment_closet is properly loaded.");
            } else {
                //depending on the type, fill it in to the appropriate place
                String clothingType = currentClothing.getCategory();
                switch (clothingType) {
                    case Clothing.HAT:
                        mAccessories.add(currentClothing);
                        break;
                    case Clothing.ACCESSORY:
                        mAccessories.add(currentClothing);
                        break;
                    case Clothing.BODY:
                        mTops.add(currentClothing);
                        break;
                    case Clothing.BOTTOM:
                        mBottoms.add(currentClothing);
                        break;
                    case Clothing.JACKET:
                        mTops.add(currentClothing);
                        break;
                    case Clothing.SHOE:
                        mShoes = currentClothing;
                        break;
                    case Clothing.TOP:
                        mTops.add(currentClothing);
                        break;
                    default:
                        System.err.println("Invalid clothing category " + clothingType);
                        break;
                }
            }
        }
    }


    /**
     * Wears the outfit
     * Marks all of the outfit as worn
     */
    public void wearOutfit() {
        for (int index = 0; index < mTops.size(); index++) {
            mTops.get(index).setWorn(true);
        }
        for (int index = 0; index < mBottoms.size(); index++) {
            mBottoms.get(index).setWorn(true);
        }
        if (mShoes != null) {
            mShoes.setWorn(true);
        }
        for (int index = 0; index < mAccessories.size(); index++) {
            mAccessories.get(index).setWorn(true);
        }
        if (mHat != null) {
            mHat.setWorn(true);
        }
    }

    public void addAccessory(Clothing clothing) {
        mAccessories.add(clothing);
    }

    public void addTop(Clothing clothing) {
        mTops.add(clothing);
    }

    public void addBottom(Clothing clothing) {
        mBottoms.add(clothing);
    }

    public void setHat(Clothing clothing) {
        mHat = clothing;
    }

    public void setShoes(Clothing clothing) {
        mShoes = clothing;
    }

    public Clothing getHat() {
        return mHat;
    }

    public Clothing getFirstTop() {
        if (mTops.isEmpty()) {
            return null;
        }
        return mTops.get(0);
    }

    public Clothing getFirstBottom() {
        if (mBottoms.isEmpty()) {
            return null;
        }
        return mBottoms.get(0);
    }

    public Clothing getFirstAccessory() {
        if (mAccessories.isEmpty()) {
            return null;
        }
        return mAccessories.get(0);
    }

    public Clothing getShoes() {
        return mShoes;
    }

    public List<Clothing> getTops() {
        return mTops;
    }

    public List<Clothing> getBottoms() {
        return mBottoms;
    }

    public List<Clothing> getAccessories() {
        return mAccessories;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;}

    public boolean hasAllClothing(Closet mContainingCloset) {
        boolean returnVal = true;
        updateSerializedList();

        for (int index = 0; index < mSerializedClothingList.size(); index++){
            if (mContainingCloset.findClothingByID(mSerializedClothingList.get(index)) == null ){
                returnVal = false;
            }
        }

        return returnVal;
    }
}
