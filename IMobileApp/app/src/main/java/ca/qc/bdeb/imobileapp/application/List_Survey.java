package ca.qc.bdeb.imobileapp.application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

public class List_Survey extends AppCompatActivity {

    private List<Questionnaire> questionnaireList;
    private ListView listViewActivite;
    private Survey_Adapter adapterActivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DbHelper db = DbHelper.getInstance(this);
//        db.
                Question q = new Question(0, "twetw", 0);
        ArrayList<Question> listQuestion = new ArrayList<>();
        listQuestion.add(q);
        Calendar calendar = Calendar.getInstance();
        Questionnaire questionnaire = new Questionnaire(0, "tset", calendar.getTime(), calendar.getTime(),listQuestion );
        questionnaireList = new ArrayList<>();
        questionnaireList.add(questionnaire);

                //todo get all questionnaire
                listViewActivite = (ListView) findViewById(R.id.content_list_survey_listView);
        adapterActivite = new Survey_Adapter(List_Survey.this, R.id.content_list_survey_listView, questionnaireList);
        listViewActivite.setAdapter(adapterActivite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
