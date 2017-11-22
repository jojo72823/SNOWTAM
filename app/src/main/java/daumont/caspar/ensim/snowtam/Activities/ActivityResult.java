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

    public String part_a = "";
    public String part_b = "";
    public String part_c = "";
    public String part_d = "";
    public String part_e = "";
    public String part_f = "";
    public String part_g = "";
    public String part_h = "";
    public String part_j = "";
    public String part_k = "";
    public String part_l = "";
    public String part_m = "";
    public String part_n = "";
    public String part_p = "";
    public String part_r = "";
    public String part_s = "";
    public String part_t = "";
    public String part_ad = "";
    public String part_bd = "";
    public String part_cd = "";
    public String part_dd = "";
    public String part_ed = "";
    public String part_fd = "";
    public String part_gd = "";
    public String part_hd = "";
    public String part_jd = "";
    public String part_kd = "";
    public String part_ld = "";
    public String part_md = "";
    public String part_nd = "";
    public String part_pd = "";
    public String part_rd = "";
    public String part_sd = "";
    public String part_td = "";
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
                                                part_a = part_atab[0];
                                                //Toast.makeText(activity, "A = " + part_a, Toast.LENGTH_SHORT).show();
                                            }
                                            if (data.indexOf("B) ") != -1) {
                                                String raw[] = data.split("B[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_b = part_tab2[0];
                                                    //Toast.makeText(activity, "B = " + part_b, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_b = part_tab[1];
                                                    //Toast.makeText(activity, "B = " + part_b, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("C) ") != -1) {
                                                String raw[] = data.split("C[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_c = part_tab2[0];
                                                    //Toast.makeText(activity, "C = " + part_b, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_c = part_tab[1];
                                                    //Toast.makeText(activity, "C = " + part_c, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("D) ") != -1) {
                                                String raw[] = data.split("D[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_d = part_tab2[0];
                                                    //Toast.makeText(activity, "D = " + part_d, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_d = part_tab[1];
                                                    //Toast.makeText(activity, "D = " + part_d, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("E) ") != -1) {
                                                String raw[] = data.split("E[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_e = part_tab2[0];
                                                    //Toast.makeText(activity, "E = " + part_e, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_e = part_tab[1];
                                                    //Toast.makeText(activity, "E = " + part_e, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("F) ") != -1) {
                                                String raw[] = data.split("F[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_f = part_tab2[0];
                                                    //Toast.makeText(activity, "F = " + part_f, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_f = part_tab[1];
                                                    //Toast.makeText(activity, "F = " + part_f, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("G) ") != -1) {
                                                String raw[] = data.split("G[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_g = part_tab2[0];
                                                    //Toast.makeText(activity, "G = " + part_g, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_g = part_tab[1];
                                                    //Toast.makeText(activity, "G = " + part_g, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("H) ") != -1) {
                                                String raw[] = data.split("H[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_h = part_tab2[0];
                                                    //Toast.makeText(activity, "H = " + part_h, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_h = part_tab[1];
                                                    //Toast.makeText(activity, "H = " + part_h, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("J) ") != -1) {
                                                String raw[] = data.split("J[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_j = part_tab2[0];
                                                    //Toast.makeText(activity, "J = " + part_j, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_j = part_tab[1];
                                                    //Toast.makeText(activity, "J = " + part_j, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("K) ") != -1) {
                                                String raw[] = data.split("K[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_k = part_tab2[0];
                                                    //Toast.makeText(activity, "K = " + part_k, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_k = part_tab[1];
                                                    //Toast.makeText(activity, "K = " + part_k, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("L) ") != -1) {
                                                String raw[] = data.split("L[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_l = part_tab2[0];
                                                    //Toast.makeText(activity, "L = " + part_l, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_l = part_tab[1];
                                                    //Toast.makeText(activity, "L = " + part_l, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("M) ") != -1) {
                                                String raw[] = data.split("M[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_m = part_tab2[0];
                                                    //Toast.makeText(activity, "M = " + part_m, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_m = part_tab[1];
                                                    //Toast.makeText(activity, "M = " + part_m, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("N) ") != -1) {
                                                String raw[] = data.split("N[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                part_n = part_tab[0];
                                                //Toast.makeText(activity, "N = " + part_n, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("P) ") != -1) {
                                                String raw[] = data.split("P[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                part_p = part_tab[0];
                                                //Toast.makeText(activity, "P = " + part_p, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("R) ") != -1) {
                                                String raw[] = data.split("R[)]");
                                                String part_tab[] = raw[1].split("[\n]");

                                                part_r = part_tab[0];
                                                //Toast.makeText(activity, "R = " + part_r, Toast.LENGTH_SHORT).show();

                                            }
                                            if (data.indexOf("S) ") != -1) {
                                                String raw[] = data.split("S[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_s = part_tab2[0];
                                                    //Toast.makeText(activity, "S = " + part_s, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_s = part_tab[1];
                                                    //Toast.makeText(activity, "S = " + part_s, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            if (data.indexOf("T) ") != -1) {
                                                String raw[] = data.split("T[)]");
                                                String part_tab[] = raw[1].split("[ ]");
                                                if (part_tab[1].indexOf("\n") != -1) {
                                                    String part_tab2[] = part_tab[1].split("[\n]");
                                                    part_t = part_tab2[0];
                                                    //Toast.makeText(activity, "T = " + part_t, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    part_t = part_tab[1];
                                                    //Toast.makeText(activity, "T = " + part_t, Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                        }

                                    }

                                    //TODO
                                    arrayList_ground.get(cptfinal).setSnowtam_raw(data);

                                    find_snowtam = false;

                                    if (part_b != "") {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, HH:mm ");
                                        Date converterDate = new Date();
                                        try {
                                            converterDate = dateFormat.parse(part_b);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        part_bd = converterDate.toString();
                                    }
                                    if (part_c != "") {
                                        part_cd = getString(R.string.part_cs) + part_c;
                                    }
                                    if (part_d != "") {
                                        part_dd = getString(R.string.part_ds) + getString(R.string.m);
                                    }
                                    if (part_e != "") {
                                        String diretion[] = part_e.split("\\s+");
                                        if (diretion[1] == "R")
                                            part_ed = getString(R.string.part_es) + diretion[0] + getString(R.string.part_esr);
                                        if (diretion[1] == "L")
                                            part_ed = getString(R.string.part_es) + diretion[0] + getString(R.string.part_esr);
                                    }
                                    if (part_f != "") {
                                        String instruction[] = part_f.split("[/]");

                                        for (int i = 0; i < instruction.length; i++) {
                                            if (instruction[i].indexOf("0") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs0);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs0);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs0);
                                                        break;
                                                }


                                            } else if (instruction[i].indexOf("1") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs1);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs1);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs1);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("2") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs2);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs2);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs2);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("3") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs3);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs3);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs3);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("4") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs4);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs4);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs4);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("5") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs5);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs5);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs5);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("6") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs6);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs6);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs6);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("7") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs7);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs7);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs7);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("8") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs8);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs8);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs8);
                                                        break;
                                                }

                                            } else if (instruction[i].indexOf("9") != -1) {
                                                switch (i) {
                                                    case 0:
                                                        part_fd += getString(R.string.threshold) + getString(R.string.part_fs9);
                                                        break;
                                                    case 1:
                                                        part_fd += getString(R.string.mid_runway) + getString(R.string.part_fs9);
                                                        break;
                                                    case 2:
                                                        part_fd += getString(R.string.roll_out) + getString(R.string.part_fs9);
                                                        break;
                                                }

                                            }
                                        }
                                    }

                                    resultat_chaine ="B) "+part_bd + "\n" +"C) "+ part_cd + "\n" +"D) "+ part_dd + "\n" +"E) "+ part_ed + "\n" +"F) "+ part_fd + "\n";
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
                                        part_ad = "A) " +data;
                                        String deco = part_ad+"\n"+resultbegin;
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
