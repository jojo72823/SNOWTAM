package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import daumont.caspar.ensim.snowtam.Model.Airport;
import daumont.caspar.ensim.snowtam.Model.ListAirport;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;


public class ActivityAddAirport extends AppCompatActivity {


    /**
     * ATTRIBUTES
     */
    //INTERFACE
    private FloatingActionButton fab_result, fab_add;
    private MyCustomAdapterAirport dataMyCustomAdapterAirport;
    private ListView listView_airport;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbar_layout;
    //ARRAYLIST
    private ArrayList<Airport> arraylist_list_airport;

    //OTHER
    private Activity activity;
    private Boolean list_empty = false;
    private ListAirport listAirport;



    /**
     * Create activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GET INTERFACE
        setContentView(R.layout.activity_add_airport);
        fab_result = (FloatingActionButton) findViewById(R.id.fab_result);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        listView_airport = (ListView) findViewById(R.id.listView_ground);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //INITIALIZE
        activity = this;
        arraylist_list_airport = new ArrayList<>();
        listAirport = new ListAirport();
        setSupportActionBar(toolbar);
        toolbar_layout.setTitle(getString(R.string.activityAddGround));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("listAirport") != null) {
                listAirport = new Gson().fromJson(extras.getString("listAirport"), ListAirport.class);
                arraylist_list_airport = listAirport.getListAirport();
                dataMyCustomAdapterAirport = new MyCustomAdapterAirport(activity, R.layout.list_layout_airport, arraylist_list_airport);
            }
        }else{
            initialize_listView_empty();
        }
        listView_airport.setAdapter(dataMyCustomAdapterAirport);


        //LISTENERS
        fab_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arraylist_list_airport.size()==0){
                    Methods.info_dialog(getString(R.string.list_empty),activity);
                }else{
                    listAirport.addListGround(arraylist_list_airport);
                    Intent intent = new Intent(activity, ActivityResult.class);
                    intent.putExtra("listAirport", new Gson().toJson(listAirport));
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                    finish();
                }

            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_ground();
            }
        });

    }
    public void initialize_listView_empty(){
        ArrayList<Airport> list_tmp = new ArrayList<>();
        list_tmp.add(new Airport(getString(R.string.add_ground)));
        list_empty = true;
        dataMyCustomAdapterAirport = new MyCustomAdapterAirport(activity, R.layout.list_layout_airport, list_tmp);
        listView_airport.setAdapter(dataMyCustomAdapterAirport);
    }

    private void udpate_listView() {
        if (dataMyCustomAdapterAirport != null) {
            listView_airport.setAdapter(null);
        }

        dataMyCustomAdapterAirport = new MyCustomAdapterAirport(activity, R.layout.list_layout_airport, arraylist_list_airport);
        listView_airport.setAdapter(dataMyCustomAdapterAirport);
    }

    public void add_ground() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_airport, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setCancelable(false);
        adb.setView(alertDialogView);
        final AlertDialog alertDialog = adb.show();

        Button button_close = (Button) alertDialogView.findViewById(R.id.button_close);
        Button button_add = (Button) alertDialogView.findViewById(R.id.button_add);
        final EditText editText_ground_name = (EditText) alertDialogView.findViewById(R.id.editText_ground_name);

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                arraylist_list_airport.add(new Airport(editText_ground_name.getText().toString()));

                udpate_listView();
                list_empty = false;

                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        retour();
    }

    public void retour() {
        if (Methods.internet_diponible(activity)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.exit_app))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }

    }


    private class MyCustomAdapterAirport extends ArrayAdapter<Airport> {

        private ArrayList<Airport> groupeList;


        public MyCustomAdapterAirport(Context context, int textViewResourceId,
                                      ArrayList<Airport> groupeList) {
            super(context, textViewResourceId, groupeList);
            this.groupeList = new ArrayList<>();
            this.groupeList.addAll(groupeList);


        }

        @Override
        public View getView(final int position, View convertViewProduit, ViewGroup parent) {

            ViewHolder holder;

            if (convertViewProduit == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertViewProduit = vi.inflate(R.layout.list_layout_airport, null);

                //GET INTERFACE
                holder = new ViewHolder();
                holder.textView_name_ground = (TextView) convertViewProduit.findViewById(R.id.textView_name_ground);
                holder.imageView = (ImageView) convertViewProduit.findViewById(R.id.imageView);
                //INITIALIZE
                convertViewProduit.setTag(holder);

                if (list_empty) {
                    holder.imageView.setImageResource(R.drawable.icon_add);
                } else {
                    holder.imageView.setImageResource(R.drawable.icon_plane);
                }

                holder.textView_name_ground.setText(groupeList.get(position).getName());


            } else {
                holder = (ViewHolder) convertViewProduit.getTag();
            }

            //INITIALIZE
            holder.textView_name_ground.setText(groupeList.get(position).getName());
            if (list_empty) {
                holder.imageView.setImageResource(R.drawable.icon_add);
            } else {
                holder.imageView.setImageResource(R.drawable.icon_plane);
            }

            //LISTENER
            convertViewProduit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (list_empty) {
                        add_ground();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setCancelable(false);
                        builder.setMessage("Voulez-vous suppprimer " + groupeList.get(position).getName())
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        arraylist_list_airport.remove(position);
                                        udpate_listView();
                                        dialog.cancel();
                                        if(arraylist_list_airport.size() == 0){
                                            initialize_listView_empty();
                                        }
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        builder.create();
                        builder.show();
                    }
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


