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
    private static final String QUESTIONNAIRE_TABLE_NAME = "Questionnaire";
    private static final String QUESTION_TABLE_NAME = "Question";
    private static final String ANSWER_CHOICES_TABLE_NAME = "answerChoices";


    //NOM DES COLONNES TABLE QUESTIONNAIRE
    private static final String QUESTIONNAIRE_ID = "_id";
    private static final String QUESTIONNAIRE_NAME = "name";
    private static final String QUESTIONNAIRE_CREATION_DATE = "creationDate";
    private static final String QUESTIONNAIRE_EDIT_DATE = "editDate";

    //NOM DES COLONNES TABLE QUESTION
    private static final String QUESTION_ID = "_id";
    private static final String QUESTION_QUESTIONNAIRE_REFERENCE_ID = "questionnaireId";

    //NOM DES COLONNES TABLE ANSWER CHOICES
    private static final String ANSWER_CHOICES_ID = "_id";
    private static final String ANSWER_QUESTION_REFERENCE_ID = "questionId";
    private static final String ANSWER_VERACITY = "veracity";


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
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db) {
        createQuestionnaireTable(db);
        createQuestionnTable(db);
        createAnswerChoicesTable(db);
    }

    private void createQuestionnaireTable(SQLiteDatabase db) {
        String sqlLine = "CREATE TABLE " + QUESTIONNAIRE_TABLE_NAME +
                "(" + QUESTIONNAIRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QUESTIONNAIRE_NAME + " TEXT," +
                QUESTIONNAIRE_CREATION_DATE + " INTEGER," +
                QUESTIONNAIRE_EDIT_DATE + " INTEGER)";
        db.execSQL(sqlLine);
    }

    private void createQuestionnTable(SQLiteDatabase db) {
        String sqlLine = "CREATE TABLE " + QUESTION_TABLE_NAME +
                "(" + QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOREIGN KEY(" + QUESTION_QUESTIONNAIRE_REFERENCE_ID +") REFERENCES " + QUESTIONNAIRE_TABLE_NAME+"(" + QUESTIONNAIRE_ID + "))";
        db.execSQL(sqlLine);
    }

    private void createAnswerChoicesTable(SQLiteDatabase db) {
        String sqlLine = "CREATE TABLE " + ANSWER_CHOICES_TABLE_NAME +
                "(" + ANSWER_CHOICES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ANSWER_VERACITY + " INTEGER," +
                "FOREIGN KEY(" + ANSWER_QUESTION_REFERENCE_ID +") REFERENCES " + QUESTION_TABLE_NAME+"(" + QUESTION_ID + "))";
        db.execSQL(sqlLine);
    }
}
