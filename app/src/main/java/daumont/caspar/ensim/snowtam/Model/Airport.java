package daumont.caspar.ensim.snowtam.Model;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jojo- on 14/11/2017.
 */

public class Airport {

    //ATTRIBUTS
    private String name;
    private String snowtam_raw ;

    private String snowtam_decoded ;
    private LatLng latLng;

    //CONSTRUCTOR
    public Airport(String name){
        this.name = name;
    }


    //GET & SET
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnowtam_raw() {
        return snowtam_raw;
    }

    public void setSnowtam_raw(String snowtam_raw) {
        this.snowtam_raw = snowtam_raw;
    }

    public String getSnowtam_decoded() {
        return snowtam_decoded;
    }

    public void setSnowtam_decoded(String snowtam_decoded) {
        this.snowtam_decoded = snowtam_decoded;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
