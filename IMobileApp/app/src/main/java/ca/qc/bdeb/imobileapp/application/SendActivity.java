package ca.qc.bdeb.imobileapp.application;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ca.qc.bdeb.imobileapp.R;

public class SendActivity extends AppCompatActivity {

    public final static String QUESTIONNAIRE_KEY = "questionnaire_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int questionnaireId = getIntent().getIntExtra(QUESTIONNAIRE_KEY, 0);
        setContentView(R.layout.send_activity);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = BluetoothChatFragment.newInstance(questionnaireId);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}
