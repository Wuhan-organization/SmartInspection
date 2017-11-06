package tw.com.a_i_t.IPCamViewer.IPCam;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import tw.com.a_i_t.IPCamViewer.CameraCommand;
import tw.com.a_i_t.IPCamViewer.CameraSocket;
import tw.com.a_i_t.IPCamViewer.CameraUIConfig;
import tw.com.a_i_t.IPCamViewer.MainActivity;
import tw.com.a_i_t.IPCamViewer.R;
import tw.com.a_i_t.IPCamViewer.IPDiscover.IPCamIpDiscover;
import tw.com.a_i_t.IPCamViewer.VideoView.VideoViewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IPCamWhistlerViewer extends Fragment {
	String TAG = "IPCamWhistlerViewer";

	private static String cameraIp = null; 
	//	ProgressDialog mpWait;//=null;
	private VideoViewer mVideoView ;

	private String mMediaUrl ;
	private FrameLayout mWhistlerLayout;


	//UI
	private ImageButton stopStreamButton;
	private ImageButton powerOffButton;
	private ImageButton powerOnButton;

	private boolean isPlaying = false;
	private ImageView camLinkIcon;
	private TextView camLinkIp;
	private int textcnt = 0;
	private EditText camLinkIpEd;
	private Button camLinkIpSet;

	private boolean camIconIdx = false;
	private boolean camConnected = false;
	private int camListenCnt = 0;
	private IPCamIpDiscover ipGet;

	//TCP
	private CameraSocket camSocket = null;

	//Dialog for PIR
	private IPCamWhistlerPIRDialog builder;
	//Dialog for Ring
	private Dialog ringDialog;

	private FrameLayout mSurfaceFrame;

	private boolean pirShowing = false;
	private boolean pirEvnGet = false;
	private boolean pirDownload = false;

	private boolean rtspOn = false;

	IPCamWhistlerReceiver mWhistler;
	Handler handler;
	String mLocalFilename;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				//Log.e(TAG, "Handler= " + msg.arg1);
				switch (msg.arg1) {

				case IPCamWhistlerEvent.MSG_UPDATE_EVENT:
					final String urllink = (String)msg.obj;
					if (urllink.contains("http")) {
						Log.e(TAG, "HTTP download " + urllink);
						if ((urllink.contains("jpg"))||(urllink.contains("JPG"))||(urllink.contains("jpeg"))||(urllink.contains("JPEG"))) {

							if (pirShowing)
								break;

							IPCamWhistlerFileDwonload mdownload;
							mdownload = new IPCamWhistlerFileDwonload(getActivity(), handler);

							try {
								mdownload.execute(new URL(urllink));
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if ((ringDialog != null)&& ringDialog.isShowing()) {
								ringDialog.dismiss();
							}
							pirShowing = true;
							openPIRDialog();
						}else{
							//if ((builder != null) && builder.isShowing())
							//	builder.dismiss();
							if (pirDownload)
								break;
							pirDownload = true;
							setPIRVideoDownload();
							builder.byes.setOnClickListener(new Button.OnClickListener() {
								@Override
								public void onClick(View v) {
									builder.dismiss();
									IPCamWhistlerFileDwonload mdownload;
									mdownload = new IPCamWhistlerFileDwonload(getActivity(), handler);
									try {
										mdownload.execute(new URL(urllink));
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}) ;
							builder.bno.setOnClickListener(new Button.OnClickListener() {
								@Override
								public void onClick(View v) {
									builder.dismiss();
								}
							}) ;
						}


					}else if (urllink.contains("rtsp")) {
						Log.e(TAG, "RTSP streaming " + urllink);
						mMediaUrl = urllink;
						if ((ringDialog != null) && (ringDialog.isShowing()))
							ringDialog.dismiss();
						play(true);
						//parseIPandSave(urllink);
/*
						String[] ipaddr = null;
						String[] ipaddr_t = urllink.split("rtsp://");
						if (ipaddr_t[1] != null)
							ipaddr = ipaddr_t[1].split("/");

						if (ipaddr[0] != null) {
							Log.e("IPIPIPIPIPI", "IPIPIPIPIPIP= " + ipaddr[0]);
							CameraUIConfig.setIP(cameraIp);
							//if (ipGet != null) {
								camConnected = true;
							//	ipGet.stopDiscover();
							//}
							camLinkIcon.setImageResource(R.drawable.cam_connect);
							camListenCnt = 0;
							camLinkIp.setText(cameraIp);
							saveCamIp(cameraIp);
						}
*/
						//saveCamIp(cameraIp);


//						if(camSocket == null)
//							camSocket = new CameraSocket(handler);
//						cameraIp = CameraCommand.getCameraIp();
//						if (cameraIp != null)
//							camSocket.TcpConnectPort(ipaddr[0],554);

					}
					parseIPandSave(urllink);
					break;
				case IPCamWhistlerEvent.MSG_DOWNLOAD_FINISHED:
					mLocalFilename = (String)msg.obj;
					Log.e(TAG, "HTTP Download Finished !" + (String)msg.obj);

					if (mLocalFilename.contains("jpg")) {
						showImage(mLocalFilename);

					}else {
						//mVideoView.startLocalPlay(mLocalFilename);
						mMediaUrl = mLocalFilename ;
						play(false);
						//stopStreamButton.setEnabled(true);
						/*
						AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
						alertDialog.setTitle("Download Finished");
						alertDialog.setMessage("Do you want to open file?");

						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Log.e(TAG, "Open File1 =" + mLocalFilename);
								dialog.dismiss();
								IPCamWhistlerReceiver.resetEid();
								//Log.e(TAG, "Open File2 =" + mLocalFilename);
								OpenFile(mLocalFilename);
							}
						});

						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Log.e(TAG, "NO Open File1 =" + mLocalFilename);
								dialog.dismiss();
								IPCamWhistlerReceiver.resetEid();
								//Log.e(TAG, "No Open File2 =" + mLocalFilename);
							}
						});
						alertDialog.show();
						*/
					}
					break;
				case IPCamWhistlerEvent.MSG_DOWNLOAD_FAIL:

					if ((builder != null)&&builder.isShowing())
						builder.dismiss();
					mLocalFilename = (String)msg.obj;
					Log.e(TAG, "HTTP Download Failed !" + (String)msg.obj);
					AlertDialog alertFDialog = new AlertDialog.Builder(getActivity()).create() ;
					alertFDialog.setTitle("Download Failed") ;
					alertFDialog.setMessage("file ["+(String)msg.obj+"] not found") ;

					alertFDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss() ;
							IPCamWhistlerReceiver.resetEid();
						}
					}) ;
					alertFDialog.show() ;
					break;
				case IPCamWhistlerEvent.MSG_RECEIVE_IP:
					String urlip = (String)msg.obj;
					String[] ipaddr = urlip.split("ip=");
					if (ipaddr[1] != null) {
						Log.e(TAG, "Camera IP =" + ipaddr[1]);
						cameraIp = ipaddr[1];
						CameraUIConfig.setIP(cameraIp);
						saveCamIp(cameraIp);

						//if (ipGet != null) {
						//	camConnected = true;
						//	ipGet.stopDiscover();
						//}
						camLinkIcon.setImageResource(R.drawable.cam_connect);
						camListenCnt = 0;
						camLinkIp.setText(cameraIp);
						//diasble auto listen IP
						//ipGet.startDiscover(30000,1000);//30 secs delay,1 sec interval
					}
					break;
				case IPCamWhistlerEvent.MSG_BROADCAST_EVENT:
					if (!camConnected) {
						if (camIconIdx)
							camLinkIcon.setImageResource(R.drawable.cam_detect1);
						else
							camLinkIcon.setImageResource(R.drawable.cam_detect2);
						camIconIdx =!camIconIdx;
					}else {
						camListenCnt++;
						if (camListenCnt > 3) {
							camListenCnt = 0;
							camConnected = false;
						}
					}
					break;
				case IPCamWhistlerEvent.MSG_RUNRTSP_EVENT:
					if ((ringDialog != null) && (ringDialog.isShowing()))
						ringDialog.dismiss();
					play(true);
					break;
					
				case IPCamWhistlerEvent.MSG_CONNECTRTSP_FAILED:
					Log.e(TAG, "Handler= " + "MSG_CONNECTRTSP_FAILED");
					IPCamWhistlerReceiver.resetEid();
					Toast.makeText(MainActivity.getAppContext(),
							MainActivity.getAppContext().getResources().getString(R.string.label_Dronecam_lostrtsp),
							Toast.LENGTH_SHORT).show() ;
					break;
				case IPCamWhistlerEvent.MSG_WAKEUP_SENT:
					powerOnButton.setEnabled(true);
					break;
				case VideoViewer.MSG_VIDEOVIEWER_STOP:
					mVideoView.releasePlayer();
					isPlaying = false;
					stopStreamButton.setVisibility(View.INVISIBLE);
					mWhistlerLayout.setVisibility(View.INVISIBLE);

					break;
				case IPCamWhistlerEvent.MSG_RECEIVE_RING:
					final String urlring = (String)msg.obj;

					if (!rtspOn) {
						if (!mVideoView.isPlaying()) {
							if (ringDialog == null) {
								ringDialog = new Dialog(MainActivity.getAppContext());
								ringDialog.setContentView(R.layout.ringdialog);
								ringDialog.setTitle("Ring Ring !!!");
								//ringDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								//TextView text = (TextView) ringDialog.findViewById(R.id.ringtext);
								//text.setText("Ring..Ring...!!!");
								ImageView image = (ImageView) ringDialog.findViewById(R.id.ringimage);
								image.setImageResource(R.drawable.bell_icon);
							} else {
								ringDialog.setContentView(R.layout.ringdialog);
								ringDialog.setTitle("Ring Ring !!!");
								//ringDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								//TextView text = (TextView) ringDialog.findViewById(R.id.ringtext);
								//text.setText("Ring..Ring...!!!");
								ImageView image = (ImageView) ringDialog.findViewById(R.id.ringimage);
								image.setImageResource(R.drawable.bell_icon);
							}

							ringDialog.show();
						}
						rtspOn = true;
					}
					playBuzzer(true);
					parseIPandSave(urlring);
					break;
					case VideoViewer.MSG_VIDEOVIEWER_NEWLAYOUT:
						ViewGroup.LayoutParams lp;
						lp = mSurfaceFrame.getLayoutParams() ;
						lp.width = mVideoView.layoutW ;
						lp.height = mVideoView.layoutH-8 ;
						mSurfaceFrame.setLayoutParams(lp) ;
						mSurfaceFrame.invalidate() ;
					break;

					case IPCamWhistlerEvent.MSG_RECEIVE_PIR:
						final String urlpir = (String)msg.obj;
						if (!pirEvnGet) {
							if (ringDialog == null) {
								ringDialog = new Dialog(MainActivity.getAppContext());
								ringDialog.setContentView(R.layout.ringdialog);
								ringDialog.setTitle("Motion detected !!!");
								//ringDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								//TextView text = (TextView) ringDialog.findViewById(R.id.ringtext);
								//text.setText("Ring..Ring...!!!");
								ImageView image = (ImageView) ringDialog.findViewById(R.id.ringimage);
								image.setImageResource(R.drawable.pir_icon);
							}else {
								ringDialog.setContentView(R.layout.ringdialog);
								ringDialog.setTitle("Motion detected !!!");
								//ringDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								//TextView text = (TextView) ringDialog.findViewById(R.id.ringtext);
								//text.setText("Ring..Ring...!!!");
								ImageView image = (ImageView) ringDialog.findViewById(R.id.ringimage);
								image.setImageResource(R.drawable.pir_icon);
							}
							ringDialog.show();
							playBuzzer(false);
							pirEvnGet = true;
						}
						parseIPandSave(urlpir);
						break;
				}
				
			}
		};
//		LibVLC.restart(getActivity()) ;


		super.onCreate(savedInstanceState) ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.whistler_view, container, false) ;

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
		mVideoView = (VideoViewer) view.findViewById(R.id.player_surface) ;

		mSurfaceFrame = (FrameLayout) view.findViewById(R.id.whistler_surface_frame) ;

		mWhistlerLayout = (FrameLayout) view.findViewById(R.id.whistler_frame) ;
		mWhistlerLayout.setVisibility(View.INVISIBLE);//set to visible if get RTSP link
		
		stopStreamButton = (ImageButton) view.findViewById(R.id.ipcamstopstream) ;
		stopStreamButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mVideoView.isPlaying()) {
//					mLibVLC.stop();
					mVideoView.releasePlayer();
					IPCamWhistlerReceiver.resetEid();
					isPlaying = false;
					//stopStreamButton.setEnabled(isPlaying);
					mWhistlerLayout.setVisibility(View.INVISIBLE);
					stopStreamButton.setVisibility(View.INVISIBLE);

					new SendCGIPoweroff().execute();
				}
			}
		}) ;
//		if (!isPlaying)
		stopStreamButton.setVisibility(View.INVISIBLE);// .setEnabled(isPlaying);

		powerOffButton = (ImageButton) view.findViewById(R.id.ipcampoweroff) ;
		powerOffButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPlaying) {
//					mLibVLC.stop();
					mVideoView.releasePlayer();
					isPlaying = false;
					stopStreamButton.setVisibility(View.INVISIBLE);
				}
				
				powerOffButton.setEnabled(false);
				new SendCGIPoweroff().execute();
				//pirShowing = false;
				//pirEvnGet = false;
				//pirDownload = false;
				
				//URL url = CameraCommand.commandCamPowerOff();
				//if (url != null) {
				//	new CameraCommand.SendRequest().execute(url) ;
				//}
				//Log.e(TAG,"powerOFF = " + url);
			}
		}) ;

		powerOnButton = (ImageButton) view.findViewById(R.id.ipcampoweron) ;
		powerOnButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPlaying) {
					mVideoView.releasePlayer();
					isPlaying = false;
				}
				Log.e(TAG,"powerON" + cameraIp);
				if(camSocket == null)
					camSocket = new CameraSocket(handler);
				//cameraIp = CameraCommand.getCameraIp();
				if (cameraIp != null) {
					powerOnButton.setEnabled(false);
					camSocket.SendTcpWakeupData(cameraIp);
				}
			}
		}) ;

		camLinkIcon = (ImageView) view.findViewById(R.id.cam_icon);
		camLinkIcon.setImageResource(R.drawable.cam_detect1);
		camLinkIcon.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPlaying) {
//					mLibVLC.stop();
					//mVideoView.releasePlayer();
					isPlaying = false;
				}
				Log.e(TAG,"ReDiscover");
				if (ipGet != null)
					if (!ipGet.isDiscovering())
					{
						camConnected = false;
						//ipGet.startDiscover(0,1000);
					}
			}
		}) ;

		
		camLinkIp = (TextView) view.findViewById(R.id.cam_ip);	
		camLinkIp.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				textcnt ++;
				if (textcnt >= 5) {
					camLinkIp.setVisibility(View.INVISIBLE);
					camLinkIpEd.setVisibility(View.VISIBLE);
					camLinkIpSet.setVisibility(View.VISIBLE);		
				}
			}
		});
		
		camLinkIpEd = (EditText) view.findViewById(R.id.cam_ip_edit);
		camLinkIpEd.setVisibility(View.INVISIBLE);
		//cam_ip_edit
		
		camLinkIpSet = (Button) view.findViewById(R.id.cam_ip_set);
		camLinkIpSet.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				textcnt = 0;
				CameraUIConfig.setIP(camLinkIpEd.getText().toString());
				saveCamIp(camLinkIpEd.getText().toString());
				cameraIp = camLinkIpEd.getText().toString();
				camLinkIp.setText(readCamIp());
				camLinkIp.setVisibility(View.VISIBLE);
				camLinkIpEd.setVisibility(View.INVISIBLE);
				camLinkIpSet.setVisibility(View.INVISIBLE);
			}
		});
		camLinkIpSet.setVisibility(View.INVISIBLE);
		
		cameraIp = readCamIp();
		if (cameraIp != null) {
			CameraUIConfig.setIP(cameraIp);
			camLinkIpEd.setText(cameraIp);
			camLinkIp.setText(readCamIp());
		}else
			camLinkIpEd.setText("192.168.1.1");

		//mVideoView.createPlayer(mMediaUrl);
		mVideoView.setHandler(handler);
		return view;
	}
	//ProgressDialog
	@Override
	public void onResume() {
		Log.d(TAG,"onResume");
		//if (ipGet == null)
		//	ipGet = new IPCamIpDiscover(handler);
		//ipGet.startDiscover(0, 1000);

		/*		if (mpWait == null) {
			mpWait = new ProgressDialog(getActivity());
			mpWait.setTitle("Event Listening");
			mpWait.show();
		}
		 */
		if (mWhistler == null) {
			mWhistler = new IPCamWhistlerReceiver(handler);
			mWhistler.start();
		}
		//play();
//		ViewGroup.LayoutParams lp = mSurfaceFrame.getLayoutParams() ;
//		lp = mSurfaceFrame.getLayoutParams() ;
//		lp.width = 720 ;
//		lp.height = 400 ;
//		mSurfaceFrame.setLayoutParams(lp) ;
		super.onResume() ;

	}
	@Override
	public void onPause() {
		Log.d(TAG,"onPause");
		/*		if (mpWait != null) {
			//mpWait = new ProgressDialog(getActivity());
			mpWait.dismiss();
			mpWait = null;
		}
		 */
		//if (ipGet != null)
		//	ipGet.stopDiscover();

		if (mWhistler != null) {
			mWhistler.alive=false;
			mWhistler = null;
		}
		stopStreamButton.setVisibility(View.INVISIBLE);
		super.onPause() ;

	}
	@Override
	public void onDestroy() {
		//mVideoView.releasePlayer();
		super.onDestroy() ;
	}

	private void OpenFile(String filename)
	{
		File f1 = new File( filename );
		String vlowerFileName = filename.toLowerCase();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		if(vlowerFileName.endsWith("mp4")
				|| vlowerFileName.endsWith("mov")
				|| vlowerFileName.endsWith("MP4")
				|| vlowerFileName.endsWith("MOV")) {
			intent.setDataAndType(Uri.fromFile(f1), "video/3gp") ;
			startActivity(intent);

		}else if(vlowerFileName.endsWith("jpg")
				|| vlowerFileName.endsWith("jpeg")
				|| vlowerFileName.endsWith("JPG")
				|| vlowerFileName.endsWith("JPEG")) {
			intent.setDataAndType(Uri.fromFile(f1), "image/jpeg") ;
			startActivity(intent);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig) ;
	}

	public void play(boolean isRTSP) {
		Activity activity = getActivity() ;
		final boolean rtsp = isRTSP;
		if (activity != null) {

			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() {
					mWhistlerLayout.setVisibility(View.VISIBLE);
					mVideoView.createPlayer(mMediaUrl,rtsp,600);
					isPlaying = true;
					stopStreamButton.setVisibility(View.VISIBLE);
					//stopStreamButton.setEnabled(isPlaying);
				} 
			}, 0) ;
		}		
	}

	//IP Saving
	private static final String dip = "DoorCamIP";
	private String readCamIp(){
		SharedPreferences saveIp = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
		return saveIp.getString(dip, null);
	}
	
	private void saveCamIp(String ip){
		SharedPreferences saveIp = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
		saveIp.edit()
	        .putString(dip, ip)
	        .commit();
	}
	
	/* Power Off CGI Task*/
	private class SendCGIPoweroff extends AsyncTask<URL, Integer, String> {
//		Context context = VLCApplication.getAppContext() ;
		protected void onPreExecute() {
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			//URL url = CameraCommand.commandQueryAV1Url() ;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			URL url = CameraCommand.commandCamPowerOff();
			if (url != null) {		
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				if (result.contains("OK")) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Toast.makeText(MainActivity.getAppContext(),
							MainActivity.getAppContext().getResources().getString(R.string.message_command_succeed),
							Toast.LENGTH_SHORT).show();
					pirShowing = false;
					pirEvnGet = false;
					pirDownload = false;
					rtspOn = false;
					camLinkIcon.setImageResource(R.drawable.cam_detect1);
				}
				else
					Toast.makeText(MainActivity.getAppContext(),
							MainActivity.getAppContext().getResources().getString(R.string.message_command_failed),
							Toast.LENGTH_SHORT).show() ;
			}else {
				Toast.makeText(MainActivity.getAppContext(),
						MainActivity.getAppContext().getResources().getString(R.string.message_command_failed),
						Toast.LENGTH_SHORT).show() ;
			}
			powerOffButton.setEnabled(true);
			super.onPostExecute(result) ;
		}
	}

	private void openPIRDialog() {
		builder = new IPCamWhistlerPIRDialog(MainActivity.getAppContext());
		builder.setPirText("Picture downloading");
		builder.byes.setEnabled(false);
		builder.bno.setEnabled(false);
		builder.show();
		builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	}

	private void showImage(String path) {
		if ((builder != null) && builder.isShowing())
		builder.setPirImg(path);
		builder.isDownloaded = true;
		builder.setPirText("Finished");
		if (builder.isVideoEvt)
			setPIRVideoDownload();

	}
	private void setPIRVideoDownload() {
		if ((builder != null) && builder.isShowing())
			if (builder.isDownloaded) {
				builder.byes.setEnabled(true);
				builder.bno.setEnabled(true);
				builder.setPirText("Do you want to download Video?");
			}else
				builder.isVideoEvt = true;
	}

	//play buzzer
	private void playBuzzer(boolean ring) {
		MediaPlayer mp;
		if (ring)
			mp = MediaPlayer.create(MainActivity.getAppContext(),R.raw.buzzer);
		else
			mp = MediaPlayer.create(MainActivity.getAppContext(),R.raw.pirbuzzer);
		mp.start();
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
					mp.release();
			}
		});
	}

	private void parseIPandSave(String str) {

		String[] ipaddr = null;
		String[] ipaddr_t = null;

		if (str.contains("rtsp"))
			ipaddr_t = str.split("rtsp://");
		else if (str.contains("http"))
			ipaddr_t = str.split("http://");
		else if (str.contains("ring"))
			ipaddr_t = str.split("ring=");
		else if (str.contains("pir"))
			ipaddr_t = str.split("pir=");

		if (ipaddr_t[1] != null)
			ipaddr = ipaddr_t[1].split("/");

		if (ipaddr[0] != null) {
			Log.e("IP", "IP= " + ipaddr[0]);
			CameraUIConfig.setIP(ipaddr[0]);
			//if (ipGet != null) {
			camConnected = true;
			//	ipGet.stopDiscover();
			//}
			camLinkIcon.setImageResource(R.drawable.cam_connect);
			camListenCnt = 0;
			camLinkIp.setText(ipaddr[0]);
			saveCamIp(ipaddr[0]);
		}
	}

}
