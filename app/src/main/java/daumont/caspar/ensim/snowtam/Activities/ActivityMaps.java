package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import daumont.caspar.ensim.snowtam.Model.Airport;
import daumont.caspar.ensim.snowtam.Model.ListAirport;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;

public class ActivityMaps extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Activity activity;
    private Button button_next, button_back;
    private ListAirport list_airport;
    private ProgressDialog mProgressDialog;
    private int id_marker;
    private boolean view_single_marker= false;
    private Airport airport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        activity = this;
        id_marker = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("airport") != null) {
                view_single_marker = true;
                airport = new Gson().fromJson(extras.getString("airport"), Airport.class);
            }
            if (extras.getString("listAirport") != null) {
                list_airport = new Gson().fromJson(extras.getString("listAirport"), ListAirport.class);
            }

        }

        button_back = (Button) findViewById(R.id.button_back);
        button_next = (Button) findViewById(R.id.button_next);

        if (view_single_marker) {
            button_back.setVisibility(View.GONE);
            button_next.setVisibility(View.GONE);
        }else{
            if(list_airport.getListAirport().size() <= 1){
                button_back.setVisibility(View.GONE);
                button_next.setVisibility(View.GONE);
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

        if (view_single_marker) {
            LatLng adress = airport.getLatLng();
            mMap.addMarker(new MarkerOptions().position(adress).title(airport.getName()));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(airport.getLatLng()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(airport.getLatLng(), 12.0f));
            mMap.setOnInfoWindowClickListener(this);
        } else {
            for (int cpt = 0; cpt < list_airport.getListAirport().size(); cpt++) {
                LatLng adress = list_airport.getListAirport().get(cpt).getLatLng();
                mMap.addMarker(new MarkerOptions().position(adress).title(list_airport.getListAirport().get(cpt).getName()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(list_airport.getListAirport().get(id_marker).getLatLng()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list_airport.getListAirport().get(id_marker).getLatLng(), 12.0f));
            mMap.setOnInfoWindowClickListener(this);
        }


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_marker == 0) {
                    id_marker = list_airport.getListAirport().size() - 1;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list_airport.getListAirport().get(id_marker).getLatLng(), 12.0f));
                } else {
                    id_marker--;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list_airport.getListAirport().get(id_marker).getLatLng(), 12.0f));
                }

            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_marker == list_airport.getListAirport().size() - 1) {
                    id_marker = 0;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list_airport.getListAirport().get(id_marker).getLatLng(), 12.0f));
                } else {
                    id_marker++;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list_airport.getListAirport().get(id_marker).getLatLng(), 12.0f));
                }

            }
        });

        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow

            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.dialog_marker, null);

                TextView textView_content = (TextView) v.findViewById(R.id.textView_content);
                TextView textView_name = (TextView) v.findViewById(R.id.textView_name);

                //INITIALIZE
                if (view_single_marker) {
                    textView_name.setText(airport.getName());
                    textView_content.setText(airport.getSnowtam_decoded());
                }else{
                    textView_name.setText(list_airport.getListAirport().get(id_marker).getName());
                    textView_content.setText(list_airport.getListAirport().get(id_marker).getSnowtam_decoded());
                }


                return v;

            }
        });
    }

    @Override
    public void onBackPressed() {
        retour();
    }

    public void retour() {
        if (Methods.internet_diponible(activity)) {
            Intent intent = new Intent(activity, ActivityResult.class);
            intent.putExtra("listAirport", new Gson().toJson(list_airport));
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_return, R.anim.push_out_return);
            finish();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }


}
