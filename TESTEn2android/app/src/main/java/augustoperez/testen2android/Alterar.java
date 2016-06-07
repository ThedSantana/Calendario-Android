package augustoperez.testen2android;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Alterar extends Activity {

	EditText txvId_a;
	EditText txvNome_a;
	EditText txvEmail_a;
	EditText txvFone_a;
	DatePicker pickerDate;
	TimePicker pickerTime;
	Button buttonSetAlarm;
	Button buttondelAlarm;
	Button buttondelexcluir;
	Button buttoninicial;
	TextView info;


	String id_contato;
 public    Date dia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alterar);
		Intent intent = getIntent();
		Bundle params = intent.getExtras();
		id_contato = params.getString("id_contato");




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
		buttondelexcluir= (Button)findViewById(R.id.excluir);
		buttoninicial = (Button)findViewById(R.id.voltar);
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
								"data invalida",
								Toast.LENGTH_LONG).show();
					} else {
						Date dia = cal.getTime();
						String nome = "teste132";
						String compro = "agora na noruega";
						int res = alterarContato(id_contato, dia);
						if (res > 0) {
							Toast.makeText(getApplicationContext(),
									"Alterado com sucesso", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(getApplicationContext(),
									Listar.class));
						} else {
							Toast.makeText(getApplicationContext(), "Error ao alterar",
									Toast.LENGTH_SHORT).show();
						}
					}
				}


			}
		});
		buttondelexcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				GregorianCalendar current = (GregorianCalendar) GregorianCalendar.getInstance();
				GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
				cal.set(pickerDate.getYear(),
						pickerDate.getMonth(),
						pickerDate.getDayOfMonth(),
						pickerTime.getCurrentHour(),
						pickerTime.getCurrentMinute(),
						00);


					Date dia = cal.getTime();
					String nome = "teste132";
					String compro = "agora na noruega";
					int res = excluir(id_contato, dia);
					if (res > 0) {
						Toast.makeText(getApplicationContext(),
								"Alterado com sucesso", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(getApplicationContext(),
								Listar.class));
					} else {
						Toast.makeText(getApplicationContext(), "Error ao alterar",
								Toast.LENGTH_SHORT).show();
					}



			}
		});
		buttondelAlarm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSecondActivity();

			}
		});
		buttoninicial.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startSecondActivitymain();

			}
		});
	}
	public void startSecondActivity() {

		Intent secondActivity = new Intent(this, Listar.class);
		startActivity(secondActivity);
	}
	public void startSecondActivitymain() {

		Intent secondActivity = new Intent(this, MainActivity.class);
		startActivity(secondActivity);

	}
	protected int alterarContato(String id_contato,Date diaa) {

		SQLiteDatabase db = openOrCreateDatabase("agenda.db",
				Context.MODE_PRIVATE, null);

		ContentValues ctv = new ContentValues();
		ctv.put("dia", String.valueOf(diaa));
		int res = db.update("agenda", ctv, "_id=?",
				new String[]{id_contato});
		db.close();
		return res;

	}

	// funcao para obter o Contato clicado
	private void buscaContato(String id_contato) {

		SQLiteDatabase db = openOrCreateDatabase("agenda.db",
				Context.MODE_PRIVATE, null);

		String sql = "SELECT * from agenda where _id=?";

		Cursor c = (SQLiteCursor) db.rawQuery(sql, new String[] { id_contato });

		if (c.moveToFirst()) {
			String id = c.getString(c.getColumnIndex("_id"));


			// Log.i("teste: ", id +" " +nome);

			txvId_a.setText(id.toString());

		}
		c.close(); // fecha a conex�o
		db.close();
	}

	// m�todo para excluir
	protected int excluir(String id_contato,Date diaa) {

		SQLiteDatabase db = openOrCreateDatabase("agenda.db",
				Context.MODE_PRIVATE, null);

		ContentValues ctv = new ContentValues();
		ctv.put("dia", String.valueOf(diaa));
		int res = db.delete("agenda", "_id=?",
				new String[]{id_contato});
		db.close();
		return res;

	}

	// �rea de MENU



}
