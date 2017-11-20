package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import daumont.caspar.ensim.snowtam.Model.Ground;
import daumont.caspar.ensim.snowtam.Model.ListGround;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Activity activity;

    private ListGround list_ground;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        activity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("listGround") != null) {
                list_ground = new Gson().fromJson(extras.getString("listGround"), ListGround.class);
            }
        }

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.loading_subtitle));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(false);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng adress = new LatLng(67.2691667,14.3652778);
        for(int cpt=0;cpt<list_ground.getListGround().size();cpt++){

        }
        mMap.addMarker(new MarkerOptions().position(adress).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(adress));
    }

    @Override
    public void onBackPressed() {
        retour();
    }

    public void retour() {
        if (Methods.internet_diponible(activity)) {
            Intent intent = new Intent(activity, ActivityResult.class);
            intent.putExtra("listGround", new Gson().toJson(list_ground));
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_return, R.anim.push_out_return);
            finish();
        }

    }


}
