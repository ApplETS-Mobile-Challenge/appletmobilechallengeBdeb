package ca.qc.bdeb.imobileapp.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.qc.bdeb.imobileapp.R;
import mehdi.sakout.fancybuttons.FancyButton;


public class HomePage extends AppCompatActivity {

    FancyButton btnSend;
    FancyButton btnReceive;
    FancyButton btnSurvey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeComponent();
        initializeBtnsClick();
    }

    private void initializeComponent() {

        btnReceive = (FancyButton) findViewById(R.id.home_page_btn_recevoir);
        btnSurvey = (FancyButton) findViewById(R.id.home_page_btn_voir_questionnaire);
    }

    private void initializeBtnsClick(){

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, SendActivity.class);
                startActivity(intent);
            }
        });

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, List_Survey.class);
                startActivity(intent);
            }
        });
    }
}
