package ca.qc.bdeb.imobileapp.modele.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;

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
    private static final String QUESTION_QUESTION = "question";
    private static final String QUESTION_QUESTIONNAIRE_REFERENCE_ID = "questionnaireId";

    //NOM DES COLONNES TABLE ANSWER CHOICES
    private static final String ANSWER_CHOICES_ID = "_id";
    private static final String ANSWER_QUESTION_REFERENCE_ID = "questionId";
    private static final String ANSWER_CHOICE_CHOICE = "choice";
    private static final String ANSWER_VERACITY = "veracity";


    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
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
                QUESTION_QUESTION + " TEXT," +
                QUESTION_QUESTIONNAIRE_REFERENCE_ID + " INTEGER," +
                "FOREIGN KEY(" + QUESTION_QUESTIONNAIRE_REFERENCE_ID +") REFERENCES " + QUESTIONNAIRE_TABLE_NAME+"(" + QUESTIONNAIRE_ID + "))";
        db.execSQL(sqlLine);
    }

    private void createAnswerChoicesTable(SQLiteDatabase db) {
        String sqlLine = "CREATE TABLE " + ANSWER_CHOICES_TABLE_NAME +
                "(" + ANSWER_CHOICES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ANSWER_VERACITY + " INTEGER," +
                ANSWER_CHOICE_CHOICE + " TEXT," +
                ANSWER_QUESTION_REFERENCE_ID + " INTEGER," +
                "FOREIGN KEY(" + ANSWER_QUESTION_REFERENCE_ID +") REFERENCES " + QUESTION_TABLE_NAME+"(" + QUESTION_ID + "))";
        db.execSQL(sqlLine);
    }

    public void insertNewQuestionnaire(Questionnaire questionnaire) {
        SQLiteDatabase db;
        ContentValues values = new ContentValues();

        db = this.getWritableDatabase();
        values.put(QUESTIONNAIRE_NAME, questionnaire.getQuestionnaireName());
        values.put(QUESTIONNAIRE_CREATION_DATE, questionnaire.getCreationDate().getTime());
        values.put(QUESTIONNAIRE_EDIT_DATE, questionnaire.getEditDate().getTime());
        int questionnaireId = (int) db.insert(QUESTIONNAIRE_TABLE_NAME, null, values);
        db.close();
        insertNewQuestions(questionnaire.getQuestionList(), questionnaireId);
    }

    public void insertNewQuestions(ArrayList<Question> questions, int questionnaireId) {
        SQLiteDatabase db;
        ContentValues values;

        for (Question question : questions) {
            db = this.getWritableDatabase();
            values = new ContentValues();
            values.put(QUESTION_QUESTION, question.getQuestion());
            values.put(QUESTION_QUESTIONNAIRE_REFERENCE_ID,  questionnaireId);
            int questionId = (int) db.insert(QUESTION_TABLE_NAME, null, values);
            db.close();
            insertNewAnswerChoices(question.getAnswerChoices(), questionId);
        }
    }

    public void insertNewAnswerChoices(HashMap<String , Boolean> answerChoices,
                                        int questionId) {
        SQLiteDatabase db;

        ContentValues values;
        for(Entry<String , Boolean> answer : answerChoices.entrySet() ) {
            db = this.getWritableDatabase();
            String answerChoice = answer.getKey();
            boolean veracity = answer.getValue();
            values = new ContentValues();
            values.put(ANSWER_CHOICE_CHOICE, answerChoice);
            values.put(ANSWER_VERACITY, veracity ? 1 : 0);
            values.put(ANSWER_QUESTION_REFERENCE_ID, questionId);
            db.insert(ANSWER_CHOICES_TABLE_NAME, null, values);
            db.close();
        }
    }



    public void deleteQuestionnaire(Questionnaire questionnaire) {
        deleteQuestions(questionnaire.getQuestionList(),questionnaire.getQuestionnaireId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QUESTION_TABLE_NAME, QUESTIONNAIRE_ID + "=?",
                new String[]{Integer.toString(questionnaire.getQuestionnaireId())});
        db.close();
    }

    public void deleteQuestions(ArrayList<Question> questions,int questionnaireId) {
        deleteAnswerChoices(questions);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QUESTION_TABLE_NAME, QUESTION_QUESTIONNAIRE_REFERENCE_ID + "=?",
                new String[]{Integer.toString(questionnaireId)});
        db.close();
    }

    public void deleteAnswerChoices(ArrayList<Question> questions) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(Question question : questions) {
            db.delete(ANSWER_CHOICES_TABLE_NAME, ANSWER_QUESTION_REFERENCE_ID + "=?",
                    new String[]{Integer.toString(question.getQuestionId())});
        }
        db.close();
    }
}
