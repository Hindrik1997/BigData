package VZ.Abstractions;

import javax.swing.*;

import VZ.Main;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hindrik on 28-1-17.
 * Wrapped code by Jacob and Romeo in class.
 */

public class MapsPanel extends JPanel {

    private Browser _browser;
    private BrowserView _browser_view;
    private File _html;
    private boolean _query = true;


    public MapsPanel(boolean query)
    {
        _browser = new Browser();
        _browser_view = new BrowserView(_browser);
        _query = query;

        _html = new File("maps.html");
        _browser.loadURL(_html.getAbsolutePath());

        init();

    }

    void init()
    {
        JTextField searchMovie = new JTextField();
        searchMovie.setColumns(30);

//        JButton switchButton = new JButton("Movie location");
//        switchButton.addActionListener(ae -> {
//            _query = !_query;
//            if(_query){
//                switchButton.setText("Movie location");
//            }
//            else{
//                switchButton.setText("Actor birthlocation");
//            }
//        });

        JButton searchButton = new JButton("Zoeken");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                clearMarkers(_browser);
                String movie = searchMovie.getText();

                if (movie != null){
                    //locaties bij film ophalen
                    ResultSet rs = getLocationsFromMovie(movie);
                    if (rs != null){
                        try {
                            //door resultaten lopen
                            while(rs.next( ))
                            {
                                if(_query)
                                    createMarker(_browser, rs.getString(1));
                                else
                                {
                                    createMarker(_browser, rs.getString(1));
                                    _browser.executeJavaScript("google.maps.event.addListener(markers[markers.length - 1] , 'click', function(){ var infowindow = new google.maps.InfoWindow({ content:'" + rs.getString(2) + "', position: myLatLng, }); infowindow.open(map, this);});");
                                }
                                System.out.println("1 " + rs.getString(1));
                                //System.out.println("2 " + rs.getString(2));

                                try {
                                    Thread.sleep(1000);
                                } catch(InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        } catch (SQLException err){

                        }
                    }else{
                        System.out.println("er gaat iets fout");
                    }
                    setMarkers(_browser);
                    zoomOutVisibleMarkers(_browser);
                }
            }
        });

        JPanel toolBar = new JPanel();
        toolBar.add(searchMovie);
        toolBar.add(searchButton);
//        toolBar.add(switchButton);

        setLayout(new BorderLayout());
        add(_browser_view, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        setSize(700, 500);
        setVisible(true);
    }

    public ResultSet getLocationsFromMovie(String movie){
            String SQL = switchQuery(_query, movie);
            return Main.getInstance().executeQuery(SQL);
    }

    public void clearMarkers (Browser browser){

        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) { markers[i].setMap(null);}");
        browser.executeJavaScript("var markers = [];");
    }

    public void setMarkers (Browser browser){
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) { markers[i].setMap(map);}");
    }

    public void zoomOutVisibleMarkers(Browser browser){
        browser.executeJavaScript("var bounds = new google.maps.LatLngBounds();");
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) {bounds.extend(markers[i].getPosition());}");
        browser.executeJavaScript("map.fitBounds(bounds);");
    }

    public String switchQuery(boolean query, String par){
        if(query)
            return "SELECT L.location FROM final.location L INNER JOIN final.movie_location ML ON L.location_id=ML.location_id INNER JOIN final.movie M ON ML.movie_id = M.movie_id WHERE m.title = '" + par + "';";
        else
            return "SELECT birth_location, actor_name FROM final.actors WHERE actors.actor_name LIKE '%" + par + "%' AND actors.birth_location IS NOT NULL;";
    }

    public void createMarker(Browser browser, String location){
        //regex voor locatie ()
        Pattern p = Pattern.compile("(.*)\\((?:.+)\\)(.*)");
        Matcher m = p.matcher(location);
        if (m.matches()){
            location = m.group(1);
        }

        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyB7J1zsErb9_7jxNu5KU5kIENFObAQEbl0");
        GeocodingResult[] results = null;
        String country = null;

        try {
            results = GeocodingApi.geocode(context, location).await();
        } catch (Exception ex) {
            results = null;
        }

        if (results != null){
            for (int i =0; i < results[0].addressComponents.length; i++){
                if (results[0].addressComponents[i].types[0] == AddressComponentType.COUNTRY){
                    country = results[0].addressComponents[i].longName;
                }
            }
            browser.executeJavaScript("var myLatLng = {lat: " + results[0].geometry.location.lat + ", lng: " + results[0].geometry.location.lng + "};");
            browser.executeJavaScript("var map = new google.maps.Map(document.getElementById('map-canvas'), {zoom: 12, center: myLatLng});");
            browser.executeJavaScript("var marker = new google.maps.Marker({ position: myLatLng, map: map, clickable: true});");
            //browser.executeJavaScript("var lookup = []; lookup.push([lat, lang]); var search = [lat, lang");
            //boolean isLocationFree = browser.executeJavaScriptAndReturnValue("for (var i = 0, l = lookup.length; i < l; i++) {    if (lookup[i][0] === search[0] && lookup[i][1] === search[1]) {      return false;    }  }  return true;").getBooleanValue();

            browser.executeJavaScript("markers.push(marker);");
            if(_query)
                browser.executeJavaScript("google.maps.event.addListener(markers[markers.length - 1] , 'click', function(){ var infowindow = new google.maps.InfoWindow({ content:'" + country + "', position: myLatLng, }); infowindow.open(map, this);});");

        }else{
            System.out.println("Geen locaties gevonden");
        }
    }



}
