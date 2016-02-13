package ca.qc.bdeb.imobileapp.application;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;

public class AnswerActivity extends AppCompatActivity implements
        AnswerFragment.OnFragmentInteractionListener {

    private static final String QUESTIONNAIRE = "questionnaire";

    private int currentIndex = 0;
    private Questionnaire questionnaire;
    private ArrayList<Boolean> goodAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goodAnswers = new ArrayList<>();
        questionnaire = (Questionnaire) getIntent().getSerializableExtra(QUESTIONNAIRE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_answer,
                AnswerFragment.newInstance(null, questionnaire.getQuestionnaireName())).commit();
    }

    @Override
    public void onFragmentInteraction(boolean goodAnswer) {
        goodAnswers.add(goodAnswer);
        Question question = questionnaire.getQuestionList().get(currentIndex);
        currentIndex++;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.container_answer,
                        AnswerFragment.newInstance(question, null)).commit();
    }

    @Override
    public void startAnswer() {
        Question question = questionnaire.getQuestionList().get(currentIndex);
        currentIndex++;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.container_answer,
                        AnswerFragment.newInstance(question, null)).commit();
    }
}
