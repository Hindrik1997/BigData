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
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private boolean _queryBool = true;

    /**
     * Provides an easy way to show locations on a map.
     * @param queryBool
     */
    public MapsPanel(boolean queryBool)
    {
        _browser = new Browser();
        _browser_view = new BrowserView(_browser);
        _queryBool = queryBool;

        _html = new File("maps.html");
        _browser.loadURL(_html.getAbsolutePath());

        init();
    }

    /**
     * Initializer
     */
    void init()
    {
        JTextArea results = new JTextArea(6, 30);
        JTextField search = new JTextField();
        JScrollPane resultsScroll = new JScrollPane(results);
        search.setColumns(30);
        results.setEditable(false);

        JButton searchButton = new JButton("Zoeken");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                clearMarkers(_browser);
                String searchText = search.getText();
                int countRows = 0;
                String addText = "";

                if (_queryBool){
                    results.setText("In deze landen heeft deze film zich afgespeeld: \n");
                }

                if (searchText != null){
                    //locaties bij film ophalen
                    ResultSet rs = executeQuery(searchText);
                    if (rs != null){
                        try {
                            //door resultaten lopen
                            while(rs.next( ))
                            {
                                if(_queryBool){
                                    String country = createMarker(_browser, rs.getString(1));
                                    if (!results.getText().contains(country))
                                    {
                                        results.append(country + "\n");
                                    }
                                }
                                else
                                {
                                    countRows++;
                                    addText += String.format("%-40s%s" , rs.getString(2), createMarker(_browser, rs.getString(1)) ) + "\n";
                                    _browser.executeJavaScript("google.maps.event.addListener(markers[markers.length - 1] , 'click', function(){ var infowindow = new google.maps.InfoWindow({ content:'" + rs.getString(2) + "', position: myLatLng, }); infowindow.open(map, this);});");
                                }

                                try {
                                    Thread.sleep(1000);
                                } catch(InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                        catch (SQLException err)
                        {

                        }
                    }
                    else{
                        System.out.println("er gaat iets fout");
                    }
                    
                    if (!_queryBool){
                        results.setText("Er zijn " + countRows + " acteurs met deze naam:" + "\n");
                        results.append(addText);
                    }
                    setMarkers(_browser);
                    zoomOutVisibleMarkers(_browser);
                }
            }
        });

        JPanel toolBar = new JPanel(new GridLayout(0,2));
        JPanel left = new JPanel();
        left.add(search);
        left.add(searchButton);

        String _question_false = "<html>Maak een kaart (b.v. google maps/ <br>openstreetview) met gebooorteplaatsen van acteurs. Zodat op de kaart<br> te zien is wie waar geboren is.";
        String _question_true = "<html>Maak een kaart (b.v. google maps / <br>openstreetview) met landen waar een flim speelt. Zodat op de kaart<br> te zien is waar de films spelen.";
        JLabel label = new JLabel(_queryBool ? _question_true : _question_false);
        left.add(label);
        toolBar.add(left);

        toolBar.add(resultsScroll);

        setLayout(new BorderLayout());
        add(_browser_view, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        setSize(700, 500);
        setVisible(true);
    }

    /**
     * Executes SQL query.
     * @param searchText record to search to.
     * @return results from query.
     */
    public ResultSet executeQuery(String searchText){
        String SQL = switchQuery(_queryBool, searchText);
        return Main.getInstance().executeQuery(SQL);
    }

    /**
     * Clears markers.
     * @param browser browser to use.
     */
    public void clearMarkers (Browser browser){
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) { markers[i].setMap(null);}");
        browser.executeJavaScript("var markers = [];");
    }

    /**
     * Sets marker.
     * @param browser browser to use.
     */
    public void setMarkers (Browser browser){
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) { markers[i].setMap(map);}");
    }

    /**
     * Zoom out until all markers are visible.
     * @param browser browser to use.
     */
    public void zoomOutVisibleMarkers(Browser browser){
        browser.executeJavaScript("var bounds = new google.maps.LatLngBounds();");
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) {bounds.extend(markers[i].getPosition());}");
        browser.executeJavaScript("map.fitBounds(bounds);");
    }

    /**
     * Switch to right query based on bool.
     * @param queryBool bool to switch query.
     * @param searchText record to search to.
     * @return query to use.
     */
    public String switchQuery(boolean queryBool, String searchText){
        if(queryBool)
            return "SELECT L.location FROM final.location L INNER JOIN final.movie_location ML ON L.location_id=ML.location_id INNER JOIN final.movie M ON ML.movie_id = M.movie_id WHERE m.title = '" + searchText + "';";
        else
            return "SELECT birth_location, actor_name FROM final.actors WHERE actors.actor_name LIKE '%" + searchText + "%' AND actors.birth_location IS NOT NULL;";
    }

    /**
     * Create marker based on location.
     * @param browser
     * @param location
     * @return country of location.
     */
    public String createMarker(Browser browser, String location){
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

            browser.executeJavaScript("markers.push(marker);");
            if(_queryBool){
                browser.executeJavaScript("google.maps.event.addListener(markers[markers.length - 1] , 'click', function(){ var infowindow = new google.maps.InfoWindow({ content:'" + country + "', position: myLatLng, }); infowindow.open(map, this);});");
                return country;
            }
            else {
                return results[0].formattedAddress;
            }
        }
        else{
            return "Geen locaties gevonden";
        }
    }
}
