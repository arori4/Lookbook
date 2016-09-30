package com.arori4.lookbook.Closet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Christopher Cabreros on 03-May-16.
 * Temporary class to read from
 */
public class Closet {

    private ArrayList<Clothing> list_clothes;
    private ArrayList<String> list_id;
    private boolean updated;

    public Closet(){
        list_clothes = new ArrayList<>();
        list_id = new ArrayList<>();
    }

    /**
     * This method will return all clothing matching the preferences given
     * Used in search, filter, etc.
     * @param pref
     * @return
     */
    public List<Clothing> filter(PreferenceList pref){
        List<Clothing> filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> temp_clothes = new ArrayList<Clothing>();

        List<Clothing> worn_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> category_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> color_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> size_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> occasion_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> style_filtered_clothes = new ArrayList<Clothing>();
        List<Clothing> weather_filtered_clothes = new ArrayList<Clothing>();

        int index = 0;
        int indexTwoF = 0;
        int numberOfFilters = 0;

        //filter for worn clothes
        //boolean type
        if (pref.isWorn()) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                if (list_clothes.get(index).isWorn())
                    worn_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = worn_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < worn_filtered_clothes.size(); indexTwoF++) {
                        if(worn_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(worn_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by category
        //string type
        if (pref.getCategory() != null) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                System.out.println(list_clothes.get(index).getCategory() + " " + pref.getCategory());
                if ((list_clothes.get(index).getCategory()).equals(pref.getCategory()))
                    category_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = category_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < category_filtered_clothes.size(); indexTwoF++) {
                        if(category_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(category_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by color
        //string type
        if (pref.getColor() != null) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                if ((list_clothes.get(index).getColor()).equals(pref.getColor()))
                    color_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = color_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < color_filtered_clothes.size(); indexTwoF++) {
                        if(color_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(color_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by size
        //string type
        if (pref.getSize() != null) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                if ((list_clothes.get(index).getSize()).equals(pref.getSize()))
                    size_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = size_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < size_filtered_clothes.size(); indexTwoF++) {
                        if(size_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(size_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by occasion
        //List<String> type
        if (pref.getOccasion() != null) {
            numberOfFilters++;
            int indexOL = 0;
            int indexLC = 0;

            List<String> pref_occasion_list = pref.getOccasion();

            for(indexOL = 0; indexOL < pref_occasion_list.size(); indexOL++) { //each preference occasion
                for (index = 0; index < list_clothes.size(); index++) { //each article of clothing in list
                    if ((list_clothes.get(index).getOccasion()).equals(pref_occasion_list.get(indexOL)))
                        occasion_filtered_clothes.add(list_clothes.get(index));
                }
            }

            occasion_filtered_clothes = new ArrayList<Clothing>(new LinkedHashSet<Clothing>(occasion_filtered_clothes));

            if(numberOfFilters == 1)
                filtered_clothes = occasion_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < occasion_filtered_clothes.size(); indexTwoF++) {
                        if(occasion_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(occasion_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by style
        //string type
        if (pref.getStyle() != null) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                if ((list_clothes.get(index).getStyle()).equals(pref.getStyle()))
                    style_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = style_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < style_filtered_clothes.size(); indexTwoF++) {
                        if(style_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(style_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //filter by weather
        //string type
        if (pref.getWeather() != null) {
            numberOfFilters++;
            for (index = 0; index < list_clothes.size(); index++) {
                if ((list_clothes.get(index).getWeather()).equals(pref.getWeather()))
                    weather_filtered_clothes.add(list_clothes.get(index));
            }

            if(numberOfFilters == 1)
                filtered_clothes = weather_filtered_clothes;
            else {
                for(index = 0; index < filtered_clothes.size(); index++) {
                    for(indexTwoF = 0; indexTwoF < weather_filtered_clothes.size(); indexTwoF++) {
                        if(weather_filtered_clothes.get(indexTwoF).equals(filtered_clothes.get(index)))
                            temp_clothes.add(weather_filtered_clothes.get(indexTwoF));
                    }
                }
                filtered_clothes = temp_clothes;
            }
        }

        //if no filters
        if(numberOfFilters == 0) {
            return list_clothes;
        }
        //if multiple filters
        else if(numberOfFilters > 0) {
            System.out.println(filtered_clothes.size());
            return filtered_clothes;
        }
        //if some kind of error
        else {
            return null;
        }
    }

    public Clothing findClothingByHash(int hashCode){
        if (hashCode == 0){
            System.err.println("Hash code is 0 in findClothingByHash in Closet.java");
            return null;
        }

        System.err.println("Finding clothing by hash in list of size " + list_clothes.size());

        for (int index = 0; index < list_clothes.size(); index++){
            if (list_clothes.get(index).hashCode() == hashCode){
                System.err.println("Successfully found the clothing");
                return list_clothes.get(index);
            }
        }
        System.err.println("Could not find clothing by hash");
        return null;
    }

    public Clothing findClothingByID(String ID){
        if (ID == ""){
            System.err.println("String ID is blank in findClothingByID in Closet.java");
            return null;
        }

        System.err.println("Finding clothing by hash in list of size " + list_clothes.size());

        for (int index = 0; index < list_clothes.size(); index++){
            if (list_clothes.get(index).getId().equals(ID) ){
                System.err.println("Successfully found the clothing");
                return list_clothes.get(index);
            }
        }
        System.err.println("Could not find clothing by ID");
        return null;
    }

    public boolean writeToDatabase(){
        return false; //returns true if written successfully.
    }

    public boolean readFromDatabase(){
        return false;//return true if read successfully
    }

    public void addClothing(Clothing val){
        list_clothes.add(val);
    }

    public void removeClothing(Clothing val){
        //val.setLost(true);
        removeId(val.getId());
        list_clothes.remove(val);
    }

    /**
     * Someone think dialog_about this
     */
    public void getClothing(){

    }
    public List<String> getId() {return list_id;}

    void addId(String id){ list_id.add(id);}


    void removeId(String id) { list_id.remove(id); }

    public ArrayList<String> getIdList() { return list_id; }


    public void setIdList(ArrayList<String> list) { list_id = list;}

    public List<Clothing> getList(){
        return list_clothes;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
