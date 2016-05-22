package augustoperez.testen2android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Listar extends Activity implements OnItemClickListener {

	SQLiteDatabase db;
	Cursor cursor;
	SimpleCursorAdapter ad;
	ListView listViewContatos;
	Button buttondelAlarm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar);

		buscarDados();
		criarListagem();

	}

	public void buscarDados(){

		try{
			db = openOrCreateDatabase("agenda.db",Context.MODE_PRIVATE, null);
			cursor = db.rawQuery("SELECT * from agenda", null);

		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}
	}


	public void criarListagem(){


		listViewContatos = (ListView) findViewById(R.id.list);

		String[] from = {"_id","dia", "meses", "ano","hora"}; // nome dos campos da tabela
		int[] to = {R.id.txvId, R.id.txvContatoNome, R.id.txvContatoEmail, R.id.txvContatoFone,R.id.txvhora};// campos do model

		ad = new SimpleCursorAdapter(getApplicationContext(), R.layout.model_list_view_contatos, cursor, from, to);


		listViewContatos.setOnItemClickListener(this);

		listViewContatos.setAdapter(ad);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		// Forma 1
		//Cursor c = (Cursor) ad.getItem(position);
		//String nome = c.getString(c.getColumnIndex("nome"));

		SQLiteCursor sqlCursor = (SQLiteCursor) ad.getItem(position);
		String nome = sqlCursor.getString(sqlCursor.getColumnIndex("dia"));


		String id_contato = sqlCursor.getString(sqlCursor.getColumnIndex("_id"));
		Toast.makeText(getApplicationContext(), "Selecionou o nome: " + nome + " e id: "+id_contato, Toast.LENGTH_SHORT).show();

		// chama a tela de alteração
		//Intent altera = new Intent(getApplicationContext(), Alterar.class);
		//altera.putExtra("id_contato", id_contato);
		//startActivity(altera);

	}

	// Área de MENU



	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

			case R.id.listmenu:
				startActivity(new Intent(this, Listar.class));
				return true;
			case R.id.menu:
				startActivity(new Intent(this,MainActivity.class));
				return true;
		}
		return false;
	}


}
