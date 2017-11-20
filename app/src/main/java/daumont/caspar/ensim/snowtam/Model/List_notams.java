package daumont.caspar.ensim.snowtam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Créé par JONATHAN DAUMONT le 20/11/2017.
 */

public class List_notams {


    @SerializedName("data")
    @Expose
    private List<Notams> data = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Notams> getData() {
        return data;
    }

    public void setData(List<Notams> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


}
