package augustoperez.testen2android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter ad;
    ListView listViewContatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }
    public void buscarDados(){

        try{
            db = openOrCreateDatabase("agenda.db", Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * from agenda", null);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public void criarListagem(){

        // pega o listview que conterá os itens
        listViewContatos = (ListView) findViewById(R.id.list);
		/*
		 * TODO
		 * Inserir o campo id para referenciar o Contato para alteração
		 * Buscar o campo TextView do arquivo XML onde é feita a listagem
		*/
        String[] from = {"_id","dia", "mes", "ano"}; // nome dos campos da tabela
        int[] to = {R.id.txvId, R.id.txvContatoNome, R.id.txvContatoEmail, R.id.txvContatoFone};// campos do model

        ad = new SimpleCursorAdapter(getApplicationContext(), R.layout.model_list_view_contatos, cursor, from, to);

        // habilita o click no item da lista
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

		/* TODO - obter o Id do objeto selecionado e enviar para a alteração */
        String id_contato = sqlCursor.getString(sqlCursor.getColumnIndex("_id"));
        Toast.makeText(getApplicationContext(), "Selecionou o nome: " + nome + " e id: "+id_contato, Toast.LENGTH_SHORT).show();

        // chama a tela de alteração
        //Intent altera = new Intent(getApplicationContext(), Alterar.class);
        //altera.putExtra("id_contato", id_contato);
        //startActivity(altera);

    }

}
