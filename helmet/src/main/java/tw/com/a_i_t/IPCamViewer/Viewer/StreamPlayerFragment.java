package tw.com.a_i_t.IPCamViewer.Viewer ;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import tw.com.a_i_t.IPCamViewer.AdasToastView;
import tw.com.a_i_t.IPCamViewer.CameraCommand;
import tw.com.a_i_t.IPCamViewer.CameraPeeker;
import tw.com.a_i_t.IPCamViewer.CameraSniffer;
import tw.com.a_i_t.IPCamViewer.FunctionListFragment;
import tw.com.a_i_t.IPCamViewer.HelmetActivity;
import tw.com.a_i_t.IPCamViewer.R;
import tw.com.a_i_t.IPCamViewer.VideoView.VideoViewer;
import tw.com.a_i_t.IPCamViewer.Viewer.MediaUrlDialog.MediaUrlDialogHandler;

public class StreamPlayerFragment extends Fragment {

	public final static String TAG = "VLC/VideoPlayerActivity" ;

	private VideoViewer mVideoView ;
	private FrameLayout mSurfaceFrame ;
	private TextView curdate;
	public Button cameraRecordButton;
	public Button cameraSnapshotButton;
	public Button findCameraButton;
	public Button cameraSwitchButton;
	public Button cameraDownloadButton;

	public Button adasCalibrateButton;
	private static Date mCameraTime ;
	private static long mCameraUptimeMills ;
	private String mTime;
	public static String mRecordStatus="";
	public static String mSensorMode="";
	public static String mUIMode="";
	private boolean mRecordthread = false;
	private boolean mViewEntry = false;
	private static int mCameraId = 0;

	/**
	 * For uninterrupted switching between audio and video mode
	 */
	private boolean mEndReached ;

	private boolean mPlaying ;
	ProgressDialog mProgressDialog ;

	private String mMediaUrl ;

	private static final String KEY_MEDIA_URL = "mediaUrl" ;
	private TimeThread timestampthread;
	private StreamLogThread streamlogthread;
	private Thread streamon;

	///ADAS calibration
	private static final int	ADAS_RECORD_NOCHECK   = 0;
	private static final int	ADAS_RECORD_CHECK   = 1;
	private ImageView adasCalifront;
	private ImageView adasCaliback;
	RelativeLayout calibratelayout;
	RelativeLayout calibratebutton;
	LinearLayout streamingLayout;
	private boolean AdascliDone = false;
	private int ad_h;
	private int ad_y1;
	private int ad_y2;
	private int ad_roiy1;
	private int ad_roiy2;

	class MyPeeker extends CameraPeeker{
		StreamPlayerFragment theFrag;
		MyPeeker(StreamPlayerFragment frag) {
			theFrag = frag;
		}
		public void Update(String s) {
			theFrag.Reception(s);
		}
	}
	public StreamPlayerFragment() {
		FunctionListFragment.setMenuHandler(sfHandler);
		//CameraSniffer sniffer = FunctionListFragment.GetCameraSniffer();
		//MyPeeker peeker = new MyPeeker(this);
		//sniffer.SetPeeker(peeker,getActivity());
	}

	public static StreamPlayerFragment newInstance(String mediaUrl) {
		StreamPlayerFragment fragment = new StreamPlayerFragment() ;

		Bundle args = new Bundle() ;
		args.putString(KEY_MEDIA_URL, mediaUrl) ;
		fragment.setArguments(args) ;

		return fragment ;
	}

	public String checkTime(int i)
	{
		mTime = Integer.toString(i);
		if (i<10) 
		{
			mTime = "0" + mTime;
		}
		return mTime;
	}

	//yining
	private class GetTimeStamp extends AsyncTask<URL, Integer, String> {

		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandTimeStampUrl() ;
			if (url != null) {	
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			Log.d(TAG, "TimeStamp property "+result) ;
			if (result != null && result.contains("OK")) {
				String[] lines;		
				String[] lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.year=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int year = Integer.valueOf(lines[0]);
				lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.month=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int month = Integer.valueOf(lines[0]); 
				lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.day=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int day = Integer.valueOf(lines[0]); 
				lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.hour=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int hour= Integer.valueOf(lines[0]); 
				lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.minute=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int minute = Integer.valueOf(lines[0]); 
				lines_temp = result.split("Camera.Preview.MJPEG.TimeStamp.second=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				int second = Integer.valueOf(lines[0]); 

				SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);  
				try {
					String cameraUptimeStr = String.format("%04d-%02d-%02d %02d:%02d:%02d", 
							year, month, day, hour, minute, second) ;
					mCameraTime = format.parse(cameraUptimeStr);  
					Log.i("GetTimeStamp", cameraUptimeStr);  
				} catch (ParseException e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				}
				mCameraUptimeMills = SystemClock.uptimeMillis() ;

			}else if (activity != null) {
				Toast.makeText(activity, activity.getResources().getString(R.string.message_fail_get_info),
						Toast.LENGTH_LONG).show() ;			
			}
			setWaitingState(false) ;
			setInputEnabled(true) ;
			super.onPostExecute(result) ;
			new GetRecordStatus().execute();
		}
	}

	private class GetRecordStatus extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandCameraStautsUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			//Log.d(TAG, "TimeStamp property "+result) ;
			if (result != null && result.contains("OK")) {
				String[] lines;
				// Check Video Status - Recording or Standby (defined in FW)
				String[] lines_temp = result.split("Camera.Preview.MJPEG.status.record=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				UpdateVideoButtonStatus(lines[0]);
				// Check Camera Mode - Videomode or NotVideomode (defined in FW)
				lines_temp = result.split("Camera.Preview.MJPEG.status.mode=");
				lines = lines_temp[1].split(System.getProperty("line.separator")) ;
				mSensorMode = lines[0];
				if (mSensorMode.equals("Videomode"))
					mUIMode = "VIDEO";
				if (!IsCameraInPreviewMode()) {
					mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_WRONG));
					if (mProgressDialog != null && mProgressDialog.isShowing()) {
						mProgressDialog.dismiss() ;
						mProgressDialog = null ;
					}
				}
				// TODO: Check current ui mode before live view
			}
			else if (activity != null) {
				Toast.makeText(activity, activity.getResources().getString(R.string.message_fail_get_info),
				Toast.LENGTH_LONG).show() ;
			}
			setWaitingState(false) ;
			setInputEnabled(true) ;
			if (activity != null)
				new GetCameraCamid().execute();
			super.onPostExecute(result) ;
		}
	}	

	private class CameraVideoRecord extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandCameraRecordUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			Log.d(TAG, "Video record response:"+result) ;
			if (result != null && result.contains("OK")) {
				if (activity != null) {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show();
					// Command is successful, current status is Standby then change to Recording
					if (IsVideoRecording())
						UpdateVideoButtonStatus("Recording");
					else
						UpdateVideoButtonStatus("Standby");
				}
			}
			else if (activity != null) {
				if (result != null && result.contains("718")) {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.label_sd_error),
							Toast.LENGTH_SHORT).show() ;
				}else
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_failed),
							Toast.LENGTH_SHORT).show() ;
			}
			setWaitingState(false) ;
			cameraRecordButton.setEnabled(true);
			setInputEnabled(true) ;
			if (activity != null) 
				new GetRecordStatus().execute();
			super.onPostExecute(result) ;
		}
	}	

	private class CameraSnapShot extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandCameraSnapshotUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			Log.d(TAG, "snapshot response:"+result) ;	
			if (result != null && result.contains("OK")) {
				if (activity != null)
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show() ;
			}
			else if (activity != null) {

				if (result != null && result.contains("718")) {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.label_sd_error),
							Toast.LENGTH_SHORT).show() ;
				}else
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_failed),
							Toast.LENGTH_SHORT).show() ;
			}
			setWaitingState(false) ;
			cameraSnapshotButton.setEnabled(true);
			setInputEnabled(true) ;
			super.onPostExecute(result) ;
		}
	}		

	private class CameraIdSwitch extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cameraSwitchButton = (Button) getView().findViewById(R.id.cameraSwitchButton) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = null;
			if (mCameraId == 1)
				url = CameraCommand.commandCameraSwitchtoFrontUrl() ;
			else if (mCameraId == 0)
				url = CameraCommand.commandCameraSwitchtoRearUrl() ;

			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			//Log.i(TAG, "CameraIdSwitch:"+result) ;
			String err = result.substring(0, 3);
			if (result != null && err.equals("709") != true) {
				if (mCameraId == 1) {
					mCameraId = 0;
					cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
				}
				else if (mCameraId == 0) {
					mCameraId = 1;
					cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtorear));
				}
				if (activity != null) 
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show() ;
			}
			else if (activity != null) {

				Toast.makeText(activity,
						activity.getResources().getString(R.string.message_command_failed),
						Toast.LENGTH_SHORT).show() ;	
			}
			play(300);
			setWaitingState(false) ;
			cameraSwitchButton.setEnabled(true);
			setInputEnabled(true) ;
			super.onPostExecute(result) ;
		}
	}		

	private class GetCameraCamid extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandCameraGetcamidUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			if (result != null) {
				String err=result.substring(0, 3);
				if (err.equals("703")){
					mCameraId = 0;
					cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
				}
				else if (result.contains("OK")){
					String[] lines_temp = result.split("Camera.Preview.Source.1.Camid=");
					cameraSwitchButton = (Button) getView().findViewById(R.id.cameraSwitchButton) ;
					
					if ((lines_temp[1] != null) && (lines_temp[0]!=null)&&(activity != null)) {
						if (lines_temp[1].equals("front\n")) {
							mCameraId = 0;
							cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
						} else if (lines_temp[1].equals("rear\n")) {
							mCameraId = 1;
							cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtorear));
						}else {
							mCameraId = 0;
							cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
						}
					}else {
						mCameraId = 0;
						cameraSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
					}
				}

				// TODO: Check current ui mode before live view
				//mCameraStatusHandler.sendMessage(mCameraStatusHandler.obtainMessage());
			}
			else if (activity != null) {
				Toast.makeText(activity, activity.getResources().getString(R.string.message_fail_get_info),
				Toast.LENGTH_LONG).show() ;
			}
			setWaitingState(false) ;
			setInputEnabled(true) ;
			if (activity != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new GetAdasEnable().execute(ADAS_RECORD_NOCHECK);
			}
			super.onPostExecute(result) ;
		}
	}

	////////one click sharing
	private class CamerashortRecord extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandCameraQuickDownloadUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			Log.d(TAG, "snapshot response:"+result) ;
			if (result != null && result.contains("OK")) {
				if (activity != null)
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show() ;
			}
			else if (activity != null) {
				if (result != null && result.contains("722")) {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.label_record_need),
							Toast.LENGTH_SHORT).show() ;
				}else
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_failed),
							Toast.LENGTH_SHORT).show() ;
			}
			setWaitingState(false) ;
			cameraSnapshotButton.setEnabled(true);
			setInputEnabled(true) ;
			super.onPostExecute(result) ;
		}
	}

	private List<View> mViewList = new LinkedList<View>() ;
	private void setInputEnabled(boolean enabled) {
		for (View view : mViewList) {
			view.setEnabled(enabled) ;
		}
	}
	private boolean mWaitingState = false ;
	private boolean mWaitingVisible = false ;
	private void setWaitingState(boolean waiting) {
		if (mWaitingState != waiting) {
			mWaitingState = waiting ;
			setWaitingIndicator(mWaitingState, mWaitingVisible) ;
		}
	}
	private void setWaitingIndicator(boolean waiting, boolean visible) {
		if (!visible)
			return ;
		setInputEnabled(!waiting) ;
		Activity activity = getActivity() ;
		if (activity != null) {
			activity.setProgressBarIndeterminate(true) ;
			activity.setProgressBarIndeterminateVisibility(waiting) ;
		}
	}
	private void clearWaitingIndicator() {
		mWaitingVisible = false ;
		setWaitingIndicator(false, true) ;
	}
	private void restoreWaitingIndicator() {
		mWaitingVisible = true ;
		setWaitingIndicator(mWaitingState, true) ;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;
		//((HelmetActivity) getActivity()).sp = this;
		if (savedInstanceState == null) {
			mRecordthread = true;
			mViewEntry = true;// for get timestamp
			/* query camera time in camera and to show on preview window */
			//new GetTimeStamp().execute();
		}
		mMediaUrl = getArguments().getString(KEY_MEDIA_URL) ;

		timestampthread = new TimeThread();
		timestampthread.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.preview_player, container, false) ;

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
		curdate = (TextView) view.findViewById(R.id.TimeStampLabel);
		mVideoView = (VideoViewer) view.findViewById(R.id.player_surface) ;

		mSurfaceFrame = (FrameLayout) view.findViewById(R.id.player_surface_frame) ;
		mVideoView.setHandler(mCameraStatusHandler);
		streamingLayout =  (LinearLayout) view.findViewById(R.id.streambuttonlayout) ;
		findCameraButton = (Button) view.findViewById(R.id.findCameraButton) ;
		findCameraButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cameraSnapshotButton.isEnabled()&&cameraSwitchButton.isEnabled()
						&&cameraRecordButton.isEnabled()) {
					URL url = CameraCommand.commandFindCameraUrl() ;
					if (url != null) {
						new CameraCommand.SendRequest().execute(url) ;
					}
				}
			}
		}) ;

		cameraRecordButton = (Button) view.findViewById(R.id.cameraRecordButton) ;
		cameraSnapshotButton = (Button) view.findViewById(R.id.cameraSnapshotButton) ;
		cameraSwitchButton = (Button) view.findViewById(R.id.cameraSwitchButton) ;

		cameraRecordButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cameraSnapshotButton.isEnabled()&&cameraSwitchButton.isEnabled()) {
					cameraRecordButton.setEnabled(false);
					new CameraVideoRecord().execute();
				}
			}
		}) ;

		cameraSnapshotButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cameraRecordButton.isEnabled()&&cameraSwitchButton.isEnabled()) {
					cameraSnapshotButton.setEnabled(false);
					new CameraSnapShot().execute();	
				}
			}
		}) ;

		cameraSwitchButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cameraRecordButton.isEnabled()&&cameraSnapshotButton.isEnabled()) {
					cameraSwitchButton.setEnabled(false);
					stop();//cch
					new CameraIdSwitch().execute();
				}
			}
		}) ;

		//label_dowload_event
		cameraDownloadButton = (Button) view.findViewById(R.id.cameraDownloadhButton) ;
		cameraDownloadButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cameraRecordButton.isEnabled()&&cameraSnapshotButton.isEnabled()) {

					new CamerashortRecord().execute();
				}
			}
		}) ;

		cameraRecordButton.setEnabled(true) ;
		cameraSnapshotButton.setEnabled(true) ;
		findCameraButton.setEnabled(true);
		cameraSwitchButton.setEnabled(true);

		getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC) ;
		if (savedInstanceState != null) {
			UpdateVideoButtonStatus(mRecordStatus);
		}

		adasCalifront = (ImageView) view.findViewById(R.id.adas_califront);
		adasCalifront.setOnTouchListener(AdasCalifListener);

		adasCaliback= (ImageView) view.findViewById(R.id.adas_caliback);
		adasCaliback.setOnTouchListener(AdasCalibListener);

		calibratelayout = (RelativeLayout) view.findViewById(R.id.adas_calibrateline) ;
		calibratelayout.setVisibility(View.INVISIBLE);

		calibratebutton = (RelativeLayout) view.findViewById(R.id.adas_calibratlayout) ;
		calibratebutton.setVisibility(View.INVISIBLE);

		adasCalibrateButton = (Button) view.findViewById(R.id.adas_calibration) ;
		adasCalibrateButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!AdascliDone) {
					AdascliDone = true;
					calibratelayout.setVisibility(View.VISIBLE);
					streamingLayout.setVisibility(View.INVISIBLE);
					adasCalibrateButton.setText(getResources().getString(R.string.AdasCalibratebuttonDone));
					new GetAdasEnable().execute(ADAS_RECORD_NOCHECK);

				}else {
					AdascliDone = false;

					adasWinToRoi();
					new SetAdasValue().execute();
					calibratelayout.setVisibility(View.INVISIBLE);
					streamingLayout.setVisibility(View.VISIBLE);
					adasCalibrateButton.setText(
							getResources().getString(R.string.AdasCalibratebutton));
				}
			}
		}) ;

		return view ;
	}

	@Override
	public void onPause() {
		stop() ;
		super.onPause() ;
	}

	@Override
	public void onStop() {
		super.onStop() ;
	}

	@Override
	public void onDestroy() {
		//((HelmetActivity) getActivity()).sp = null;
		//CameraSniffer sniffer = FunctionListFragment.GetCameraSniffer();
		//sniffer.SetPeeker(null);
		super.onDestroy() ;
		//
		if (streamlogthread != null)
			streamlogthread.stopPlay();
		timestampthread.stopPlay();
	}

	private void stop() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss() ;
			mProgressDialog = null ;
		}
		if (mPlaying == true) {
			mPlaying = false ;
			mVideoView.releasePlayer();
		}
	}

	public void play(int connectionDelay) {

		Activity activity = getActivity() ;
		if (activity != null) {
			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() { 
					if (mPlaying == false && IsCameraInPreviewMode())
					{
						mPlaying = true;
						mVideoView.createPlayer(mMediaUrl,true,600);//true for rtsp,false fo local play//max 250ms
						mEndReached = false ;
					}
				} 
			}, connectionDelay) ;
		}		
	}

	private void playLiveStream() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( HelmetActivity.getAppContext());
		int sl;

		play(100);//HelmetActivity.sConnectionDelay) ;
		sl = preferences.getInt("_stream_log", 0);
		streamlogthread = null;
		if (sl == 1) {
			streamlogthread = new StreamLogThread();
			streamlogthread.start();
		}

	}

	@Override
	public void onResume() {
		AdascliDone = false;
		
		mProgressDialog = new ProgressDialog(getActivity()) ;
		mProgressDialog.setTitle("Connecting to Camera") ;
		mProgressDialog.setCancelable(false) ;
		mProgressDialog.show() ;
		
		if (mViewEntry)
			new GetTimeStamp().execute();
		else
			new GetCameraCamid().execute();

		mViewEntry = false;	
		super.onResume() ;
	}

	private void endReached() {
		/* Exit player when reach the end */
		mEndReached = true ;
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss() ;
			mProgressDialog = null ;
		}
		getActivity().onBackPressed() ;
	}

	private void encounteredError() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss() ;
			mProgressDialog = null ;
		}
		if(IsCameraInPreviewMode())
		{
			new MediaUrlDialog(getActivity(), mMediaUrl, new MediaUrlDialogHandler() {

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			}).show() ;
		}
		cameraRecordButton.setEnabled(false);
		cameraSnapshotButton.setEnabled(false);
		findCameraButton.setEnabled(false);
		cameraSwitchButton.setEnabled(false);
		//		recordButton.setEnabled(false);
		//		snapshotButton.setEnabled(false);
	}

	private void handleVout(Message msg) {
		if (msg.getData().getInt("data") == 0 && mEndReached) {
			//stop() ;//chrison
			//playLiveStream() ;//chrison
		}
	}

	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg){
			long timeElapsed = SystemClock.uptimeMillis() - mCameraUptimeMills ;

			Date currentTime = new Date(mCameraTime.getTime() + timeElapsed) ;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.US) ;
			String currentTimeStr = sdf.format(currentTime);
			curdate.setText(currentTimeStr) ;
			super.handleMessage(msg);  
		}
	};

	// Message ID
	private static final int	MSG_VIDEO_RECORD = 1;
	private static final int	MSG_VIDEO_STOP   = 2;
	private static final int	MSG_MODE_CHANGE  = 3;
	private static final int	MSG_MODE_WRONG   = 4;
	private static final int	MSG_MODE_LDWS_LEFT   = 5;
	private static final int	MSG_MODE_LDWS_RIGHT   = 6;
	private static final int	MSG_MODE_FCWS_ALARM   = 7;
	private static final int	MSG_MODE_SAG_ALARM   = 8;
	private static final int	MSG_MODE_UPDATE_ADASROI_ON   = 9;
	private static final int	MSG_MODE_UPDATE_ADASROI_OFF   = 10;
	private static final int	MSG_MODE_RTSP_PLAYING   = 11;

	public Handler mCameraStatusHandler = new Handler() {
		public void handleMessage(Message msg) {
			AdasToastView adasarm;
			int		msgid = msg.getData().getInt("msg");
			switch (msgid) {
			case MSG_VIDEO_RECORD:
				cameraRecordButton.setText(getActivity().getResources().getString(R.string.label_camera_stop_record));
				break;
			case MSG_VIDEO_STOP:
				cameraRecordButton.setText(getActivity().getResources().getString(R.string.label_camera_record));
				break;
			case MSG_MODE_WRONG:
			case MSG_MODE_CHANGE:
				String	info = (msgid == MSG_MODE_WRONG)? "Camera is not at Video mode, please exit live view" :
					"Camera mode was changed, please exit live view";
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create() ;
				alertDialog.setTitle("Mode Error!");
				alertDialog.setMessage(info);	
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
						"OK",			
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {					
						dialog.dismiss() ;
					}									
				}) ;
				alertDialog.show() ;	
				break;
			case MSG_MODE_LDWS_LEFT:
				//Log.i("StreamPlayFragment", "===Get LDWS State MSG_MODE_LDWS_LEFT");
				adasarm = new AdasToastView(getActivity());	
				adasarm.setGravity(Gravity.LEFT | Gravity.BOTTOM , 10, 50);
				adasarm.setAdasIcon(R.drawable.adas_left);
				adasarm.show();
				break;
			case MSG_MODE_LDWS_RIGHT:
				//Log.i("StreamPlayFragment", "===Get LDWS State MSG_MODE_LDWS_RIGHT");
				adasarm = new AdasToastView(getActivity());	
				adasarm.setGravity(Gravity.LEFT | Gravity.BOTTOM , 10, 50);
				adasarm.setAdasIcon(R.drawable.adas_right);
				adasarm.show();
				break;
			case MSG_MODE_FCWS_ALARM:
				//Log.i("StreamPlayFragment", "===Get ADAS State MSG_MODE_FCWS_ALARM");
				adasarm = new AdasToastView(getActivity());	
				adasarm.setGravity(Gravity.LEFT | Gravity.BOTTOM , 10, 50);
				adasarm.setAdasIcon(R.drawable.adas_fcws);
				adasarm.show();
				break;
			case MSG_MODE_SAG_ALARM:
				//Log.i("StreamPlayFragment", "===Get ADAS State MSG_MODE_SAG_ALARM");
				adasarm = new AdasToastView(getActivity());	
				adasarm.setGravity(Gravity.LEFT | Gravity.BOTTOM , 10, 50);
				adasarm.setAdasIcon(R.drawable.adas_sag);
				adasarm.show();
				break;	
			case MSG_MODE_UPDATE_ADASROI_ON:
				adasRoiToWin();
				calibratebutton.setVisibility(View.VISIBLE);
				break;
			case MSG_MODE_UPDATE_ADASROI_OFF:
				calibratebutton.setVisibility(View.INVISIBLE);
				break;
			case MSG_MODE_RTSP_PLAYING:
				if ((mProgressDialog != null)&&(mProgressDialog.isShowing()))
					mProgressDialog.dismiss();
				//new GetCameraCamid().execute();
				break;
			}
			switch (msg.arg1) {
				case VideoViewer.MSG_VIDEOVIEWER_NEWLAYOUT:
					ViewGroup.LayoutParams lp;
					lp = mSurfaceFrame.getLayoutParams() ;
					lp.width = mVideoView.layoutW ;
					lp.height = mVideoView.layoutH-24 ;
					mSurfaceFrame.setLayoutParams(lp) ;
					mSurfaceFrame.invalidate() ;
					break;
				case VideoViewer.MSG_VIDEOVIEWER_START:
					Log.e(TAG,"MSG_VIDEOVIEWER_START");
					break;
			}

			super.handleMessage(msg);  
		}
	};

	private class TimeThread extends Thread {
		boolean timerPlaying=true;
		int dialogcnt=0;
		FileWriter fw = null;
		BufferedWriter bw;
		public void run() {
			while(timerPlaying)
			{	
				//sometimes can't get vlc playing callback, close mProgressDialog by timer
				if ((mProgressDialog != null)&&(mProgressDialog.isShowing())&&mPlaying)
					mProgressDialog.dismiss();
				else if ((mProgressDialog != null)&&(mProgressDialog.isShowing())&& dialogcnt > 5) {
					dialogcnt = 0;
					mProgressDialog.dismiss();
				}
				dialogcnt ++;	
				try{				
					Thread.sleep(1000);
				} catch (Exception e){
					e.printStackTrace();
				}
				if (mCameraTime == null) {
					continue ;
				}
				mTimeHandler.sendMessage(mTimeHandler.obtainMessage());			
			}
		}
		public void stopPlay(){
			timerPlaying=false;
		}
	};

	//
	private class StreamLogThread extends Thread {
		boolean streamPlaying=true;
		FileWriter fw = null;
		BufferedWriter bw;
		public void run() {
			Log.e(TAG,"TimeThread start");
			try{
				try {
					fw = new FileWriter(HelmetActivity.getAppDir()+"/output.txt", false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				bw = new BufferedWriter(fw);
				bw.write("Hello, Sih! Hello, Android!");
				bw.newLine();
				//bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			while(streamPlaying)
			{
				try{
					Thread.sleep(33);
				} catch (Exception e){
					e.printStackTrace();
				}
				//Log.e(TAG,"lost="+mVideoView.getLostP());
				try {
					bw.write("time:"+SystemClock.uptimeMillis() + ",   lost frame:" + mVideoView.getLostP());
					bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void stopPlay(){
			streamPlaying=false;
		}
	};

	public boolean IsVideoRecording()
	{
		return mRecordStatus.equals("Recording");
	}

	public boolean IsCameraInPreviewMode()
	{
		return mSensorMode.equals("Videomode") ||
				mSensorMode.equals("VIDEO")     ||
				mSensorMode.equals("Capturemode") ||
				mSensorMode.equals("CAMERA") ||
				mSensorMode.equals("BURST")  ||
				mSensorMode.equals("TIMELAPSE");
	}
	//	public void SetRecordStatus(String Status)
	//	{
	//		mRecordStatus = Status;
	//	}

	private Message buildMessage(int msgid) {
		Message msgObj = mCameraStatusHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putInt("msg", msgid);
		msgObj.setData(b);
		return msgObj;
	}
/*
	private Message buildVideoPlayerEventMessage(int msgid) {
		Message msgObj = eventHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putInt("event", msgid);
		b.putInt("data", 0);
		msgObj.setData(b);
		return msgObj;
	}
*/
	public void Reception(String s) {
		String val;
		// Monitor UImode
		val = CameraSniffer.GetUIMode(s);
		if (val != null) {
			if (!val.equals(mUIMode)) {
				if (val.equals("CAMERA") ||
						val.equalsIgnoreCase("BURST") ||
						val.equalsIgnoreCase("TIMELAPSE") ||
						val.equalsIgnoreCase("VIDEO")) {
					mSensorMode = "Videomode"; // is always Videomode
					Log.i("RESET", "=MODE Changed " + val);
					// Sensor mode changed! Restart streaming...
					endReached();
//					eventHandler.sendMessage(
//							buildVideoPlayerEventMessage(EventHandler.MediaPlayerVout));
				} else {
					// Stop Streaming
					mSensorMode = "NotVideomode";
					stop();
					mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_CHANGE));
				}
				mUIMode = val;
			}
		}
		// Monitor Video Record
		val = CameraSniffer.GetRecording(s);
		if (val != null) {
			Log.i("STREAM", "===Get Record status " + val + " Recording " + mRecordStatus);
			if (val.equals("YES") && !IsVideoRecording()) {
				UpdateVideoButtonStatus("Recording");
			} else
				if (val.equals("NO") && IsVideoRecording()) {
					UpdateVideoButtonStatus("Standby");
				}
		}

		val = CameraSniffer.GetIpAddr(s);
		if (val != null) {
			Log.i("StreamPlayFragment", "===Get device IP Address " + val);
		}
		//LDWS
		val = CameraSniffer.GetLdwsState(s);
		if (val != null) {
			if (val.equals("LEFT")) {
				mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_LDWS_LEFT));
			}else if (val.equals("RIGHT")) {
				mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_LDWS_RIGHT));
			}
		}
		//FCWS
		val = CameraSniffer.GetFcwsState(s);
		if (val != null) {
			if (val.equals("ALARM"))
				mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_FCWS_ALARM));
		}
		//SAG
		val = CameraSniffer.GetSagState(s);
		if (val != null) {
			if (val.equals("ALARM"))
				mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_SAG_ALARM));
		}
	}
	public void UpdateVideoButtonStatus(String s) {
		int		status;
		if(s.equals("Recording")) {
			status = MSG_VIDEO_RECORD;
			mRecordStatus = "Recording";
		} else {
			status = MSG_VIDEO_STOP;
			mRecordStatus = "Standby";
		}
		mCameraStatusHandler.sendMessage(buildMessage(status));
	}

	////Check ADAS Enable
	private class GetAdasEnable extends AsyncTask<Integer, Integer, String> {
		boolean recCheck = false;
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(Integer... params) {
			if (params[0] == ADAS_RECORD_CHECK)
				recCheck = true;
			URL url = CameraCommand.commandAdasEnableUrl() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			if ((result != null)&&result.contains("Enable=1")) {
				String[] val=result.split("Height=");
				
				if (result.contains("Height=") && (val[1] != null) /*&& val[1].contains("set")*/) {
					String height[] = val[1].split("\n");
					ad_h =  Integer.parseInt(height[0]);
					Log.d("ad_h","ad_h=" + ad_h);
					
					val=result.split("Yone=");
					String y1[] = val[1].split("\n");
					ad_y1 = Integer.valueOf(y1[0]);
					Log.d("ad_y1","ad_y1=" + ad_y1);
					
					val=result.split("Ytwo=");
					String y2[] = val[1].split("\n");
					ad_y2 = Integer.valueOf(y2[0]);
					Log.d("ad_y2","ad_y2=" + ad_y2);

					mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_UPDATE_ADASROI_ON));
				}else {
					mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_UPDATE_ADASROI_OFF));
				}
				// TODO: Check current ui mode before live view
			}
			else if (activity != null) {
				mCameraStatusHandler.sendMessage(buildMessage(MSG_MODE_UPDATE_ADASROI_OFF));
			}
			setWaitingState(false) ;
			setInputEnabled(true) ;
			
			if ((activity != null)&&(mProgressDialog != null)&&(mProgressDialog.isShowing())) {
				playLiveStream() ;
				/////chrison
				//mVideoView.createPlayer("");
			}
			super.onPostExecute(result) ;
			if (recCheck)
				new GetRecordStatus().execute();
		}
	}

	private class SetAdasValue extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandAdasRoiUrl(Integer.toString(ad_h),Integer.toString(ad_roiy1),
					Integer.toString(ad_roiy2),"set") ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			if (result != null) {
				if (!result.contains("703")) {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(activity,
							activity.getResources().getString(R.string.message_command_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
			else if (activity != null) {
				Toast.makeText(activity,
						activity.getResources().getString(R.string.message_command_failed),
						Toast.LENGTH_SHORT).show();
			}
			setWaitingState(false) ;
			setInputEnabled(true) ;
			super.onPostExecute(result) ;
		}
	}	

	private void adasWinToRoi()
	{
		float h = (float)mSurfaceFrame.getHeight()/(float)ad_h;
		float y1 = ((float)(adasCalifront.getTop()-mSurfaceFrame.getTop()+adasCalifront.getHeight()/2))/h;
		float y2 = ((float)(adasCaliback.getTop()-mSurfaceFrame.getTop()+adasCaliback.getHeight()/2))/h;

		ad_roiy1 = (int)y1;
		ad_roiy2 = (int)y2;
	}

	private void adasRoiToWin()
	{
		float h = (float)mSurfaceFrame.getHeight()/(float)ad_h;
		float y1 = ad_y1 * h;
		float y2 = ad_y2 * h;	

		int t = mSurfaceFrame.getTop()+(int)y1-adasCalifront.getHeight()/2;
		int r = (int)adasCalifront.getWidth();
		int b = mSurfaceFrame.getTop()+(int)y1+adasCalifront.getHeight()-adasCalifront.getHeight()/2;

		updateAdasfLayout(0,t,r,b );

		t = mSurfaceFrame.getTop()+(int)y2-adasCaliback.getHeight()/2;
		r = (int)adasCaliback.getWidth();
		b = mSurfaceFrame.getTop()+(int)y2+adasCaliback.getHeight()-adasCaliback.getHeight()/2;
		updateAdasbLayout(0,t,r,b );
	}

	private void updateAdasfLayout(int l,int t,int r,int b)
	{
		adasCalifront.layout(l,t,r,b);
	}
	private void updateAdasbLayout(int l,int t,int r,int b)
	{
		adasCaliback.layout(l,t,r,b);
	}

	private OnTouchListener AdasCalifListener = new OnTouchListener() {
		private int ny;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				ny = (int) (event.getY() + v.getTop() - v.getHeight() / 2);
				if (ny > (adasCaliback.getTop()-adasCaliback.getHeight()/2)
						|| (ny < mSurfaceFrame.getTop())) {
					break;
				}
				updateAdasfLayout(0, ny, (int)v.getWidth(), ny+v.getHeight());
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		}
	};

	private OnTouchListener AdasCalibListener = new OnTouchListener() {
		private int ny;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				ny = (int) (event.getY() + v.getTop() - v.getHeight() / 2);
				if (ny < (adasCalifront.getTop() + adasCalifront.getHeight()/2)
						|| (ny > (mSurfaceFrame.getBottom()-adasCalifront.getHeight()))) {
					break;
				}
				updateAdasbLayout(0, ny, (int)v.getWidth(), ny+v.getHeight());
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		}
	};

	//sniffer handler
	private Handler sfHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("Stream","Get sniffer message = " + (String)msg.obj);
			Reception((String)msg.obj);
		}
	};
}	// end of StreamPlayerFragment
