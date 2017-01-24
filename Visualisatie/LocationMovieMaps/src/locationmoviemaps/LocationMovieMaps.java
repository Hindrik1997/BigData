/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locationmoviemaps;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.google.maps.*;
import com.google.maps.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jacob
 */
public class LocationMovieMaps {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Connection con = connectDB();
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        File resourcesDirectory = new File("src/locationmoviemaps/");
        browser.loadURL( resourcesDirectory.getAbsolutePath() + "\\maps.html");
        
        JTextField searchMovie = new JTextField();
        searchMovie.setColumns(30);
        
        JButton zoomInButton = new JButton("Zoek knop denk ik");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                clearMarkers(browser);
                String movie = searchMovie.getText();
                
                if (movie != null){
                    //locaties bij film ophalen
                    ResultSet rs = getLocationsFromMovie(con, movie);
                    if (rs != null){
                        try {
                        //door resultaten lopen
                        while(rs.next( ))
                        {
                            createMarker(browser, rs.getString(1));
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
                    setMarkers(browser);
                    zoomOutVisibleMarkers(browser);
                }
            }
        });
        
        JPanel toolBar = new JPanel();
        toolBar.add(searchMovie);
        toolBar.add(zoomInButton);
        
        JFrame frame = new JFrame("JxBrowser Google Maps");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static Connection connectDB()
    {
        try 
        {
            String host = "jdbc:postgresql://localhost:5432/BigData";
            String uName = "postgres";
            String uPass= "";
            return DriverManager.getConnection( host, uName, uPass );
        } 
        catch ( SQLException err ) 
        {
            System.out.println( err.getMessage( ) );
            return null;
        }
    }
    
    public static ResultSet getLocationsFromMovie(Connection con, String movie){
        try {
            Statement stmt = con.createStatement( );
            String SQL = "SELECT L.location FROM final.location L INNER JOIN final.movie_location ML ON L.location_id=ML.location_id INNER JOIN final.movie M ON ML.movie_id = M.movie_id WHERE m.title = '" + movie + "';";
            return stmt.executeQuery( SQL);
        } catch (SQLException err){
            System.out.println( err.getMessage( ) );
            return null;
        }
    }
    
    public static void createMarker(Browser browser, String location){
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
            browser.executeJavaScript("google.maps.event.addListener(markers[markers.length - 1] , 'click', function(){ var infowindow = new google.maps.InfoWindow({ content:'" + country + "', position: myLatLng, }); infowindow.open(map, this);});");
            
        }else{
            System.out.println("Geen locaties gevonden");
        }
    }
   
    public static void clearMarkers (Browser browser){
        browser.executeJavaScript("var markers = [];");
        browser.executeJavaScript("setMapOnAll(null);");
    }
    
    public static void setMarkers (Browser browser){
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) { markers[i].setMap(map);}");
    }
    
    public static void zoomOutVisibleMarkers(Browser browser){
        browser.executeJavaScript("var bounds = new google.maps.LatLngBounds();");
        browser.executeJavaScript("for (var i = 0; i < markers.length; i++) {bounds.extend(markers[i].getPosition());}");
        browser.executeJavaScript("map.fitBounds(bounds);");
    }
}
