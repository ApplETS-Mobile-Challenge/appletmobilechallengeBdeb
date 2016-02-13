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

    private static final int QUESTIONNAIRE_CREATE = 1;
    private static final String QUESTIONNAIRE_TEMPLATE = "questionnaire";
    private ListView listViewActivite;//
    private Survey_Adapter adapterActivite;
    private DbHelper dbHelper;
    private ArrayList<QuestionnaireTemplate> questionnaireList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__survey);

        dbHelper = DbHelper.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_Survey.this, Create_Survey.class);
                startActivityForResult(intent, QUESTIONNAIRE_CREATE);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionnaireList = dbHelper.getAllQuestionnaire();
        listViewActivite = (ListView) findViewById(R.id.content_list_survey_listView);
        adapterActivite = new Survey_Adapter(List_Survey.this, R.layout.layout_list_survey, questionnaireList);
        listViewActivite.setAdapter(adapterActivite);

        addListListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == QUESTIONNAIRE_CREATE) {
            QuestionnaireTemplate questionnaireTemplate =
                    (QuestionnaireTemplate) data.getSerializableExtra(QUESTIONNAIRE_TEMPLATE);
            questionnaireList.add(questionnaireTemplate);
            adapterActivite.notifyDataSetChanged();
        }
    }

    private void addListListener() {
        listViewActivite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final int questionnaireId = questionnaireList.get(position).questionnaireId;
                new BottomSheet.Builder(List_Survey.this).title(adapterActivite.getItem(position).questionnaireName).sheet(R.menu.menu_bottom_sheet).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_select:
                                Intent intentAnswer = new Intent(List_Survey.this, AnswerActivity.class);
                                intentAnswer.putExtra(AnswerActivity.QUESTIONNAIRE, dbHelper.getOneQuestionnaire(questionnaireId));
                                startActivity(intentAnswer);
                                break;
                            case R.id.action_delete:
                                dbHelper.deleteQuestionnaire(dbHelper.getOneQuestionnaire(questionnaireId));
                                questionnaireList.remove(position);
                                adapterActivite.notifyDataSetChanged();
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
