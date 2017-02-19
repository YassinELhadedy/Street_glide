package kk.ljq.hhh.loca;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elhadedy on 2/1/2017.
 */

public class DriverInfo {
    private double d_lat;
    private double d_lon;
    private String d_name;
    private String d_loc_name;
    private String d_loc_wait;
    private String d_number;
    private String d_Email;
    private String d_photo;
    private long d_time;
    private float d_spead;
    private String d_provider_name;
    private String d_loc_url;
    private boolean d_state;
    private double last_seen;
    private String time_go;
    private String date_go;
    private String add_location_order;
    private String add_time_order;
    private String add_rating_order;




    public DriverInfo() {
    }

    public DriverInfo(String add_location_order, String add_rating_order, String add_time_order, String d_Email) {
        this.add_location_order = add_location_order;
        this.add_rating_order = add_rating_order;
        this.add_time_order = add_time_order;
        this.d_Email = d_Email;
    }

    public DriverInfo(String d_Email, String d_name, String d_loc_name, String d_loc_wait, String time_go, String date_go) {
        this.d_Email = d_Email;
        this.d_name = d_name;
        this.d_loc_name = d_loc_name;
        this.d_loc_wait = d_loc_wait;
        this.time_go=time_go;
        this.date_go=date_go;
    }

    public DriverInfo(DriverInfo driverInfo){
        this.d_lat = driverInfo.d_lat;
        this.d_lon = driverInfo.d_lon;
        this.d_name = driverInfo.d_name;
        this.d_loc_wait = driverInfo.d_loc_wait;
        this.d_loc_name = driverInfo.d_loc_name;
        this.d_Email = driverInfo.d_Email;
        this.d_number = driverInfo.d_number;
        this.d_photo=driverInfo.d_photo;
        this.d_time=driverInfo.d_time;
        this.d_spead=driverInfo.d_spead;
        this.d_provider_name=driverInfo.d_provider_name;
        this.d_loc_url=driverInfo.d_loc_url;



    }

    public DriverInfo(double d_lat, double d_lon, String d_name, String d_number, String d_Email, String d_loc_wait, String d_loc_name,String d_photo,long d_time,float d_spead,String d_provider_name,String d_loc_url) {
        this.d_lat = d_lat;
        this.d_lon = d_lon;
        this.d_name = d_name;
        this.d_number = d_number;
        this.d_Email = d_Email;
        this.d_loc_wait = d_loc_wait;
        this.d_loc_name = d_loc_name;
        this.d_photo=d_photo;
        this.d_time=d_time;
        this.d_spead=d_spead;
        this.d_provider_name=d_provider_name;
        this.d_loc_url=d_loc_url;

    }

    public double getD_lat() {
        return d_lat;
    }

    public String getDate_go() {
        return date_go;
    }

    public String getTime_go() {
        return time_go;
    }

    public double getLast_seen() {
        return last_seen;
    }

    public boolean isD_state() {
        return d_state;
    }

    public String getD_loc_name() {
        return d_loc_name;
    }

    public String getD_loc_wait() {
        return d_loc_wait;
    }

    public double getD_lon() {
        return d_lon;
    }

    public String getD_name() {
        return d_name;
    }

    public String getD_Email() {
        return d_Email;
    }

    public String getD_number() {
        return d_number;
    }

    public String getD_photo() {
        return d_photo;
    }

    public String getD_provider_name() {
        return d_provider_name;
    }

    public float getD_spead() {
        return d_spead;
    }

    public long getD_time() {
        return d_time;
    }

    public String getD_loc_url() {
        return d_loc_url;
    }

    public String getAdd_location_order() {
        return add_location_order;
    }

    public String getAdd_rating_order() {
        return add_rating_order;
    }

    public String getAdd_time_order() {
        return add_time_order;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("d_lat", d_lat);
        result.put("d_lon", d_lon);
        result.put("d_name", d_name);
        result.put("d_loc_name", d_loc_name);
        result.put("d_loc_wait", d_loc_wait);
        result.put("d_number", d_number);
        result.put("d_Email",d_Email);
        result.put("d_photo",d_photo);
        result.put("d_time",d_time);
        result.put("d_spead",d_spead);
        result.put("d_provider_name",d_provider_name);
        result.put("d_loc_url",d_loc_url);

        return result;
    }

}
