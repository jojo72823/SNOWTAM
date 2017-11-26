package daumont.caspar.ensim.snowtam.Model;

import java.util.ArrayList;

/**
 * Created by jojo- on 15/11/2017.
 */

public class ListGround {

    private ArrayList<Ground> listGround;

    public ListGround(){
        this.listGround = new ArrayList<>();
    }

    public void addListGround(ArrayList<Ground> listGround){
        this.listGround = listGround;
    }

    public ArrayList<Ground> getListGround(){
        return this.listGround;
    }

}
