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

    int[] tab = new int[]{1,2,3,4,5,6,7};
    int Liveindex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Questionnaire questionnaire = (Questionnaire) getIntent().getSerializableExtra(QUESTIONNAIRE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_answer,
                AnswerFragment.newInstance(null, "kajdhgkj")).commit();
    }

    @Override
    public void onFragmentInteraction(boolean goodAnswer) {
        Question ques = new Question(0,"ajsdhkfjh", 0);
        ques.addAnswerChoices("hahhs", false);
        ques.addAnswerChoices("ahsdf", false);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.container_answer,
                        AnswerFragment.newInstance(ques, null)).commit();
    }
}
