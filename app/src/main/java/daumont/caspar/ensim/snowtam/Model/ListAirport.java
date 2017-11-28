package daumont.caspar.ensim.snowtam.Model;

import java.util.ArrayList;

/**
 * Created by jojo- on 15/11/2017.
 */

public class ListAirport {

    private ArrayList<Airport> listAirport;

    public ListAirport(){
        this.listAirport = new ArrayList<>();
    }

    public void addListGround(ArrayList<Airport> listAirport){
        this.listAirport = listAirport;
    }

    public ArrayList<Airport> getListAirport(){
        return this.listAirport;
    }

}
