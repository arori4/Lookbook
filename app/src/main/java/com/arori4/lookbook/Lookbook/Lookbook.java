package com.arori4.lookbook.Lookbook;

import com.arori4.lookbook.Closet.Closet;
import com.arori4.lookbook.Closet.Clothing;
import com.arori4.lookbook.Closet.PreferenceList;
import com.arori4.lookbook.YahooClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Christopher Cabreros on 05-May-16.
 * Defines the Lookbook
 */
public class Lookbook {

    private List<Outfit> mOutfitList;
    private Closet mBelongingCloset;

    public Lookbook() {
        mOutfitList = new ArrayList<>();
    }

    public void assignBelongingCloset(Closet closet) {
        mBelongingCloset = closet;
    }

    public void deserializeAllOutfits(List<List<String>> list){
        if (mBelongingCloset == null){
            System.err.println("Lookbook does not have a belonging fragment_closet.");
        }
        for (int index = 0; index < list.size(); index++){
            Outfit newOutfit = new Outfit();
            newOutfit.setSerializedClothingList(list.get(index));
            newOutfit.initializeFromSerializedList(mBelongingCloset);
            mOutfitList.add(newOutfit);
        }
    }

    public List<List<String>> createSerializedList(){
        List<List<String>> returnList = new ArrayList<>();
        for (int index = 0; index < mOutfitList.size(); index++){
            returnList.add(mOutfitList.get(index).getSerializedClothingList());
        }
        return returnList;
    }

    public boolean writeToDatabase() {
        return false; //returns true if written successfully.
    }

    public boolean readFromDatabase() {
        return false;//return true if read successfully
    }

    /*
     * Takes in the PreferenceList and chooses an outfit accordingly.
     * @param preferenceList
     * @return Outfit
     */
    public Outfit generateOutfit(PreferenceList preferenceList) {

		Clothing jacket = null;
        Clothing shirt = null;
        Clothing pants = null;
        Clothing shoes = null;
        Clothing acce = null;
        Clothing hat = null;

        String cat = "category";

        Outfit result = new Outfit();

        /* PrefList with top */
        PreferenceList shirtPref = new PreferenceList(
                preferenceList, cat, Clothing.TOP);

        /* Get a shirt */
        shirt = pickOne(shirtPref);

		/* Pants should match the shirt */
        PreferenceList pantsPref = new PreferenceList(shirt);
        pantsPref = new PreferenceList(pantsPref, cat, Clothing.BOTTOM);

        /* Get pants */
        pants = pickOne(pantsPref);

        /* Get shoes */
        PreferenceList shoesPref = new PreferenceList(shirtPref,
                cat, Clothing.SHOE);
        shoes = pickOne(shoesPref);

		/* 20% chance there will be an hat */
        Random randHat = new Random();
        int iHat = randHat.nextInt(5);
        if (iHat == 0) {
            PreferenceList hatPref = new PreferenceList(shirt);
            hatPref = new PreferenceList(hatPref, cat, Clothing.HAT);
            hat = pickOne(hatPref);
        }

		/* 20% chance there will be a jacket, if it's cold then 50% */
        Random randJac = new Random();
        int iJac = randJac.nextInt(5);
		if ("cold".equals("cold")){
			iJac = randJac.nextInt(2);
		}
        if (iHat == 0) {
            PreferenceList jacPref = new PreferenceList(shirt);
            jacPref = new PreferenceList(jacPref, cat, Clothing.JACKET);
            jacket = pickOne(jacPref);
        }

        if (result.getFirstTop() == null || result.getFirstBottom() == null || result.getShoes() == null) {
            return generateRandomOutfit();
        }

        /* Construct the outfit */
        result.addTop(shirt);
        result.addBottom(pants);
        result.setShoes(shoes);
		if(hat != null){
			result.setHat(hat);
		}
		if(jacket != null){
			result.addTop(jacket);
		}

        return result;
    }

    private String colorMatches(String color) {

        List<String> colorL = new ArrayList<>();

        switch (color) {
            case "red":
                colorL.add("Blue");
                colorL.add("Black");
                //blue, black
                break;
            case "green":
                colorL.add("Blue");
                colorL.add("Black");
                colorL.add("White");
                colorL.add("Grey");
                // blue, black, white, grey
                break;
            case "blue":
                colorL.add("Yellow");
                colorL.add("Black");
                colorL.add("White");
                colorL.add("Grey");
                colorL.add("Purple");
                colorL.add("Brown");
                colorL.add("Pink");
                colorL.add("Red");
                colorL.add("Green");
                colorL.add("Orange");
                // yellow, black, white, grey, purple, brown, pink, red, green
                break;
            case "yellow":
                colorL.add("White");
                colorL.add("Grey");
                //white, grey
                break;
            case "black":
                colorL.add("Blue");
                colorL.add("Black");
                colorL.add("White");
                colorL.add("Grey");
                colorL.add("Purple");
                colorL.add("Brown");
                colorL.add("Pink");
                colorL.add("Red");
                colorL.add("Green");
                //everything
                break;
            case "white":
                colorL.add("Blue");
                colorL.add("Green");
                colorL.add("Yellow");
                colorL.add("Black");
                colorL.add("Grey");
                colorL.add("Brown");
                colorL.add("Pink");
                colorL.add("Orange");
                //green, blue, yellow, black, grey, brown, pink, orange
                break;
            case "grey":
                colorL.add("Blue");
                colorL.add("Green");
                colorL.add("Black");
                colorL.add("White");
                //green, blue, black, white
                break;
            case "purple":
                colorL.add("Blue");
                colorL.add("Black");
                //blue, black
                break;
            case "orange":
                colorL.add("Blue");
                colorL.add("White");
                //blue, white
                break;
            case "brown":
                colorL.add("Blue");
                colorL.add("Black");
                colorL.add("White");
                //blue, black, white
                break;
            case "pink":
                colorL.add("Blue");
                colorL.add("Black");
                colorL.add("White");
                //blue, black, white
                break;
            default:
                break;
        }
        Random random = new Random();

        String result = null;
        if (colorL != null && colorL.size() > 0) {
            result = colorL.get(random.nextInt(colorL.size()));
        }
        return result;

    }

    /*
     * Pick one clothing article given a preference list by calling filter
	 * multiple times.
	 * Current fields used in filter: Color, SecondaryColor, Occasion, Weather,
	 * Worn.
     * @param PreferenceList
     * @return Clothing
     */
    private Clothing pickOne(PreferenceList prefList) {

        List<Clothing> match = null;

		/* Local for all fields in prefList*/
        Boolean worn = prefList.isWorn();
        String category = prefList.getCategory();
        String color = prefList.getColor();
        String secColor = prefList.getSecondaryColor();
        String size = prefList.getSize();
        String occasion;
        if (prefList.getOccasion() != null && prefList.getOccasion().size() >= 1) {
            occasion = prefList.getOccasion().get(0);
        }
        String style = prefList.getStyle();
        String weather = prefList.getWeather();

        String attriWorn = "worn";
        String attriWeather = "weather";
        String attriTop = "Top";
        String attriColor = "color";
        String attriOcca = "occasion";

        /* Do we need to pick color or use the one passed in */
        if (category.equals(attriTop) && color != null) {
            color = colorMatches(color);
        }

        /* Create a new preference list with this color */
        PreferenceList first = new PreferenceList(prefList, attriColor, color);

        /* Do we need to get current weather information */
        if (weather == null) {

            YahooClient client = new YahooClient();

            //TODO: where to get the location name?
            //TODO: temporarily weather is always warm until client works
            //weather = client.checkWeather("san diego");
            weather = "warm";
        }

        /* Filter for the perfect list */
        if (mBelongingCloset != null) {
            match = mBelongingCloset.filter(first);
        }

		/* Next filter */
        if (match == null) {
            /* If color is set, then consider color first */
            PreferenceList third = new PreferenceList(first,
                    attriWeather, null);
            /* Delete lowest priority weather field */
            if (mBelongingCloset != null) {
                match = mBelongingCloset.filter(third);
            }


            /* Delete occasion field */
            PreferenceList fifth = new PreferenceList(third, attriOcca, null);
            if (match == null) {
                match = mBelongingCloset.filter(fifth);
            }

			/* If color is not set, occasion is primary and weather follows */
            PreferenceList seventh = new PreferenceList(fifth, attriColor, null);
            if (match == null) {
                match = mBelongingCloset.filter(seventh);
            }

        }

		/* If still nothing is found, then pick fails */
        if (match == null || match.isEmpty()) {
            return null;

        }

        /* Randomly choose one from the list */
        Random random = new Random();
        int index = 0;
        index = random.nextInt(match.size());

        return match.get(index);
    }

    /*
     * Randomly pick a clothing from each category.
     * @return Outfit
     */
    public Outfit generateRandomOutfit() {

        Random random = new Random();

        Clothing accessory = null;
        Clothing shirt = null;
        Clothing pants = null;
        Clothing shoes = null;
        Clothing hat = null;

        Outfit result = new Outfit();

        //accessories
        if (random.nextInt(4) == 0) {
            PreferenceList accessoryPref = new PreferenceList
                    (false, Clothing.ACCESSORY, null, null, null, null, null, null);
            List<Clothing> accessoryList = mBelongingCloset.filter(accessoryPref);
            if (!accessoryList.isEmpty()) { //nullptr check
                accessory = accessoryList.get(random.nextInt(accessoryList.size()));
                result.addAccessory(accessory);
            }
        }

        //top
        PreferenceList topPref = new PreferenceList
                (false, Clothing.TOP, null, null, null, null, null, null);
        List<Clothing> topList = mBelongingCloset.filter(topPref);
        if (!topList.isEmpty()) { //nullptr check
            shirt = topList.get(random.nextInt(topList.size()));
            result.addTop(shirt);
        }

        //bottom
        PreferenceList pantsPref = new PreferenceList
                (false, Clothing.BOTTOM, null, null, null, null, null, null);
        ;
        List<Clothing> bottomList = mBelongingCloset.filter(pantsPref);
        if (!bottomList.isEmpty()) {
            pants = bottomList.get(random.nextInt(bottomList.size()));
            result.addBottom(pants);
        }

        //shoes
        PreferenceList shoesPref = new PreferenceList
                (false, Clothing.SHOE, null, null, null, null, null, null);
        ;
        List<Clothing> shoesList = mBelongingCloset.filter(shoesPref);
        if (!shoesList.isEmpty()) {
            shoes = shoesList.get(random.nextInt(shoesList.size()));
            result.setShoes(shoes);
        }

        //hat
        if (random.nextInt(4) == 0) {
            PreferenceList hatPref = new PreferenceList
                    (false, Clothing.HAT, null, null, null, null, null, null);
            ;
            List<Clothing> hatList = mBelongingCloset.filter(hatPref);
            if (!hatList.isEmpty()) {
                hat = hatList.get(random.nextInt(hatList.size()));
                result.setHat(hat);
            }
        }

        return result;
    }

    private List<Clothing> filter(PreferenceList topPref) {
        return new ArrayList<>();
    }

    public List<Outfit> getOutfitList() {
        return mOutfitList;
    }

    public void addOutfit(Outfit out) {
        mOutfitList.add(out);
    }

    public void removeOutfit(Outfit out) {
        mOutfitList.remove(out);
    }

}
