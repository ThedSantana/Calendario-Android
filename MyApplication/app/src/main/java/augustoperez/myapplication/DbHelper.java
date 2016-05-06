package augustoperez.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

        private static final String ID = "id";
        private static final String TABELA = "eventos";
        private static final String HORA = "hora";
        private static final String MINUTO = "minuto";
        private static final String DIA = "dia";
        private static final String MES = "mes";
        private static final String ANO = "ano";
        private static final String DATATOTAL = "datatoda";
        private static final String NOMEVENTO = "nomevento";
        private static final String DESCEVENTO = "descricaoevento";
        private static final String LOCALEVENTO ="localevento";
        private static final String NOME_BASE = "BlibliotecaDevento";
        private static final int VERSAO_BASE = 1;

    public DbHelper(Context context) {
        super(context,NOME_BASE, null ,VERSAO_BASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE"+TABELA+"("
                + ID + "integer primary key autoincrement,"
                + NOMEVENTO + "text,"
                + DESCEVENTO + "text,"
                + LOCALEVENTO + "text"
                +DATATOTAL+"timestamp"
                +")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
