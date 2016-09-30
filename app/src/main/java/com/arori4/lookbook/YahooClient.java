package com.arori4.lookbook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import com.android.volley.toolbox.Volley;
//import com.survivingwithandroid.weather.Config;
//import com.survivingwithandroid.weather.MainActivity;
//import com.survivingwithandroid.weather.model.CityResult;
//import com.survivingwithandroid.weather.model.Weather;

/*
 * Copyright (C) 2014 Francesco Azzola - Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class YahooClient {

    /* URL's for requesting WOEID and temperature */
    private String GEO_REQUEST = "http://where.yahooapis.com/v1";
    private String WEATHER_REQUEST = "http://weather.yahooapis.com/forecastrss";
    private String APPID = "dj0yJmk9eWZrYVFRUkc0bmxDJmQ9WVdrOWNrZHFVVmxpTkdNbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1hYw--";

    /* Special value for Rain and Snow */
    private int SNOW = 0xFAABCAFE;
    private int RAIN = 0xFBBCBEEF;

    /*
     * Get the WOEID for a city name
     * @param String city -- name for the place to find a woeid for
     * @return String -- the woeid for input city
     */
    private String getWOEID(String city) {

        /* A list of regularly used cities */
        if (city.equalsIgnoreCase("san diego")) {
            return "2487889";
        }

        /* Construct the request */
        city = city.replaceAll(" ", "%20");
        String WoeidQuery = GEO_REQUEST + "/place.q(" + city + ")?appid=" + APPID;

        /* URL connection for the query */
        HttpURLConnection woeidHC = null;
        String WOEID = null;

        /* Make the query */
        try {
            woeidHC = (HttpURLConnection) (new URL(WoeidQuery)).openConnection();
            woeidHC.connect();

            /* Get the result ready to be parsed */
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(woeidHC.getInputStream()));
            int event = parser.getEventType();

            /* String to store start tag */
            String tag = null;

            /* Parse the response */
            while (event != XmlPullParser.END_DOCUMENT) {

                /* Get START TAG */
                if (event == XmlPullParser.START_TAG) {

                    tag = parser.getName();
                }
                /* Check for TEXT event */
                else if (event == XmlPullParser.TEXT) {

                    /* Find the WOEID */
                    if (tag.equals("woeid")) {

                        /* Set the WOEID */
                        WOEID = parser.getText();
                    }
                }

                /* Proceed to next event */
                event = parser.next();
            } /* End of parsing while loop */
        } /* End of try block */

        /* Disconnect from the query */ catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            woeidHC.disconnect();
        }

        /* Return the result */
        return WOEID;

    } /* End of getWOEID */

    /*
     * Get the temparature of a city given its WOEID.
     * @param String woeid -- the WOEID for the city
     * @return int -- temparature for the city
     */
    private int getTemp(String woeid) {

        /* Construct the request */
        String tempQuery = WEATHER_REQUEST + "?w=" + woeid;

        /* URL connection for the query */
        HttpURLConnection tempHC = null;
        int temp = 0;
        int condition = -1;

        /* Make the query */
        try {
            tempHC = (HttpURLConnection) (new URL(tempQuery)).openConnection();
            tempHC.connect();

            /* Get the result ready to be parsed */
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(tempHC.getInputStream())); //changed from woeidHC.getInputStream()
            int event = parser.getEventType();

            /* String to store start tag */
            String tag = null;

            /* Parse the response */
            while (event != XmlPullParser.END_DOCUMENT) {

                /* Get START TAG */
                if (event == XmlPullParser.START_TAG) {

                    tag = parser.getName();
                }
                /* Check for TEXT event */
                else if (event == XmlPullParser.TEXT) {

                    /* Find the condition element */
                    if (tag.equals("yweather:condition")) {

                        /* Get the temperature attribute */
                        temp = Integer.parseInt(parser.getAttributeValue(null, "temp"));
                        condition = Integer.parseInt(parser.getAttributeValue(null, "code"));
                    }
                }

                /* Proceed to next event */
                event = parser.next();
            } /* End of parsing while loop */
        } /* End of try block */

        /* Disconnect from the query */ catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tempHC.disconnect();
        }

        int code = condition;

        /* If it's raining, return RAIN */
        if ((-1 < code && code < 12) || (36 < code && code < 41) || code == 45 || code == 47) {
            return RAIN;
        }

        /* If it's snowing, return SNOW */
        if ((12 < code && code < 19) || code == 35 || (40 < code && code < 44) || code == 46) {
            return SNOW;
        }

        /* If it's not raining or snowing, return the result */
        return temp;

    }

    /*
     * Convert a temperature to a String that generator could use.
     * @param int temp -- temperature to convert from
     * @return String -- the descriptive String of the temperature
     */
    private String tempToWeather(int temp) {

        String weather = null;

        /* Check if it's raining */
        if (temp == RAIN) {
            return "Rain";
        }

        /* Check if it's snowing */
        if (temp == SNOW) {
            return "Snow";
        }

        /* Cold */
        if (temp < 25) {
            weather = "Cold";
        }

        /* Cool */
        else if (temp < 65) {
            weather = "Cool";
        }

        /* Warm */
        else if (temp < 80) {
            weather = "Warm";
        }

        /* Hot */
        else {
            weather = "Hot";
        }

        return weather;
    }

    /*
     * To be called from generator, go return an appropriate weather field.
     * @param String city -- the city name
     * @return String -- the weather for the city that day
     */
    public String checkWeather(String city) {

        String weather = null;

        /* Get the WOEID for the city */
        String WOEID = getWOEID(city);

        /* Get temperature for that city */
        int temp = getTemp(WOEID);

        /* Return the weather */
        return tempToWeather(temp);

    }
}