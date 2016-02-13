package ca.qc.bdeb.imobileapp.application;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;

public class SendActivity extends AppCompatActivity {

    private final static String QUESTIONNAIRE_KEY = "questionnaire_key";

    Questionnaire questionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Questionnaire questionnaire = (Questionnaire) getIntent().getSerializableExtra(QUESTIONNAIRE_KEY);
        setContentView(R.layout.send_activity);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = BluetoothChatFragment.newInstance(questionnaire);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}
