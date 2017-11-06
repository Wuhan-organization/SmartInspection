package tw.com.a_i_t.IPCamViewer.DroneCam;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.videolan.libvlc.LibVLC;

import tw.com.a_i_t.IPCamViewer.R;
import tw.com.a_i_t.IPCamViewer.IPCam.IPCamWhistlerEvent;
import tw.com.a_i_t.IPCamViewer.IPCam.IPCamWhistlerFileDwonload;
import tw.com.a_i_t.IPCamViewer.IPCam.IPCamWhistlerReceiver;
import tw.com.a_i_t.IPCamViewer.IPDiscover.IPCamIpDiscover;
import tw.com.a_i_t.IPCamViewer.VideoView.VideoViewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DroneCamViewer extends Fragment {
	private String TAG="DroneCamViewer";

	private FrameLayout mSurfaceFrame ;
	private String mMediaUrl = "rtsp://192.168.1.1/720P_1M";//rtsp://192.168.1.1:8880/channel1";//"rtsp://192.72.1.1/liveRTSP/av4" ;
	//rtsp://192.168.1.1:8880/channel1		//DroneCam default IP

	////for new vlc
	private VideoViewer mVideoView ;
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.e(TAG, "Handler= " + msg.arg1);

				switch (msg.arg1) {
				}
			}
		};
		super.onCreate(savedInstanceState) ;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dronecam_view, container, false) ;

		mVideoView = (VideoViewer) view.findViewById(R.id.dronecam_surface) ;
		mSurfaceFrame = (FrameLayout) view.findViewById(R.id.dronecam_surface_frame) ;
		mVideoView.setHandler(handler);
		RSSI_VIEW = (TextView) view.findViewById(R.id.rssi_text);
		RSSI_VIEW.setTextColor(android.graphics.Color.YELLOW);
		RSSI_VIEW.setText("RSSI:" + total_rssi_value);
		QOS_button = (Button) view.findViewById(R.id.qos_button);
		QOS_button.setOnClickListener(QOS_BUTTON);
		if(qos_enable == 1)
		{
			QOS_button.setText("Disable QOS");
		}
		return view;
	}


	public void play(boolean isRTSP) {
		Activity activity = getActivity() ;
		final boolean rtsp = isRTSP;
		if (activity != null) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					mVideoView.createPlayer(mMediaUrl,rtsp,200);
					//stopStreamButton.setEnabled(isPlaying);
				}
			}, 0) ;
		}
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		rate_ctrl_enable = 1;
		rssi_thread = new Thread(rssi_poll);
		rssi_thread.start();
		play(true);
		super.onResume() ;
	}
	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		rate_ctrl_enable = 0;
		rssi_thread.interrupt();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mVideoView.releasePlayer();
		super.onPause() ;

	}


	//////QOS
	private Thread rssi_thread;
	private int rate_ctrl_enable;
	private int total_rssi_value;
	private int total_link_speed;
	private int qos_enable = 1;
	private WifiManager wifi;
	private WifiInfo info;
	private Socket rate_socket;
	private OutputStream rate_out;
	private TextView RSSI_VIEW;
	private Button QOS_button;

	private void QOS_enable(int enable)
	{
		try {
			byte[] sendstr = new byte[2];
			rate_socket=new Socket("192.168.1.1", 10505);
			OutputStream rate_out=rate_socket.getOutputStream();
			sendstr[0] = (byte) (3 + enable);
			sendstr[1] = 0;
			rate_out.write(sendstr);
			rate_socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void send_cmd(int cmd)
	{
		try {
			byte[] sendstr = new byte[8];
			rate_socket=new Socket("192.168.1.1", 10505);
			OutputStream rate_out=rate_socket.getOutputStream();
			if(cmd == 0)
			{
				sendstr[0] = 0;
				sendstr[1] = 0;
				sendstr[2] = 4;
				sendstr[3] = 0;
				sendstr[4] = (byte) (total_rssi_value & 0xFF);
				sendstr[5] = (byte) ((total_rssi_value >> 8) & 0xFF);
				sendstr[6] = (byte) ((total_rssi_value >> 16) & 0xFF);
				sendstr[7] = (byte) ((total_rssi_value >> 24) & 0xFF);
				rate_out.write(sendstr);
			}else if (cmd == 0x3){
				sendstr[0] = 3;
				sendstr[1] = 0;
			}else if (cmd == 0x4){
				sendstr[0] = 4;
				sendstr[1] = 0;
			}
			rate_socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// create click listener
	View.OnClickListener QOS_BUTTON = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Button qos = (Button)v;
			String text = qos.getText().toString();
			if(text.equals("Disable QOS"))
			{
				qos_enable = 0x10;
				qos.setText("Enable QOS");
			}else if(text.equals("Enable QOS")){
				qos_enable = 0x11;
				qos.setText("Disable QOS");
			}
		}
	};

	private void update_rssi_value()
	{
		int strength = 0;
		int speed = 0, raw_rssi=0;

		wifi = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
		info = wifi.getConnectionInfo();
		if (info.getBSSID() != null) {
			strength = WifiManager.calculateSignalLevel(info.getRssi(), 100);
			speed = info.getLinkSpeed();
			raw_rssi = info.getRssi();
			total_rssi_value += Math.abs(raw_rssi);;
			total_link_speed += speed;
			Log.d(TAG,"total_rssi_value = " + total_rssi_value);
			Log.d(TAG,"total_link_speed = " + total_link_speed);
		}
	}

	private Runnable rssi_poll = new Runnable()
	{
		public void run()
		{
			while (rate_ctrl_enable == 1)
			{
				int i=0;
				try {
					for(i=0;i<4;i++)
					{
						update_rssi_value();
						Thread.sleep(250);
					}
					total_rssi_value = total_rssi_value / 4;
					total_link_speed = total_link_speed / 4;
					Log.d(TAG,"Send value = " + total_rssi_value);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							RSSI_VIEW.setText("RSSI:" + total_rssi_value + ",Link_SPEED:" + total_link_speed);
						}
					});
					if((qos_enable & 0x10) == 0x10)
					{
						send_cmd((qos_enable & 0x01) + 3);
						qos_enable = qos_enable & 0x01;
					}
					if (qos_enable == 1)
					{
						send_cmd(0);
					}
					total_rssi_value = 0;
					total_link_speed = 0;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	} ;

}
