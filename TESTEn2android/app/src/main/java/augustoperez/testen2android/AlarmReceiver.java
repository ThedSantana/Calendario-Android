package augustoperez.testen2android;

/**
 * Created by augusto on 29/04/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Alarm teste teste!", Toast.LENGTH_LONG).show();

    }

}