package ca.qc.bdeb.imobileapp.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cocosw.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.objectModel.QuestionnaireTemplate;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

public class List_Survey extends AppCompatActivity {

    private List<QuestionnaireTemplate> questionnaireList;
    private ListView listViewActivite;
    private Survey_Adapter adapterActivite;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__survey);

        dbHelper = DbHelper.getInstance(this);

        Questionnaire questionnaire = new Questionnaire(0, "Cat names", new Date(), new Date());
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question(0, "What is the name of the africain cat", 0));
        questionnaire.setQuestionList(questions);

        dbHelper.insertNewQuestionnaire(questionnaire);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionnaireList = dbHelper.getAllQuestionnaire();

        listViewActivite = (ListView) findViewById(R.id.content_list_survey_listView);
        adapterActivite = new Survey_Adapter(List_Survey.this, R.layout.layout_list_survey, questionnaireList);
        listViewActivite.setAdapter(adapterActivite);

        adapterActivite.notifyDataSetChanged();

        addListListener();
    }

    private void addListListener() {
        listViewActivite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int questionnaireId = adapterActivite.getItem(position).questionnaireId;
                new BottomSheet.Builder(List_Survey.this).title(adapterActivite.getItem(position).questionnaireName).sheet(R.menu.menu_bottom_sheet).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_select:

                                break;
                            case R.id.action_modify:

                                break;
                            case R.id.action_send:
                                Intent intent = new Intent(List_Survey.this, SendActivity.class);
                                intent.putExtra(SendActivity.QUESTIONNAIRE_KEY, questionnaireId);
                                startActivity(intent);
                                break;
                        }
                    }
                }).show();
            }
        });
    }

}