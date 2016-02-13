package ca.qc.bdeb.imobileapp.application;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.qc.bdeb.imobileapp.R;
import mehdi.sakout.fancybuttons.FancyButton;


public class HomePage extends AppCompatActivity {

    private FancyButton btnSend;
    private FancyButton btnReceive;
    private FancyButton btnSurvey;
    private BluetoothAdapter mBluetoothAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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
                ensureDiscoverable();
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

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
            startActivity(discoverableIntent);
        }
    }
}
