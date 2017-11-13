package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import daumont.caspar.ensim.snowtam.R;

public class ActivityResult extends AppCompatActivity {

    private FloatingActionButton fab_maps;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        activity = this;
        fab_maps = (FloatingActionButton)findViewById(R.id.fab_maps);

        fab_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityMaps.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();
            }
        });
    }
}
