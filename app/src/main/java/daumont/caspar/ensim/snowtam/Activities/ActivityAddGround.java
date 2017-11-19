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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import daumont.caspar.ensim.snowtam.Model.Ground;
import daumont.caspar.ensim.snowtam.Model.ListGround;
import daumont.caspar.ensim.snowtam.R;
import daumont.caspar.ensim.snowtam.utils.Methods;


public class ActivityAddGround extends AppCompatActivity {


    /**
     * ATTRIBUTES
     */
    //INTERFACE
    private FloatingActionButton fab_result, fab_add;
    private EditText editText_ground_name;
    private MyCustomAdapterGround dataMyCustomAdapterGround;
    private ListView listView_ground;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbar_layout;
    //ARRAYLIST
    private ArrayList<Ground> arraylist_list_ground;

    //OTHER
    private Activity activity;
    private Boolean list_empty = false;
    private ListGround listGround;


    /**
     * Create activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GET INTERFACE
        setContentView(R.layout.activity_add_ground);
        fab_result = (FloatingActionButton) findViewById(R.id.fab_result);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        listView_ground = (ListView) findViewById(R.id.listView_ground);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //INITIALIZE
        activity = this;
        arraylist_list_ground = new ArrayList<>();
        listGround = new ListGround();
        setSupportActionBar(toolbar);
        toolbar_layout.setTitle(getString(R.string.activityAddGround));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("listGround") != null) {
                listGround = new Gson().fromJson(extras.getString("listGround"), ListGround.class);
                arraylist_list_ground = listGround.getListGround();
                dataMyCustomAdapterGround = new MyCustomAdapterGround(activity, R.layout.list_layout_ground, arraylist_list_ground);
            }
        }else{
            initialize_listView_empty();


        }
        listView_ground.setAdapter(dataMyCustomAdapterGround);


        //LISTENERS
        fab_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listGround.addListGround(arraylist_list_ground);
                Intent intent = new Intent(activity, ActivityResult.class);
                intent.putExtra("listGround", new Gson().toJson(listGround));
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();

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
        ArrayList<Ground> list_tmp = new ArrayList<>();
        list_tmp.add(new Ground(getString(R.string.add_ground)));
        list_empty = true;
        dataMyCustomAdapterGround = new MyCustomAdapterGround(activity, R.layout.list_layout_ground, list_tmp);
        listView_ground.setAdapter(dataMyCustomAdapterGround);
    }

    private void udpate_listView() {
        if (dataMyCustomAdapterGround != null) {
            listView_ground.setAdapter(null);
        }

        dataMyCustomAdapterGround = new MyCustomAdapterGround(activity, R.layout.list_layout_ground, arraylist_list_ground);
        listView_ground.setAdapter(dataMyCustomAdapterGround);
    }

    public void add_ground() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View alertDialogView = factory.inflate(R.layout.dialog_add_ground, null);
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


                arraylist_list_ground.add(new Ground(editText_ground_name.getText().toString()));

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

            ViewHolder holder;

            if (convertViewProduit == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertViewProduit = vi.inflate(R.layout.list_layout_ground, null);

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
                                        arraylist_list_ground.remove(position);
                                        udpate_listView();
                                        dialog.cancel();
                                        if(arraylist_list_ground.size() == 0){
                                            initialize_listView_empty();
                                            Toast.makeText(activity, "size = " + arraylist_list_ground.size(), Toast.LENGTH_SHORT).show();
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


