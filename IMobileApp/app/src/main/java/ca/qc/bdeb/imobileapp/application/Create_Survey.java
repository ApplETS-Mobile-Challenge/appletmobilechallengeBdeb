package ca.qc.bdeb.imobileapp.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.objectModel.QuestionnaireTemplate;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

public class Create_Survey extends AppCompatActivity {

    private static final int QUESTIONNAIRE_CREATE = 1;
    private static final String QUESTIONNAIRE_TEMPLATE = "questionnaire";
    public static final int RESULT_SUCCES = 1;
    private ArrayList<Question> questionList;
    private ListView listViewQuestion;
    private Survey_Question_Adapter adapterActivite;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = DbHelper.getInstance(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_question);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Create_Survey.this, Add_Modify_Question.class);
                startActivityForResult(intent, 0);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionList = new ArrayList<>();
        listViewQuestion = (ListView) findViewById(R.id.Create_Survey_List);
        adapterActivite = new Survey_Question_Adapter(this, R.layout.layout_list_question, questionList);
        listViewQuestion.setAdapter(adapterActivite);
        listViewQuestion.setEmptyView(findViewById(R.id.empty_survey));
        adapterActivite.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_done){
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Create a questionnaire");
            alert.setMessage("Enter a title");
            // Create TextView
            final EditText input = new EditText (this);
            alert.setView(input);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String questionnaireName = input.getText().toString();
                    Calendar cal = Calendar.getInstance();
                    Questionnaire questionnaire = new Questionnaire(0, questionnaireName,
                            cal.getTime(), cal.getTime());
                    questionnaire.setQuestionList(questionList);
                    dbHelper.insertNewQuestionnaire(questionnaire);
                    QuestionnaireTemplate ques = new QuestionnaireTemplate();
                    ques = questionnaire.convertToQuestionnaireTemplate();
                    Intent intent = new Intent();
                    intent.putExtra(QUESTIONNAIRE_TEMPLATE, ques);
                    setResult(QUESTIONNAIRE_CREATE, intent);
                    finish();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == RESULT_SUCCES){
            Question question = (Question)data.getSerializableExtra("question");
            questionList.add(question);
           // adapterActivite.add(question);
            adapterActivite.notifyDataSetChanged();
        }
    }
}
