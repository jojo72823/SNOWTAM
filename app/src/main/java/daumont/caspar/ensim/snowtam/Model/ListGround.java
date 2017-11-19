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

    public void add_ground(Ground ground){
        this.listGround.add(ground);
    }

    public void delete_ground(int position){
        this.listGround.remove(position);
    }

    public void addListGround(ArrayList<Ground> listGround){
        this.listGround = listGround;
    }
    public ArrayList<Ground> getListGround(){
        return this.listGround;
    }

}
