package augustoperez.myapplication;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import augustoperez.myapplication.R;

public class MainActivity extends Activity{

    DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm;
    Button buttondelAlarm;
    TextView info;

    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);
        pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        pickerTime = (TimePicker)findViewById(R.id.pickertime);

        Calendar now = Calendar.getInstance();

        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));

        buttonSetAlarm = (Button)findViewById(R.id.setalarm);
        buttondelAlarm = (Button)findViewById(R.id.delalarm);
        buttonSetAlarm.setOnClickListener(new OnClickListener(){

            @Override

            public void onClick(View arg0) {
                GregorianCalendar current = (GregorianCalendar) GregorianCalendar.getInstance();
                if(arg0.getId() == R.id.setalarm) {

                    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                    cal.set(pickerDate.getYear(),
                            pickerDate.getMonth(),
                            pickerDate.getDayOfMonth(),
                            pickerTime.getCurrentHour(),
                            pickerTime.getCurrentMinute(),
                            00);

                    if (cal.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getApplicationContext(),
                                "Invalid Date/Time",
                                Toast.LENGTH_LONG).show();
                    } else { Toast.makeText(getApplicationContext(),
                            "Data ou Hora Inválida",
                            Toast.LENGTH_LONG).show();
                        setAlarm(cal);
                        Toast.makeText(getApplicationContext(),
                                "uyyyyyyyye",
                                Toast.LENGTH_LONG).show();
                    }
                }
                if(arg0.getId() == R.id.delalarm){


                }

            }});
    }

    private void setAlarm(GregorianCalendar targetCal){

        info.setText("\n\n***\n"
                + "Alarm is set@ " + targetCal.getTime() + "\n"
                + "***\n " +  targetCal.get(GregorianCalendar.DAY_OF_MONTH));

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

        PendingIntent alarme = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), alarme);
    }

}