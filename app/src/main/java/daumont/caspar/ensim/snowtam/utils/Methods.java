package daumont.caspar.ensim.snowtam.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import daumont.caspar.ensim.snowtam.Activities.ActivityStart;

/**
 * Created by Jonathan Daumont on 09/06/2017.
 */

public class Methods {

    /**
     * Permet de connaitre l'état de la connexion internet
     * @param activity
     * @return
     */
    public static boolean internet_diponible(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.compareTo(NetworkInfo.State.CONNECTED) == 0) //connecté à internet
            {
                return true;
            }
            else{
                activity.startActivity(new Intent(activity, ActivityStart.class));
                activity.finish();
                return false;
            }
        }else{
            activity.startActivity(new Intent(activity, ActivityStart.class));
            activity.finish();
            return false;
        }

    }

    public static boolean internet_diponible_activity_start(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.compareTo(NetworkInfo.State.CONNECTED) == 0) //connecté à internet
            {
                return true;
            }
            else{
                return false;
            }
        }
        else return false;
    }


    /**
     * Show a dialog to inform a user
     * @param message a
     * @param activity a
     */
    public static void info_dialog(String message,Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(message)
                .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

}


