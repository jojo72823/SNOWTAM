package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import daumont.caspar.ensim.snowtam.Model.Ground;
import daumont.caspar.ensim.snowtam.Model.ListGround;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;

public class ActivityResult extends AppCompatActivity {

    /**
     * ATTRIBUTES
     */
    //INTERFACE
    private FloatingActionButton fab_maps;
    private ListView listView_ground;
    private TextView textView_content;
    private BottomNavigationView navigation;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbar_layout;
    //ARRAYLIST
    private ListGround list_ground;
    private ArrayList<Ground> arrayList_ground;
    //OTHER
    private Activity activity;
    private MyCustomAdapterGround dataMyCustomAdapterGround;

    private String crypt = "SWEN0492 ENBO 11151041 \n" +
            "(SNOWTAM 0492\n" +
            "A) ENBO\n" +
            "B) 11151041 C) 07\n" +
            "F) NIL/NIL/NIL H) 5/5/5\n" +
            "N) ALPHA BRAVO CHARLIE DELTA/1  ECHO WHISKEY/NIL\n" +
            "R) APRON EAST APRON WEST/2  RWY EXTENSION WEST/NIL)";
    private String decrypt = "A) KJFK – NEW YORK KENNEDY\n" +
            "B) 22 DECEMBER AT 09h45UTC\n" +
            "C) RUNWAY 08L\n" +
            "F) WET SNOW / WET SNOW / WET SNOW\n" +
            "G) MEAN DEPTH 2mm / 2mm / 3mm\n" +
            "H) BRAKING ACTION : MEDIUM TO POOR / POOR / POOR\n" +
            "N) TAXIWAYS: WET SNOW";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GET INTERFACE
        setContentView(R.layout.activity_result);
        listView_ground = (ListView) findViewById(R.id.listView_ground);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //INITIALIZE
        activity = this;
        setSupportActionBar(toolbar);
        toolbar_layout.setTitle(getString(R.string.activityResult));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("listGround") != null) {
                list_ground = new Gson().fromJson(extras.getString("listGround"), ListGround.class);
                arrayList_ground = list_ground.getListGround();
            }
        }
        dataMyCustomAdapterGround = new MyCustomAdapterGround(activity, R.layout.list_layout_ground,arrayList_ground);
        listView_ground.setAdapter(dataMyCustomAdapterGround);


        //LISTENERS
        fab_maps = (FloatingActionButton) findViewById(R.id.fab_maps);

        fab_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityMaps.class);
                intent.putExtra("listGround", new Gson().toJson(list_ground));
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        retour();
    }

    public void retour() {
        if (Methods.internet_diponible(activity)) {
            Intent intent = new Intent(activity, ActivityAddGround.class);
            intent.putExtra("listGround", new Gson().toJson(list_ground));
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_return, R.anim.push_out_return);
            finish();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (Methods.internet_diponible(activity)) {
                switch (item.getItemId()) {
                    case R.id.navigation_crypt:
                        textView_content.setText(crypt);

                        return true;
                    case R.id.navigation_decrypt:
                        textView_content.setText(decrypt);
                        return true;

                }
            }

            return false;
        }

    };

    private class MyCustomAdapterGround extends ArrayAdapter<Ground> {

        private ArrayList<Ground> groupeList;


        public MyCustomAdapterGround(Context context, int textViewResourceId,
                                     ArrayList<Ground> groupeList) {
            super(context, textViewResourceId, groupeList);
            this.groupeList = new ArrayList<>();
            this.groupeList.addAll(groupeList);


        }

        @Override
        public View getView(final int position, View convertViewProduit, ViewGroup parent) {

            MyCustomAdapterGround.ViewHolder holder;

            if (convertViewProduit == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertViewProduit = vi.inflate(R.layout.list_layout_ground, null);

                //GET INTERFACE
                holder = new MyCustomAdapterGround.ViewHolder();
                holder.textView_name_ground = (TextView) convertViewProduit.findViewById(R.id.textView_name_ground);
                holder.imageView = (ImageView) convertViewProduit.findViewById(R.id.imageView);
                //INITIALIZE
                convertViewProduit.setTag(holder);


                holder.imageView.setImageResource(R.drawable.icon_plane);


                holder.textView_name_ground.setText(groupeList.get(position).getName());


            } else {
                holder = (MyCustomAdapterGround.ViewHolder) convertViewProduit.getTag();
            }

            //INITIALIZE
            holder.textView_name_ground.setText(groupeList.get(position).getName());

            holder.imageView.setImageResource(R.drawable.icon_plane);


            //LISTENER
            convertViewProduit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(activity);

                    final View alertDialogView = factory.inflate(R.layout.dialog_view_ground, null);
                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);

                    //GET INTERFACE
                    Button button_close = (Button) alertDialogView.findViewById(R.id.button_close);
                    textView_content = (TextView)alertDialogView.findViewById(R.id.textView_content);
                    navigation = (BottomNavigationView) alertDialogView.findViewById(R.id.navigation_produits);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                    //INITIALIZE
                    textView_content.setText(crypt);
                    adb.setView(alertDialogView);
                    final AlertDialog alertDialog = adb.show();


                    //LISTENER
                    button_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                }
            });


            return convertViewProduit;
        }

        private class ViewHolder {
            TextView textView_name_ground;
            ImageView imageView;

        }


    }
}
