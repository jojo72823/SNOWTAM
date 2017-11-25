package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import daumont.caspar.ensim.snowtam.Model.Ground;
import daumont.caspar.ensim.snowtam.Model.ListGround;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;

public class ActivityResult extends AppCompatActivity {
    public String  [] Snowtam_partc = new String [17];
    public String [] Snowtam_partd = new String [17];
    public String data = "";
    public Boolean find_snowtam = false;
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
    private ProgressDialog mProgressDialog;
    //ARRAYLIST
    private ListGround list_ground;
    private ArrayList<Ground> arrayList_ground;
    //OTHER
    private Activity activity;
    private MyCustomAdapterGround dataMyCustomAdapterGround;

    private int id_position;

    private String resultat_chaine;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (Methods.internet_diponible(activity)) {
                switch (item.getItemId()) {
                    case R.id.navigation_crypt:
                        textView_content.setText(arrayList_ground.get(id_position).getSnowtam_raw());

                        return true;
                    case R.id.navigation_decrypt:
                        textView_content.setText(arrayList_ground.get(id_position).getSnowtam_decoded());
                        return true;

                }
            }

            return false;
        }

    };

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
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.loading_subtitle));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(false);
        id_position = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("listGround") != null) {
                list_ground = new Gson().fromJson(extras.getString("listGround"), ListGround.class);
                arrayList_ground = list_ground.getListGround();
            }
        }
        dataMyCustomAdapterGround = new MyCustomAdapterGround(activity, R.layout.list_layout_ground, arrayList_ground);
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

        new Loading().execute();

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

                    id_position = position;

                    //GET INTERFACE
                    Button button_close = (Button) alertDialogView.findViewById(R.id.button_close);
                    textView_content = (TextView) alertDialogView.findViewById(R.id.textView_content);
                    navigation = (BottomNavigationView) alertDialogView.findViewById(R.id.navigation_produits);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                    //INITIALIZE
                    textView_content.setText(arrayList_ground.get(id_position).getSnowtam_decoded());
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

    private class Loading extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int cpt = 0; cpt < arrayList_ground.size(); cpt++) {
                final int cptfinal = cpt;
                // Initialize a new RequestQueue instance
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                String url = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/states/notams/notams-list?api_key=72b1ee30-cdce-11e7-8f50-f15f214edab3&format=json&type=&Qcode=&locations=" + arrayList_ground.get(cpt).getName() + "&qstring=&states=&ICAOonly=false";
                // Initialize a new JsonArrayRequest instance
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Do something with response
                                //mTextView.setText(response.toString());

                                // Process the JSON
                                try {
                                    // Loop through the array elements
                                    for (int i = 0; i < response.length(); i++) {
                                        // Get current json object
                                        JSONObject detail = response.getJSONObject(i);

                                        if (find_snowtam == false) data = detail.getString("all");
                                        //TRAITEMENT
                                        if (data.indexOf("SNOWTAM ") != -1 && find_snowtam == false) {
                                            //Toast.makeText(activity, "data = " + data, Toast.LENGTH_SHORT).show();
                                            find_snowtam = true;


                                            if (data.indexOf("A) ") != -1) {
                                                String raw[] = data.split("A[)]");
                                                String part_atab[] = raw[1].split("[\n]");
                                                Snowtam_partc[0] = part_atab[0];
                                                //Toast.makeText(activity, "A = " + part_a, Toast.LENGTH_SHORT).show();
                                            }
                                            if (data.indexOf("B) ") != -1) {
                                                String raw[] = data.split("B[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[1] = part_tab2[0];
                                                    //Toast.makeText(activity, "B = " + part_b, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[1] = part_tab[1];
                                                    //Toast.makeText(activity, "B = " + part_b, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("C) ") != -1) {
                                                String raw[] = data.split("C[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[2] = part_tab2[0];
                                                    //Toast.makeText(activity, "C = " + part_b, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[2] = part_tab[1];
                                                    //Toast.makeText(activity, "C = " + part_c, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("D) ") != -1) {
                                                String raw[] = data.split("D[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[3] = part_tab2[0];
                                                    //Toast.makeText(activity, "D = " + part_d, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[3] = part_tab[1];
                                                    //Toast.makeText(activity, "D = " + part_d, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("E) ") != -1) {
                                                String raw[] = data.split("E[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[4] = part_tab2[0];
                                                    //Toast.makeText(activity, "E = " + part_e, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[4] = part_tab[1];
                                                    //Toast.makeText(activity, "E = " + part_e, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("F) ") != -1) {
                                                String raw[] = data.split("F[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[5] = part_tab2[0];
                                                    //Toast.makeText(activity, "F = " + part_f, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[5] = part_tab[1];
                                                    //Toast.makeText(activity, "F = " + part_f, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("G) ") != -1) {
                                                String raw[] = data.split("G[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[6] = part_tab2[0];
                                                    //Toast.makeText(activity, "G = " + part_g, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[6] = part_tab[1];
                                                    //Toast.makeText(activity, "G = " + part_g, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("H) ") != -1) {
                                                String raw[] = data.split("H[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[7] = part_tab2[0];
                                                    //Toast.makeText(activity, "H = " + part_h, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[7] = part_tab[1];
                                                    //Toast.makeText(activity, "H = " + part_h, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("J) ") != -1) {
                                                String raw[] = data.split("J[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[8] = part_tab2[0];
                                                    //Toast.makeText(activity, "J = " + part_j, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[8] = part_tab[1];
                                                    //Toast.makeText(activity, "J = " + part_j, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("K) ") != -1) {
                                                String raw[] = data.split("K[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[9] = part_tab2[0];
                                                    //Toast.makeText(activity, "K = " + part_k, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[9] = part_tab[1];
                                                    //Toast.makeText(activity, "K = " + part_k, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("L) ") != -1) {
                                                String raw[] = data.split("L[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[10] = part_tab2[0];
                                                    //Toast.makeText(activity, "L = " + part_l, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[10] = part_tab[1];
                                                    //Toast.makeText(activity, "L = " + part_l, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("M) ") != -1) {
                                                String raw[] = data.split("M[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[11] = part_tab2[0];
                                                    //Toast.makeText(activity, "M = " + part_m, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[11] = part_tab[1];
                                                    //Toast.makeText(activity, "M = " + part_m, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("N) ") != -1) {
                                                String raw[] = data.split("N[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                Snowtam_partc[12] = part_tab[0];
                                                //Toast.makeText(activity, "N = " + part_n, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("P) ") != -1) {
                                                String raw[] = data.split("P[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                Snowtam_partc[13] = part_tab[0];
                                                //Toast.makeText(activity, "P = " + part_p, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("R) ") != -1) {
                                                String raw[] = data.split("R[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                Snowtam_partc[14] = part_tab[0];
                                                //Toast.makeText(activity, "R = " + part_r, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("S) ") != -1) {
                                                String raw[] = data.split("S[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    Snowtam_partc[15] = part_tab2[0];
                                                    //Toast.makeText(activity, "S = " + part_s, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snowtam_partc[15] = part_tab[1];
                                                    //Toast.makeText(activity, "S = " + part_s, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("T) ") != -1) {
                                                String raw[] = data.split("T[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                Snowtam_partc[16] = part_tab[0];

                                            }

                                        }

                                    }

                                    //TODO
                                    arrayList_ground.get(cptfinal).setSnowtam_raw(data);

                                    find_snowtam = false;

                                    if (Snowtam_partc[1] != null) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, HH:mm ");
                                        Date converterDate = new Date();
                                        try {
                                            converterDate = dateFormat.parse(Snowtam_partc[1]);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Snowtam_partd[1] = "B) " + converterDate.toString();
                                    }
                                    if (Snowtam_partc[2] != null) {
                                        Snowtam_partd[2] = "C) "+getString(R.string.runway) + Snowtam_partc[2];
                                    }
                                    if (Snowtam_partc[3] != null) {
                                        Snowtam_partd[3] = "D) "+getString(R.string.part_ds) + getString(R.string.m);
                                    }
                                    if (Snowtam_partc[4] != null) {
                                        String diretion[] = Snowtam_partc[4].split("\\s+");
                                        if (diretion[1] == "R")
                                            Snowtam_partd[4] = "E) "+getString(R.string.part_es) + diretion[0] + getString(R.string.m)+" "+getString(R.string.right);
                                        if (diretion[1] == "L")
                                            Snowtam_partd[4] = "E) "+getString(R.string.part_es) + diretion[0] + getString(R.string.m)+" "+getString(R.string.left);
                                    }
                                    if (Snowtam_partc[5] != null) {
                                        String instruction[] = Snowtam_partc[5].split("[/]");
                                        Snowtam_partd[5] = "F) ";

                                        for (int i = 0; i < instruction.length; i++) {
                                            if (instruction[i].indexOf("0") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs0);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs0);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs0);
                                                        break;
                                                }


                                            } else if (instruction[i].indexOf("1") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs1);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs1);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs1);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("2") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs2);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs2);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs2);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("3") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs3);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs3);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs3);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("4") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs4);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs4);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs4);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("5") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs5);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs5);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs5);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("6") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs6);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs6);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs6);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("7") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs7);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs7);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs7);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("8") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs8);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs8);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs8);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("9") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.part_fs9);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.part_fs9);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.part_fs9);
                                                        break;
                                                }

                                            }
                                            else
                                            {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[5] += getString(R.string.threshold) + getString(R.string.nil);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[5] += getString(R.string.mid_runway) + getString(R.string.nil);
                                                        break;
                                                    case 2:
                                                        Snowtam_partd[5] += getString(R.string.roll_out) + getString(R.string.nil);
                                                        break;
                                                }

                                            }
                                        }
                                    }
                                    if (Snowtam_partc[6] != null)
                                    {
                                        String instruction[] = Snowtam_partc[6].split("[/]");
                                        Snowtam_partd[6] = "";
                                        Snowtam_partd[6] = "G) "+ getString(R.string.threshold) + instruction[0]+getString(R.string.mm)+getString(R.string.mid_runway)+instruction[1]+getString(R.string.mm)+getString(R.string.roll_out)+instruction[2]+getString(R.string.mm);
                                    }
                                    if (Snowtam_partc[7] != null)
                                    {
                                        String instruction[] = Snowtam_partc[7].split("[/]");
                                        String instruction2[] = instruction[2].split(" ");
                                        Snowtam_partd[7] = "H) ";

                                        for(int i = 0; i < instruction.length;i++)
                                        {
                                            if(i == 2)
                                            {
                                                if (Integer.parseInt(instruction2[0]) >= 40 || Integer.parseInt(instruction2[0]) == 5)
                                                {
                                                    Snowtam_partd[7] += getString(R.string.roll_out) + getString(R.string.good);
                                                }
                                                else if ((Integer.parseInt(instruction2[0]) <= 39  && Integer.parseInt(instruction2[0]) >= 36) || Integer.parseInt(instruction2[0]) == 4)
                                                {
                                                    Snowtam_partd[7] += getString(R.string.roll_out) + getString(R.string.MG);
                                                }
                                                else if ((Integer.parseInt(instruction2[0]) <= 35  && Integer.parseInt(instruction2[0]) >= 30) || Integer.parseInt(instruction2[0]) == 3)
                                                {
                                                    Snowtam_partd[7] += getString(R.string.roll_out) + getString(R.string.medium);
                                                }
                                                else if ((Integer.parseInt(instruction2[0]) <= 29  && Integer.parseInt(instruction2[0]) >= 26) || Integer.parseInt(instruction2[0]) == 2)
                                                {
                                                    Snowtam_partd[7] += getString(R.string.roll_out) + getString(R.string.MP);
                                                }
                                                else if (Integer.parseInt(instruction2[0]) <= 25   || Integer.parseInt(instruction2[0]) == 1)
                                                {
                                                    Snowtam_partd[7] += getString(R.string.roll_out) + getString(R.string.poor);
                                                }

                                            }
                                            else if (Integer.parseInt(instruction[i]) >= 40 || Integer.parseInt(instruction[i]) == 5) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[7] += getString(R.string.threshold) + getString(R.string.good);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[7] += getString(R.string.mid_runway) + getString(R.string.good);
                                                        break;
                                                }


                                            }
                                            else if ((Integer.parseInt(instruction[i]) <= 39  && Integer.parseInt(instruction[i]) >= 36) || Integer.parseInt(instruction[i]) == 4) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[7] += getString(R.string.threshold) + getString(R.string.MG);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[7] += getString(R.string.mid_runway) + getString(R.string.MG);
                                                        break;
                                                }


                                            }
                                            else if ((Integer.parseInt(instruction[i]) <= 35  && Integer.parseInt(instruction[i]) >= 30) || Integer.parseInt(instruction[i]) == 3) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[7] += getString(R.string.threshold) + getString(R.string.medium);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[7] += getString(R.string.mid_runway) + getString(R.string.medium);
                                                        break;
                                                }


                                            }
                                            else if ((Integer.parseInt(instruction[i]) <= 29  && Integer.parseInt(instruction[i]) >= 26) || Integer.parseInt(instruction[i]) == 2) {
                                            switch (i) {
                                                case 0:
                                                    Snowtam_partd[7] += getString(R.string.threshold) + getString(R.string.MP);
                                                    break;
                                                case 1:
                                                    Snowtam_partd[7] += getString(R.string.mid_runway) + getString(R.string.MP);
                                                    break;
                                                 }


                                            }
                                            else if (Integer.parseInt(instruction[i]) <= 25   || Integer.parseInt(instruction[i]) == 1) {
                                                switch (i) {
                                                    case 0:
                                                        Snowtam_partd[7] += getString(R.string.threshold) + getString(R.string.poor);
                                                        break;
                                                    case 1:
                                                        Snowtam_partd[7] += getString(R.string.mid_runway) + getString(R.string.poor);
                                                        break;
                                                }


                                            }

                                        }

                                        if(instruction2.length > 1)
                                        {
                                            if(instruction2[1].indexOf("BRD") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.brd);
                                            else if(instruction2[1].indexOf("GRT") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.grt);
                                            else if(instruction2[1].indexOf("MUM") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.mum);
                                            else if(instruction2[1].indexOf("RFT") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.rft);
                                            else if(instruction2[1].indexOf("SFH") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.sfh);
                                            else if(instruction2[1].indexOf("SFL") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.sfl);
                                            else if(instruction2[1].indexOf("SKH") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.skh);
                                            else if(instruction2[1].indexOf("SKL") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.skl);
                                            else if(instruction2[1].indexOf("TAP") != -1)Snowtam_partd[7] += getString(R.string.instrument)+getString(R.string.tap);
                                        }

                                    }
                                    if (Snowtam_partc[8] != null)
                                    {
                                        String instruction[] = Snowtam_partc[8].split("[/]");
                                        if(instruction[1].indexOf("L") != -1)
                                        {
                                            String instruction2 [] = instruction[1].split("L");
                                            Snowtam_partd[8] = "J) "+getString(R.string.part_js) + instruction[0]+getString(R.string.cm) + "/ "+instruction2[0]+ getString(R.string.m)+  getString(R.string.left)+  getString(R.string.of)+ getString(R.string.runway);
                                        }
                                        else if(instruction[1].indexOf("R") != -1)
                                        {
                                            String instruction2 [] = instruction[1].split("R");
                                            Snowtam_partd[8] = "J) "+getString(R.string.part_js) + instruction[0]+getString(R.string.cm) + "/ "+instruction2[0]+ getString(R.string.m)+  getString(R.string.right)+  getString(R.string.of)+ getString(R.string.runway);
                                        }

                                    }

                                    if (Snowtam_partc[9] != null)
                                    {
                                        String instruction[] = Snowtam_partc[9].split(" ");

                                        if(instruction[2].indexOf("L") != -1)
                                        {
                                            if(instruction[2].indexOf("LR") == -1)
                                            {
                                                Snowtam_partd[9] = "K) "+ getString(R.string.part_ks)+ instruction[1] + getString(R.string.left)+ getString(R.string.of)+getString(R.string.runway);
                                            }

                                        }
                                        if(instruction[2].indexOf("L") != -1)
                                        {
                                            if(instruction[2].indexOf("LR") == -1)
                                            {
                                                Snowtam_partd[9] = "K) "+ getString(R.string.part_ks)+ instruction[1] +getString(R.string.right)+ getString(R.string.of)+getString(R.string.runway);

                                            }

                                        }
                                        if(instruction[2].indexOf("LR") != -1)
                                        {
                                            Snowtam_partd[9] = "K) " + getString(R.string.part_ks)+ instruction[1] +getString(R.string.left)+getString(R.string.right)+ getString(R.string.of)+getString(R.string.runway);
                                        }


                                    }

                                    if (Snowtam_partc[10] != null) {
                                        String instruction[] = Snowtam_partc[10].split("/");
                                        Snowtam_partd[10] = "L) " + getString(R.string.part_ls) + instruction[0] + getString(R.string.m) + "/ " + instruction[1] + getString(R.string.m);
                                    }
                                    if (Snowtam_partc[11] != null) {
                                        Snowtam_partd[11] = "M) " + getString(R.string.part_ms) + Snowtam_partc[11] + getString(R.string.utc);
                                    }
                                    if (Snowtam_partc[12] != null) {
                                        Snowtam_partd[12] = "N) " + Snowtam_partc[12];
                                    }
                                    if (Snowtam_partc[13] != null) {
                                        String instruction [] = Snowtam_partc[13].split("YES");
                                        Snowtam_partd[13] = "P) " + getString(R.string.yes) + getString(R.string.space) + instruction[1]+getString(R.string.m);
                                    }
                                    if (Snowtam_partc[14] != null) {
                                        Snowtam_partd[14] = "R) " + Snowtam_partc[14];
                                    }
                                    if (Snowtam_partc[15] != null) {
                                        Snowtam_partd[15] = "S) " + Snowtam_partc[15];
                                    }
                                    if (Snowtam_partc[16] != null) {
                                        Snowtam_partd[16] = "T) " + Snowtam_partc[16];
                                    }




                                    resultat_chaine = "";

                                    for(int i = 0; i < Snowtam_partd.length;i++ )
                                    {
                                        if(Snowtam_partd[i] != null)
                                        {
                                            resultat_chaine += Snowtam_partd[i] + "\n\n";
                                        }

                                    }





                                    arrayList_ground.get(cptfinal).setSnowtam_decoded(resultat_chaine);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something when error occurred

                            }
                        }
                );
                // Add JsonArrayRequest to the RequestQueue
                requestQueue.add(jsonArrayRequest);

                RequestQueue requestQueue2 = Volley.newRequestQueue(activity);
                String url2 = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/airports/locations/doc7910?api_key=72b1ee30-cdce-11e7-8f50-f15f214edab3&airports=" + arrayList_ground.get(cpt).getName() + "&format=json";
                // Initialize a new JsonArrayRequest instance
                JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(
                        Request.Method.GET,
                        url2,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Do something with response
                                //mTextView.setText(response.toString());

                                // Process the JSON
                                try {
                                    // Loop through the array elements
                                    for (int i = 0; i < response.length(); i++) {
                                        // Get current json object
                                        JSONObject detail = response.getJSONObject(i);

                                        String data = detail.getString("Location_Name");
                                        String longitude = detail.getString("Longitude");
                                        String latitude = detail.getString("Latitude");

                                        arrayList_ground.get(cptfinal).setLatLng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                                        String resultbegin =  arrayList_ground.get(cptfinal).getSnowtam_decoded();
                                        Snowtam_partd[0] = "A) " +data;
                                        String deco = Snowtam_partd[0]+"\n"+resultbegin;
                                        arrayList_ground.get(cptfinal).setSnowtam_decoded(deco);
                                        //Toast.makeText(activity, "A = " + part_ad, Toast.LENGTH_SHORT).show();


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something when error occurred

                            }
                        }
                );

                // Add JsonArrayRequest to the RequestQueue
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestQueue2.add(jsonArrayRequest2);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.hide();


        }

    }


}
