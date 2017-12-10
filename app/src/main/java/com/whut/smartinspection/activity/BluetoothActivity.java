package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.whut.smartinspection.R;

import java.util.ArrayList;
import java.util.List;

import cn.com.reformer.rfBleService.BleDevContext;
import cn.com.reformer.rfBleService.BleService;
import cn.com.reformer.rfBleService.OnCompletedListener;
import cn.com.reformer.rfBleService.OnPasswordWriteListener;

public class BluetoothActivity extends Activity {
//    private static final String VK = "3131313131313131";//"37E87C4539A45D23";
    private BleService mService;

    private Spinner sp_action;
    private EditText et_num;
    private EditText et_password;
    private EditText et_outputTime;
    private Button btn_refreshDevList;
    private ListView lsv_door;
    private TextView tv_result;
    private TextView bluetooth_back;

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private BleService.RfBleKey rfBleKey = null;
    private ArrayList<byte[]> mWhiteList;
    private ArrayAdapter<String> adapter;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder rawBinder) {
            mService = ((BleService.LocalBinder) rawBinder).getService();
            rfBleKey = mService.getRfBleKey();
            rfBleKey.init(null);//mWhiteList
            rfBleKey.setOnCompletedListener(new OnCompletedListener() {
                @Override
                public void OnCompleted(byte[] bytes, int i) {
                    final int result = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (result) {
                                case 0:
                                    tv_result.setText(R.string.result_Success);
                                    break;
                                case 1:
                                    tv_result.setText(R.string.result_password_error);
                                    break;
                                case 2:
                                    tv_result.setText(R.string.result_bluetooth_break);
                                    break;
                                case 3:
                                    tv_result.setText(R.string.result_timeout);
                                    break;
                            }
                        }
                    });
                }
            });


            rfBleKey.setOnPasswordWriteListener(new OnPasswordWriteListener() {
                @Override
                public void OnPasswordWrite(byte[] bytes, int i) {
                    final int result = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result == 0) {
                                tv_result.setText(R.string.result_set_success);
                            } else if (result == 1) {
                                tv_result.setText(R.string.result_set_failed);
                            }
                        }
                    });
                }
            });

            rfBleKey.setOnBleDevListChangeListener(new BleService.OnBleDevListChangeListener() {
                @Override
                public void onNewBleDev(BleDevContext bleDevContext) {
                    //发现新设备
                }

                @Override
                public void onUpdateBleDev(BleDevContext bleDevContext) {
                    //设备更新
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sp_action = (Spinner)findViewById(R.id.sp_action);
        et_num = (EditText)findViewById(R.id.et_num);
        et_password = (EditText)findViewById(R.id.et_password);
        et_outputTime = (EditText)findViewById(R.id.et_outputTime);
        lsv_door = (ListView)findViewById(R.id.listView);
        tv_result = (TextView)findViewById(R.id.tv_result);
        bluetooth_back = (TextView)findViewById(R.id.bluetooth_back);
        data_list = new ArrayList<String>();
        data_list.add("开门");
        data_list.add("编号");
        data_list.add("设置");
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spannable sp = et_password.getText();
        sp.setSpan(new ForegroundColorSpan(Color.RED), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp_action.setAdapter(arr_adapter);
        sp_action.setSelection(0, true);
        et_password.setEnabled(false);

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                Spannable sp = et_password.getText();
                sp.setSpan(new ForegroundColorSpan(Color.RED), 0, len <= 16 ? len : 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        bluetooth_back.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sp_action.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                       if (id == (data_list.size() - 1)){
                           et_password.setEnabled(true);//设置密码
                       }else{
                           et_password.setEnabled(false);
                       }

                       if (id == 1){
                           et_num.setVisibility(View.VISIBLE);
                       }else{
                           et_num.setVisibility(View.GONE);
                       }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        adapter = new ArrayAdapter<String>(this,R.layout.lsv_door_item);
        lsv_door.setAdapter(adapter);
        lsv_door.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_action.getSelectedItemId() == 0) {
//                    if (0 == rfBleKey.openDoor(stringToBytes(adapter.getItem(i).substring(0,18))
//                            , Integer.decode(et_outputTime.getText().toString())
//                            , et_password.getText().toString())) {
//                        tv_result.setText(R.string.openning);
//                    }
                    if (0 == rfBleKey.openDoor(stringToBytes(adapter.getItem(i).substring(0,18))
                            , Integer.decode(et_outputTime.getText().toString())
                            , et_password.getText().toString())) {
                        tv_result.setText(R.string.openning);
                    }
                }else if (sp_action.getSelectedItemId() == 1){
                    if (0 == rfBleKey.ctrlTK(stringToBytes(adapter.getItem(i).substring(0,18)),"15212345678"
                            ,et_password.getText().toString(), Integer.parseInt(et_num.getText().toString()))){
                        tv_result.setText(R.string.release);
                    }
                }else {
                    if (0==rfBleKey.setDevPassword(stringToBytes(adapter.getItem(i).substring(0,18))
                            ,et_password.getText().toString())){
                        tv_result.setText(R.string.setting);
                    }else{
                        tv_result.setText(R.string.setting_error);
                    }
                }
            }
        });
        btn_refreshDevList = (Button)findViewById(R.id.btn_refresh);
        btn_refreshDevList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rfBleKey!=null){
                    //Scan dev list
                    ArrayList<BleDevContext> lst = rfBleKey.getDiscoveredDevices();
                    adapter.clear();
                    for (BleDevContext dev:lst){
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(bytesToString(dev.mac))
                        .append(" (").append(dev.rssi).append(")");
                        adapter.add(stringBuffer.toString().toUpperCase());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Intent bindIntent = new Intent(getApplicationContext(), BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        //搜索白名单 mac
        mWhiteList = (new ArrayList<byte[]>() {{
            add(new byte[]{0x36, 0x38, 0x37, 0x74, 0x47, 0x63, 0x72, 0x4E, 0x35});
            add(new byte[]{0x36, 0x4F, 0x70, 0x6A, 0x73, 0x54, 0x42, 0x41, 0x41});
            add(new byte[]{0x30, 0x56, 0x4C, 0x56, 0x7A, 0x34, 0x34, 0x58, 0x50});
            add(new byte[]{0x31, 0x6C, 0x66, 0x64, 0x73, 0x36, 0x73, 0x38, 0x73});
            add(new byte[]{0x30, 0x68, 0x75, 0x72, 0x55, 0x6D, 0x53, 0x72, 0x53});
            add(new byte[]{0x01, 0x11, 0x68, (byte) 0x93, (byte) 0xC4, 0x27, 0x5B, (byte) 0xC2, 0x6C});
        }});
    }

    @Override
    protected void onDestroy(){
        rfBleKey.free();
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    public static String bytesToString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString().toUpperCase();
    }

    public static byte[] stringToBytes(String outStr){
        if (outStr.length()!=18)
            return null;
        int len = outStr.length()/2;
        byte[] mac = new byte[len];
        for (int i = 0; i < len; i++){
            String s = outStr.substring(i*2,i*2+2);
            if (Integer.valueOf(s, 16)>0x7F) {
                mac[i] = (byte)(Integer.valueOf(s, 16) - 0xFF - 1);
            }else {
                mac[i] = Byte.valueOf(s, 16);
            }
        }
        return mac;
    }
}
