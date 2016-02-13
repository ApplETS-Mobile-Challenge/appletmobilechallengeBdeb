package ca.qc.bdeb.imobileapp.modele.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Olivier on 2015-09-25.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db"; // Nom de db
    public static final int DBVERSION = 1; // Version db
    private Context context;
    private static DbHelper instance = null;

    //NOS DES TABLES
    private static final String TABLE_1 = "table1";
    private static final String TABLE_2 = "table2";
    private static final String TABLE_3 = "table3";


    //NOM DES COLONNES TABLE 1
    private static final String COLONNE1 = "_id";

    //NOM DES COLONNES TABLE 2
    private static final String COLONNE2 = "_id";

    //NOM DES COLONNES TABLE 3
    private static final String COLONNE3 = "_id";


    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DBVERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     //   String sqlClient = "CREATE TABLE " + TABLE_NAME_TYPE + "("
     //           + TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE_MEDIA + " TEXT NOT NULL)";
     //   db.execSQL(sqlClient);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
