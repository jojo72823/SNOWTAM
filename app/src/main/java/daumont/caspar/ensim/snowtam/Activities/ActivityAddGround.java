package daumont.caspar.ensim.snowtam.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import daumont.caspar.ensim.snowtam.R;


public class ActivityAddGround extends AppCompatActivity {

    private FloatingActionButton fab_result;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ground);
        activity = this;

        fab_result = (FloatingActionButton)findViewById(R.id.fab_result);

        fab_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityResult.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in, R.anim.push_out);
                finish();
            }
        });

    }
}
