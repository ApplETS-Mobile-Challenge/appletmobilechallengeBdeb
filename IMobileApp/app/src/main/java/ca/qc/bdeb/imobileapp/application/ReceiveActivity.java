package ca.qc.bdeb.imobileapp.application;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.objectModel.QuestionnaireTemplate;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;
import ca.qc.bdeb.imobileapp.modele.utilitaires.XmlParser;
import mehdi.sakout.fancybuttons.FancyButton;

public class ReceiveActivity extends AppCompatActivity {

    private final static int DISCOVERABLE_DURATION = 10;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;
    private SendReceiveService mChatService = null;

    private DbHelper dbHelper;

    private FancyButton btnDiscoverable;
    private TextView txvStatus;
    private ListView lsvItems;

    private Survey_Adapter adapterActivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        dbHelper = DbHelper.getInstance(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lsvItems = (ListView) findViewById(R.id.content_reveive_lsv_items);
        adapterActivite = new Survey_Adapter(this, R.layout.layout_list_survey, new ArrayList<QuestionnaireTemplate>());
        lsvItems.setAdapter(adapterActivite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiserComposante();
        initialiserBtn();
    }

    private void initialiserComposante() {
        btnDiscoverable = (FancyButton) findViewById(R.id.content_receive_btn_discoverable);
        txvStatus = (TextView) findViewById(R.id.content_receive_txv_status);
        lsvItems = (ListView) findViewById(R.id.content_reveive_lsv_items);
    }

    private void initialiserBtn() {
        btnDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureDiscoverable();
            }
        });
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
            startActivity(discoverableIntent);
            btnDiscoverable.setEnabled(false);
        } else if (mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "You are already in discoverable mode", Toast.LENGTH_SHORT).show();
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case SendReceiveService.STATE_CONNECTED:
                            txvStatus.setText(getString(R.string.title_connected_to, mConnectedDeviceName));
                            txvStatus.setTextColor(Color.parseColor("#FF007E0A"));
                            break;
                        case SendReceiveService.STATE_CONNECTING:
                            txvStatus.setText(R.string.title_connecting);
                            txvStatus.setTextColor(Color.parseColor("#FFC107"));
                            break;
                        case SendReceiveService.STATE_LISTEN:
                        case SendReceiveService.STATE_NONE:
                            txvStatus.setText(R.string.title_not_connected);
                            txvStatus.setTextColor(Color.parseColor("#ff0900"));
                            break;
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != ReceiveActivity.this) {
                        Toast.makeText(ReceiveActivity.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    readSurvey(new String(readBuf, 0, msg.arg1));
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != ReceiveActivity.this) {
                        Toast.makeText(ReceiveActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void readSurvey(String stringServey) {
        Questionnaire questionnaire = XmlParser.readFromXml(stringServey);
        questionnaire.setQuestionnaireId(dbHelper.insertNewQuestionnaire(questionnaire));
        adapterActivite.add(questionnaire.convertToQuestionnaireTemplate());
        adapterActivite.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (mChatService == null) {
            mChatService = new SendReceiveService(this, mHandler);
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
            if (mChatService.getState() == SendReceiveService.STATE_NONE) {
                mChatService.start();
            }
        }
    }
}