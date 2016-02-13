package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;
import ca.qc.bdeb.imobileapp.modele.utilitaires.XmlParser;
import mehdi.sakout.fancybuttons.FancyButton;

public class BluetoothChatFragment extends Fragment {

    private static final String QUESTIONNAIRE_ID_KEY = "questionnaire_id_key";
    private static final boolean SECURE = true;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    private TextView mTitle;
    private FancyButton mScanButton;
    private FancyButton mSendButton;

    private DbHelper dbHelper;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private Questionnaire questionnaire;

    public static BluetoothChatFragment newInstance(int questionnaireId) {
        BluetoothChatFragment fragment = new BluetoothChatFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTIONNAIRE_ID_KEY, questionnaireId);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView status;
    private TextView create_Date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = DbHelper.getInstance(this.getContext());

        questionnaire = dbHelper.getOneQuestionnaire(getArguments().getInt(QUESTIONNAIRE_ID_KEY));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (questionnaire == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(getContext(), "Non-expected error", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(getContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (mChatService == null) {
            initialiserComposante();
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        create_Date = (TextView) view.findViewById(R.id.send_activity_creation);
        status = (TextView) view.findViewById(R.id.send_activity_status);
        mScanButton = (FancyButton) view.findViewById(R.id.send_activity_btn_scan);
        mSendButton = (FancyButton) view.findViewById(R.id.send_activity_btn_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void initialiserComposante() {

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            }
        });

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(XmlParser.parseToXml(questionnaire));
            }
        });

        mChatService = new BluetoothChatService(getActivity(), mHandler);
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);
            mOutStringBuffer.setLength(0);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            status.setText(getString(R.string.title_connected_to, mConnectedDeviceName));
                            status.setTextColor(Color.parseColor("#FF007E0A"));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            status.setText(R.string.title_connecting);
                            status.setTextColor(Color.parseColor("#FFC107"));
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            status.setText(R.string.title_not_connected);
                            status.setTextColor(Color.parseColor("#ff0900"));
                            break;
                    }
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    readSurvey(new String(readBuf, 0, msg.arg1));
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    initialiserComposante();
                } else {
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    private void connectDevice(Intent data) {
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device, SECURE);
    }
}