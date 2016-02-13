package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;
import ca.qc.bdeb.imobileapp.modele.utilitaires.XmlParser;
import mehdi.sakout.fancybuttons.FancyButton;


public class HomePage extends AppCompatActivity {

    private FancyButton btnReceive;
    private FancyButton btnSurvey;

    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;
    private static final int REQUEST_ENABLE_BT = 3;
    private BluetoothChatService mChatService = null;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dbHelper = DbHelper.getInstance(this);

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

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (mChatService == null) {
            mChatService = new BluetoothChatService(this, mHandler);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
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


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != HomePage.this) {
                        Toast.makeText(HomePage.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    readSurvey(new String(readBuf, 0, msg.arg1));
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != HomePage.this) {
                        Toast.makeText(HomePage.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void readSurvey(String stringServey) {
        Questionnaire questionnaire = XmlParser.readFromXml(stringServey);
        dbHelper.insertNewQuestionnaire(questionnaire);
    }
}
