package daumont.caspar.ensim.snowtam.Model;

/**
 * Created by jojo- on 14/11/2017.
 */

public class Ground {

    private String name;
    private String data_crypt;

    private String snowtam_raw ;

    private String snowtam_decoded ;

    public Ground(String name){
        this.name = name;
    }

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

    public String getData_crypt() {
        return data_crypt;
    }

    public void setData_crypt(String data_crypt) {
        this.data_crypt = data_crypt;

    }
}
