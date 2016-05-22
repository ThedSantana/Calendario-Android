package augustoperez.testen2android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

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

        criarTabela();

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
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                GregorianCalendar current = (GregorianCalendar) GregorianCalendar.getInstance();
                if (arg0.getId() == R.id.setalarm) {

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
                    } else {
                       Date dia = cal.getTime();
                        int mes = cal.get(GregorianCalendar.MONTH);
                        int ano = cal.get(GregorianCalendar.YEAR);
                        int hora = cal.get(GregorianCalendar.HOUR);
                        int min = cal.get(GregorianCalendar.MINUTE);
                        String nome= "teste132";
                        String compro = "agora na noruega";
                        salvarContato(dia,mes,ano,hora,min,compro,nome);
                        setAlarm(cal);
                        ;
                    }
                }


            }
        });
        buttondelAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSecondActivity();

            }
        });

    }


    public void startSecondActivity() {

        Intent secondActivity = new Intent(this, Listar.class);
        startActivity(secondActivity);
    }

    private void setAlarm(GregorianCalendar targetCal) {

        info.setText("\n\n***\n"
                + "Alarm is set@ " + targetCal.getTime() + "\n"
                + "***\n " + targetCal.get(GregorianCalendar.MONTH));

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

        PendingIntent alarme = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), alarme);
    }
    public void criarTabela() {
        SQLiteDatabase db = null;
        try {
            db = openOrCreateDatabase("agenda.db", Context.MODE_PRIVATE, null);

            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS agenda(");
            sql.append("_id integer primary key autoincrement,");
            sql.append("dia timestamp,");
            sql.append("meses integer,");
            sql.append("ano integer,");
            sql.append("hora integer,");
            sql.append("minutos integer,");
            sql.append("descricao varchar(120),");
            sql.append("nome varchar(20))");

            db.execSQL(sql.toString());

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error Ocorreu",
                    Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public void salvarContato(Date dia,int mes,int ano,int hora,int min,String descricao,String nome) {
        SQLiteDatabase db = null;
        try {

            db = openOrCreateDatabase("agenda.db", Context.MODE_PRIVATE, null);

            ContentValues contentInsert = new ContentValues();
            contentInsert.put("dia", String.valueOf(dia));
            contentInsert.put("meses",mes);
            contentInsert.put("ano", ano);
            contentInsert.put("hora", hora);
            contentInsert.put("minutos", min);
            contentInsert.put("descricao", descricao);
            contentInsert.put("nome", nome);

            db.insert("agenda", null, contentInsert);


        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error ao inserir",
                    Toast.LENGTH_SHORT).show();
        } finally {
            Toast.makeText(getApplicationContext(), "Dados Cadastrados",Toast.LENGTH_SHORT).show();
            // chama a listagem
            db.close();

        }
    }

}
