package daumont.caspar.ensim.snowtam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jojo- on 20/11/2017.
 */

public class Notams {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("entity")
    @Expose
    private String entity;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("Qcode")
    @Expose
    private String Qcode;

    @SerializedName("Area")
    @Expose
    private String Area;

    @SerializedName("SubArea")
    @Expose
    private String SubArea;

    @SerializedName("Condition")
    @Expose
    private String Condition;

    @SerializedName("Subject")
    @Expose
    private String Subject;

    @SerializedName("Modifier")
    @Expose
    private String Modifier;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("startdate")
    @Expose
    private String startdate;

    @SerializedName("enddate")
    @Expose
    private String enddate;

    @SerializedName("all")
    @Expose
    private String all;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("isICAO")
    @Expose
    private String isICAO;

    @SerializedName("Created")
    @Expose
    private String Created;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("StateCode")
    @Expose
    private String StateCode;

    @SerializedName("StateName")
    @Expose
    private String StateName;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQcode() {
        return Qcode;
    }

    public void setQcode(String qcode) {
        Qcode = qcode;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getSubArea() {
        return SubArea;
    }

    public void setSubArea(String subArea) {
        SubArea = subArea;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getModifier() {
        return Modifier;
    }

    public void setModifier(String modifier) {
        Modifier = modifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsICAO() {
        return isICAO;
    }

    public void setIsICAO(String isICAO) {
        this.isICAO = isICAO;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }
}
