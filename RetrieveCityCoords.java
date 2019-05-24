/**
 * Created by Redmal on 5/24/2019.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;

public class RetrieveCityCoords {
    private String coordString;

    public RetrieveCityCoords(String city1) throws IOException{
        setCoordString(getFormattedCoords(scrapeCityCoords(city1)));
    }

    public void setCoordString(String s){
        coordString = s;
    }

    public String getCoordString(){
        return coordString;
    }

    // get the coordinates of the city specified by the user
    public String scrapeCityCoords(String userSpecifiedCity) throws IOException {
        String cityCoords = "";

        String googleCoordsURL = "http://www.google.com/search?hl=en&btn=1&q=" + userSpecifiedCity + " coordinates";

        Document coordSearchResults = Jsoup.connect(googleCoordsURL).get();

        Elements coordResults = coordSearchResults.getElementsByClass("Z0LcW");

        //filter headers that contain getUserTopic
        GrabCoordsLoop: for (Element coordTextResults : coordResults){
            if (coordTextResults.text() != null){
                cityCoords = coordTextResults.text();
                break GrabCoordsLoop;
            }
        }
        return cityCoords;
    }


    // pass a coordset result here to return a formatted version
    public String getFormattedCoords(String coords){
        String NorS;
        String EorW;
        if (coords.contains("N"))
                NorS = "N";
        else
                NorS = "S";

        if (coords.contains("E"))
            EorW = "E";
        else
            EorW = "W";

        String splitCoords[];
        // 40.7128° N, 74.0060° W
        coords = coords.replace("°", "");
        coords = coords.replace("N", "");
        coords = coords.replace("E", "");
        coords = coords.replace("S", "");
        coords = coords.replace("W", "");
        coords = coords.replaceAll("\\s","");
        // 40.7128,74.0060
        splitCoords = coords.split(",");

        coords = "";
        for (int i = 0; i < 2; i++) {
            double degrees = Double.parseDouble(splitCoords[i]);
            double minutes = (degrees - (int)degrees) * 60;
            double seconds = (minutes - (int) minutes) * 60;
            if (i == 0)
                coords = String.valueOf((int)degrees) + String.valueOf((int) minutes) + String.valueOf((int)seconds) + NorS;
            else
                coords = coords + ", " + String.valueOf((int)degrees) + String.valueOf((int) minutes) + String.valueOf((int)seconds) + EorW;
        }
        return coords;
    }


    public static void main(String[] args) throws IOException{
        String userCity;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter a city name: " );
        userCity = keyboard.nextLine();

        RetrieveCityCoords city = new RetrieveCityCoords(userCity);

        System.out.println("Coordinates for " + userCity + ": " + city.getCoordString());
    }
}
