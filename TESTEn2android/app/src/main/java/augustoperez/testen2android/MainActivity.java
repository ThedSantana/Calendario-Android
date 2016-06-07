package augustoperez.testen2android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity implements OnItemSelectedListener{

    DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm;
    Button buttondelAlarm;
    TextView info;
    final static int RQS_1 = 1;
    Spinner spinner;
    Button btnAdd;
    Button btnRemove;
    EditText inputLabel;
    String eventno= null ;

    public class DatabaseHandler extends SQLiteOpenHelper{
        // Database Version
        private static final int DATABASE_VERSION = 1;
        // Database Name
        private static final String DATABASE_NAME = "spinnerExample";
        // Labels table name
        private static final String TABLE_LABELS = "labels";
        // Labels Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Category table create query
            String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
            db.execSQL(CREATE_CATEGORIES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
            // Create tables again
            onCreate(db);
        }

        public void insertLabel(String label) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, label);

            // Inserting Row
            db.insert(TABLE_LABELS, null, values);
            db.close(); // Closing database connection

        }
        public List<String> getAllLabels(){
            List<String> labels = new ArrayList<String>();
// Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_LABELS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    labels.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
// closing connection
            cursor.close();
            db.close();
// returning lables
            return labels;
        }

        /**
         * Removing label
         * */
        public void removeLabel(String label){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, label);

// Removing Row
            db.delete(TABLE_LABELS, KEY_NAME + "=?", new String[] {label});
            db.close(); // Closing database connection
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        // add button
        btnAdd = (Button) findViewById(R.id.btn_add);
        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_Label);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        btnRemove = (Button) findViewById(R.id.btn_remove);
        // Loading spinner data from database
        loadSpinnerData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String label = inputLabel.getText().toString();

                if (label.trim().length() > 0) {
                    // database handler
                    DatabaseHandler db = new DatabaseHandler(
                            getApplicationContext());

                    // inserting new label into database
                    db.insertLabel(label);

                    // making input filed text to blank
                    inputLabel.setText("");

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);

                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, insira um nome!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DatabaseHandler db = new DatabaseHandler(
                        getApplicationContext());

                String selected = (String) spinner.getSelectedItem();

                db.removeLabel(selected);
                loadSpinnerData();
            }
        });
        //spinner

        criarTabela();
        Toast.makeText(getApplicationContext(),
                "tela 1",
                Toast.LENGTH_LONG).show();
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
                    int label1 =0;
                    EditText Nome = (EditText) findViewById(R.id.rep);
                    if (cal.compareTo(current) <= 0 || Nome.length()<=0) {
                        //The set Date/Time already passed
                            if( Nome.length()<=0) {
                                Toast.makeText(getApplicationContext(),
                                        "campo repetir alarma vazio",
                                        Toast.LENGTH_LONG).show();
                            }
                        else{
                                Toast.makeText(getApplicationContext(),
                                        "data invalida",
                                        Toast.LENGTH_LONG).show();
                            }

                    } else {
                       Date dia = cal.getTime();
                        String nome= "teste132";
                        String compro = "agora na noruega";
                        salvarContato(dia,compro,nome,eventno);
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

    //spinner
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }
    //spinner

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
            sql.append("meses varchar(120),");
            sql.append("descricao varchar(120),");
            sql.append("repetir integer)");

            db.execSQL(sql.toString());

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error Ocorreu",
                    Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }//fim criar tabela

    public void salvarContato(Date dia,String descricao,String nomee,String tipoeven) {
        SQLiteDatabase db = null;
        try {
            String selected = (String) spinner.getSelectedItem();
            db = openOrCreateDatabase("agenda.db", Context.MODE_PRIVATE, null);
            final EditText Nome = (EditText) findViewById(R.id.rep);
            String nome = Nome.getText().toString();
            int i = Integer.parseInt(nome);
            selected = (String) spinner.getSelectedItem();
            ContentValues contentInsert = new ContentValues();
            contentInsert.put("dia", String.valueOf(dia));
            contentInsert.put("meses",eventno);
            contentInsert.put("repetir",i);

            db.insert("agenda", null, contentInsert);


        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Erro ao inserir",
                    Toast.LENGTH_SHORT).show();
        } finally {
            Toast.makeText(getApplicationContext(), "Dados Cadastrados",Toast.LENGTH_SHORT).show();
            // chama a listagem
            db.close();

        }
    }

    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        eventno = label;
        Toast.makeText(parent.getContext(), "Voce selecionou: " + label,
                Toast.LENGTH_LONG).show();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
    //spinner

}
