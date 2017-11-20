package daumont.caspar.ensim.snowtam.Model;

/**
 * Created by jojo- on 14/11/2017.
 */

public class Ground {

    private String name;
    private String data_crypt;

    public Ground(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData_crypt() {
        return data_crypt;
    }

    public void setData_crypt(String data_crypt) {
        this.data_crypt = data_crypt;
    }
}
