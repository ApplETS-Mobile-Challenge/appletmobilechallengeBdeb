package ca.qc.bdeb.imobileapp.modele.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.objectModel.QuestionnaireTemplate;

import static ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire.*;

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

    public int insertNewQuestionnaire(Questionnaire questionnaire) {
        SQLiteDatabase db;
        ContentValues values = new ContentValues();

        db = this.getWritableDatabase();
        values.put(QUESTIONNAIRE_NAME, questionnaire.getQuestionnaireName());
        values.put(QUESTIONNAIRE_CREATION_DATE, questionnaire.getCreationDate().getTime());
        values.put(QUESTIONNAIRE_EDIT_DATE, questionnaire.getEditDate().getTime());
        int questionnaireId = (int) db.insert(QUESTIONNAIRE_TABLE_NAME, null, values);
        db.close();
        insertNewQuestions(questionnaire.getQuestionList(), questionnaireId);
        return questionnaireId;
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

    public void updateQuestionnaire(Questionnaire questionnaire) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUESTIONNAIRE_NAME, questionnaire.getQuestionnaireName());
        values.put(QUESTIONNAIRE_CREATION_DATE, questionnaire.getCreationDate().getTime());
        values.put(QUESTIONNAIRE_EDIT_DATE, questionnaire.getEditDate().getTime());
        db.update(QUESTION_TABLE_NAME, values, QUESTIONNAIRE_ID + "=?",
                new String[]{Integer.toString(questionnaire.getQuestionnaireId())});
        db.close();
    }

    public void updateAnswerChoices(int questionId, String answerChoice, boolean veracity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ANSWER_CHOICE_CHOICE, answerChoice);
        values.put(ANSWER_VERACITY, veracity ? 1 : 0);
        db.update(ANSWER_CHOICES_TABLE_NAME, values, ANSWER_QUESTION_REFERENCE_ID + "=?",
                new String[]{Integer.toString(questionId)});
        db.close();
    }

    public void updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUESTION_QUESTION, question.getQuestion());
        db.update(QUESTION_TABLE_NAME, values, QUESTION_ID + "=?",
                new String[]{Integer.toString(question.getQuestionId())});
        db.close();
    }

    public ArrayList<QuestionnaireTemplate> getAllQuestionnaire() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<QuestionnaireTemplate> templates = new ArrayList<>();
        QuestionnaireTemplate template = new QuestionnaireTemplate();
        String query = "select " + QUESTIONNAIRE_TABLE_NAME + "."
                + QUESTIONNAIRE_ID + ", " + QUESTIONNAIRE_NAME + ", "
                + QUESTIONNAIRE_EDIT_DATE + ", COUNT(" + QUESTION_TABLE_NAME + "." + QUESTION_ID
                + ") from " + QUESTIONNAIRE_TABLE_NAME + ", " + QUESTION_TABLE_NAME
                + " WHERE " + QUESTIONNAIRE_TABLE_NAME + "." + QUESTIONNAIRE_ID + " = "
                + QUESTION_TABLE_NAME + "." + QUESTION_QUESTIONNAIRE_REFERENCE_ID
                + " GROUP BY " + QUESTION_TABLE_NAME + "." + QUESTION_QUESTIONNAIRE_REFERENCE_ID;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            template.questionnaireId = Integer.parseInt(cursor.getString(0));
            template.questionnaireName = cursor.getString(1);
            template.editDate = new Date(Long.parseLong(cursor.getString(2)));
            template.numberOfAnwer = Integer.parseInt(cursor.getString(3));
            templates.add(template);
            while(cursor.moveToNext()) {
                template = new QuestionnaireTemplate();
                template.questionnaireId = Integer.parseInt(cursor.getString(0));
                template.questionnaireName = cursor.getString(1);
                template.editDate = new Date(Long.parseLong(cursor.getString(2)));
                template.numberOfAnwer = Integer.parseInt(cursor.getString(3));
                templates.add(template);
            }
        }

        return templates;
    }

    public Questionnaire getOneQuestionnaire(int questionnaireId) {
        Questionnaire questionnaire = null;
        String query = "select * from " + QUESTIONNAIRE_TABLE_NAME + " where "
                + QUESTIONNAIRE_ID + "=" + questionnaireId;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            questionnaire = new Questionnaire(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), new Date(Long.parseLong(cursor.getString(2))),
                    new Date(Long.parseLong(cursor.getString(3))));
            db.close();
            questionnaire.setQuestionList(getQuestionnaireQuestions(questionnaireId));
        }
        return questionnaire;
    }

    private ArrayList<Question> getQuestionnaireQuestions(int questionnaireId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Question question = null;
        ArrayList<Question> questions = new ArrayList<>();
        String query = "select * from " + QUESTION_TABLE_NAME + " where " + QUESTION_QUESTIONNAIRE_REFERENCE_ID
                + "=" + questionnaireId;

       // String query = "select * from " + QUESTION_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
             question = new Question(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)));
            questions.add(question);
            while (cursor.moveToNext()) {
                question = new Question(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                questions.add(question);
            }
            db.close();
        }
        getQuestionAnswerChoices(questions);
        return questions;
    }

    private void getQuestionAnswerChoices(ArrayList<Question> questions) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query;
        Cursor cursor = null;
        for(int i = 0; i < questions.size(); ++i) {
            query = "select " + ANSWER_CHOICE_CHOICE + ", " + ANSWER_VERACITY + " from "
                    + ANSWER_CHOICES_TABLE_NAME + " where " + ANSWER_QUESTION_REFERENCE_ID
                    + "=" + questions.get(i).getQuestionId();
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                questions.get(i).addAnswerChoices(cursor.getString(0),
                        Integer.parseInt(cursor.getString(1)) == 1);
                while (cursor.moveToNext()) {
                    questions.get(i).addAnswerChoices(cursor.getString(0),
                            Integer.parseInt(cursor.getString(1)) == 1);
                }
            }
        }
    }
}
