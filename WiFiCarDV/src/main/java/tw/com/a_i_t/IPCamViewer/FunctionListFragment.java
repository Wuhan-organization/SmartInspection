package tw.com.a_i_t.IPCamViewer ;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tw.com.a_i_t.IPCamViewer.Control.CameraControlFragment ;
import tw.com.a_i_t.IPCamViewer.DroneCam.DroneCamViewer;
import tw.com.a_i_t.IPCamViewer.FileBrowser.FileBrowserFragment ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.BrowserSettingFragment ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.LocalFileBrowserFragment ;
import tw.com.a_i_t.IPCamViewer.FileUtility.NetFileDownload;
import tw.com.a_i_t.IPCamViewer.IPCam.IPCamWhistlerViewer;
import tw.com.a_i_t.IPCamViewer.Viewer.MjpegPlayerFragment ;
import tw.com.a_i_t.IPCamViewer.Viewer.StreamPlayerFragment ;
import tw.com.a_i_t.IPCamViewer.Viewer.ViewerSettingFragment ;

import android.app.AlertDialog;
import android.app.Fragment ;
import android.content.Context ;
import android.content.DialogInterface;
import android.net.DhcpInfo ;
import android.net.wifi.WifiManager ;
import android.os.AsyncTask;
import android.os.Bundle ;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater ;
import android.view.MotionEvent ;
import android.view.View ;
import android.view.View.OnClickListener ;
import android.view.View.OnTouchListener ;
import android.view.ViewGroup ;
import android.widget.RelativeLayout ;

public class FunctionListFragment extends Fragment {
	private static CameraSniffer sniffer = null;
	private int h264_cache = 600;
	private int mjpeg_cache = 400;
	private MyPeeker peeker;
	public static boolean Mode_SW_Enable = false;
	Timer timer = null;
	String broidcast_ip=null;

	private static Handler menuHandler = null;

	String camType = "CARDV";//"CARDV","DOORBELL","DRONECAM","AUTOTEST"

	class MyPeeker extends CameraPeeker{
		FunctionListFragment theFrag;
		MyPeeker(FunctionListFragment frag) {
			theFrag = frag;
		}
		public void Update(String s) {
			theFrag.Reception(s);
		}
	}

	public FunctionListFragment() {
		CameraUIConfig.setUItype(camType);//"CARDV","DOORBELL","DRONECAM", "AUTOTEST"

		if (FunctionListFragment.sniffer == null) {
			FunctionListFragment.sniffer = new CameraSniffer();
			FunctionListFragment.sniffer.start();
		}
		peeker = new MyPeeker(this);
		sniffer.SetPeeker(peeker);
	}
	public static CameraSniffer GetCameraSniffer() {
		return sniffer;
	}

	public static boolean GetCameraSWModeEn() {
		return Mode_SW_Enable;
	}

	public static void SetCameraSWModeEn(boolean En) {
		Mode_SW_Enable= En;
	}

	/* Query property of RTSP AV1 */
	private class GetRTPS_AV1 extends AsyncTask<URL, Integer, String> {
		String CamIp;
		protected void onPreExecute() {
			CamIp = CameraCommand.getCameraIp();
			Log.d("Get Camera IP","IP="+CamIp);
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandQueryAV1Url() ;
			if (url != null) {		
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			String	liveStreamUrl;
			WifiManager wifiManager = (WifiManager)
					getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

			if ((dhcpInfo == null || dhcpInfo.gateway == 0)&&(CamIp == null)) {
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create() ;
				alertDialog.setTitle(getResources().getString(R.string.dialog_DHCP_error)) ;
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
						getResources().getString(R.string.label_ok),
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss() ;

					}
				}) ;
				alertDialog.show() ;
				return;
			}
			String gateway = MainActivity.intToIp(dhcpInfo.gateway) ;
			// set http push as default for streaming
			liveStreamUrl = "http://" + CamIp/*gateway*/ + MjpegPlayerFragment.DEFAULT_MJPEG_PUSH_URL ;
			if (result != null) {
				String[] lines;
				try {
					String[] lines_temp = result.split("Camera.Preview.RTSP.av=");
					lines = lines_temp[1].split(System.getProperty("line.separator")) ;
					int av = Integer.valueOf(lines[0]);
					//LibVLC instance = LibVLC.getExistingInstance();
					switch (av) {
					case 1:	// liveRTSP/av1 for RTSP MJPEG+AAC
						//instance.setNetworkCaching(mjpeg_cache);
						liveStreamUrl = "rtsp://" + CamIp/*gateway*/ + MjpegPlayerFragment.DEFAULT_RTSP_MJPEG_AAC_URL ;
						break;
					case 2: // liveRTSP/v1 for RTSP H.264
						//instance.setNetworkCaching(h264_cache);
						liveStreamUrl = "rtsp://" + CamIp/*gateway*/ + MjpegPlayerFragment.DEFAULT_RTSP_H264_URL ;
						break;
					case 3: // liveRTSP/av2 for RTSP H.264+AAC
						//instance.setNetworkCaching(h264_cache);
						liveStreamUrl = "rtsp://" + CamIp/*gateway*/ + MjpegPlayerFragment.DEFAULT_RTSP_H264_AAC_URL ;
						break;
					case 4: // liveRTSP/av4 for RTSP H.264+PCM
						//instance.setNetworkCaching(h264_cache);
						liveStreamUrl = "rtsp://" + CamIp/*gateway*/ + MjpegPlayerFragment.DEFAULT_RTSP_H264_PCM_URL ;
						break;
					}
				} catch (Exception e) {/* not match, for firmware of MJPEG only */}
			}
			Fragment fragment = StreamPlayerFragment.newInstance(liveStreamUrl) ;
			MainActivity.addFragment(FunctionListFragment.this, fragment) ;
			super.onPostExecute(result) ;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.function_list, container, false) ;
		OnTouchListener onTouch = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.selected_background) ;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					v.setBackgroundResource(0) ;
				}
				return false ;
			}
		} ;

		RelativeLayout control = (RelativeLayout) view.findViewById(R.id.functionListControl) ;

		control.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				MainActivity.addFragment(FunctionListFragment.this, new CameraControlFragment()) ;
			}
		}) ;

		control.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.settingItem))
			control.setVisibility(View.GONE);

		RelativeLayout preview = (RelativeLayout) view.findViewById(R.id.functionListPreview) ;

		preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean engineerMode = ((MainActivity) getActivity()).mEngineerMode ;

				if (engineerMode) {

					MainActivity.addFragment(FunctionListFragment.this, new ViewerSettingFragment()) ;
				} else {
						if (CameraUIConfig.uiType == CameraUIConfig.cdvView)
							new GetRTPS_AV1().execute();
						else if (CameraUIConfig.uiType == CameraUIConfig.droneView) {
							//
							Fragment fragment = 
									StreamPlayerFragment.newInstance("rtsp://" + CameraCommand.getCameraIp() + ":8880/channel1") ;
							MainActivity.addFragment(FunctionListFragment.this, fragment) ;
							//
						}
				}
			}
		}) ;

		preview.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.cardvpreviewItem))
			preview.setVisibility(View.GONE);

		/////DroneCAM
		RelativeLayout DroneCam = (RelativeLayout) view.findViewById(R.id.functionListDcam) ;

		DroneCam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.addFragment(FunctionListFragment.this, new DroneCamViewer()) ;
			}
		}) ;

		DroneCam.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.dronepreviewItem))
			DroneCam.setVisibility(View.GONE);

		RelativeLayout browser = (RelativeLayout) view.findViewById(R.id.functionListBrowser) ;

		browser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean engineerMode = ((MainActivity) getActivity()).mEngineerMode ;

				if (engineerMode) {

					MainActivity.addFragment(FunctionListFragment.this, new BrowserSettingFragment()) ;
				} else {
					Fragment fragment = FileBrowserFragment.newInstance(null, null, null) ;

					MainActivity.addFragment(FunctionListFragment.this, fragment) ;
				}
			}
		}) ;

		browser.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.filelstItem))
			browser.setVisibility(View.GONE);

		RelativeLayout localAlbum = (RelativeLayout) view.findViewById(R.id.functionListLocalAlbum) ;

		localAlbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.addFragment(FunctionListFragment.this, new LocalFileBrowserFragment()) ;
			}
		}) ;

		localAlbum.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.localfiletItem))
			localAlbum.setVisibility(View.GONE);
		/////Ip CAM
		RelativeLayout IpCam = (RelativeLayout) view.findViewById(R.id.functionListIpcamevent) ;

		IpCam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.addFragment(FunctionListFragment.this, new IPCamWhistlerViewer()) ;
			}
		}) ;

		IpCam.setOnTouchListener(onTouch) ;
		//Disable Ip CAM function
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.doorbellItem))
			IpCam.setVisibility(View.GONE);

		/////DroneCAM
/*		RelativeLayout DroneCam = (RelativeLayout) view.findViewById(R.id.functionListDcam) ;

		DroneCam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.addFragment(FunctionListFragment.this, new DroneCamViewer()) ;
			}
		}) ;

		DroneCam.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.dronepreviewItem))
			DroneCam.setVisibility(View.GONE);
*/
		/////AutoTest
		RelativeLayout AutoTest = (RelativeLayout) view.findViewById(R.id.functionListAcam) ;

		AutoTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//MainActivity.addFragment(FunctionListFragment.this, new AutoTestViewer()) ;
			}
		}) ;

		AutoTest.setOnTouchListener(onTouch) ;
		if (!CameraUIConfig.getUIDisplay(CameraUIConfig.autotestItem))
			AutoTest.setVisibility(View.GONE);
		return view ;
	}

	public void Reception(String s) {
		String val;
		// Get Ip Address
		val = CameraSniffer.GetIpAddr(s);
		if (val != null) {
			Log.i("FunctionListFragment", "===Get device IP Address " + val);
			sniffer.setCameraIp(val);
		}
//		val = CameraSniffer.GetshortFileUrl(s);
		if ((s != null)&&(menuHandler != null))  {
			Log.e("FunctionListFragment", "===GetshortFileUrl =  " + val);
			Message message;
			message = menuHandler.obtainMessage();
			message.obj = s;
			menuHandler.sendMessage(message);
		}

		//auto open download dialog
		val = null;
		val = CameraSniffer.GetshortFileUrl(s);

		if ((val != null)&& (val.contains("SD"))) {
			Message message;
			message = mHandler.obtainMessage();
			message.obj = val;
			message.arg1 = NetFileDownload.NET_DOWNLOAD_STATR;;
			mHandler.sendMessage(message);
		}
	}

	public class DiscoverDeviceRoutine extends TimerTask
	{
		public void run()
		{
			Log.d("FunctionListFragment","DiscoverDeviceRoutine");
			if (CameraCommand.isAPEnable())
				sendbroadcast();
		}
	};

	@Override
	public void onResume() {
		super.onResume() ;
		broidcast_ip = getWifiApBroadcastIpAddress();
		sniffer.SetPeeker(peeker);
		//		if (CameraCommand.isAPEnable()) {
		timer = new Timer(true);
		timer.schedule(new DiscoverDeviceRoutine(), 0, 8000);
		//		}
	}
	@Override
	public void onPause() {
		super.onPause() ;
		if (timer != null) 
			timer.cancel();
	}

	/*	private InetAddress getBroadcastAddress(WifiManager wm, int ipAddress) throws IOException {
	    DhcpInfo dhcp = wm.getDhcpInfo();
	    if(dhcp == null)
	        return InetAddress.getByName("255.255.255.255");
	    int broadcast = (ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	} 
	 */	
	public String getWifiApBroadcastIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
					en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().contains("wlan")) {
					///
					List<InterfaceAddress> faceAddresses = intf.getInterfaceAddresses();
					for (InterfaceAddress faceAddress : faceAddresses) {
						InetAddress address = faceAddress.getAddress();
						if (address.isLoopbackAddress() == true || address.getHostAddress().contains(":")) {
							continue;
						}
						//get Local IP
						byte[] ipBytes = address.getAddress();
						//long ipData = (ipBytes[0] << 24) + (ipBytes[1] << 16) + (ipBytes[2] << 8) + (ipBytes[3]);
						int IP[] = {0,0,0,0};
						IP[0]=ipBytes[0]& 0xff;
						IP[1]=ipBytes[1]& 0xff;
						IP[2]=ipBytes[2]& 0xff;
						IP[3]=ipBytes[3]& 0xff;
						String ip_str = IP[0]+"."+IP[1]+"."+IP[2]+"."+IP[3];
						Log.d("getIpAddress", "IP="+ip_str);

						//get Local IP subnet mask
						long ipMask = (faceAddress.getNetworkPrefixLength());
						int mshift = 0xffffffff<<(32-ipMask);
						int oct[] = {0,0,0,0};
						oct[0] = ((byte) ((mshift&0xff000000)>>24)) & 0xff;
						oct[1] = ((byte) ((mshift&0x00ff0000)>>16)) & 0xff;
						oct[2] = ((byte) ((mshift&0x0000ff00)>>8)) & 0xff;
						oct[3] = ((byte) (mshift&0x000000ff)) & 0xff;
						String submask = oct[0]+"."+oct[1]+"."+oct[2]+"."+oct[3];
						Log.d("getIpAddress", "Submask="+submask);

						//int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
						//get transfer Local IP & netmask tp broadcastIP 
						int[] bip = {0,0,0,0};
						for (int i = 0; i < 4; i++)
							bip[i] = ((IP[i] & oct[i]) | ~oct[i])&0xff;//(byte) (broadcast >> (k * 8));

						String BroadcastIP = bip[0]+"."+bip[1]+"."+bip[2]+"."+bip[3];
						Log.d("getIpAddress", "BroadcastIP="+BroadcastIP);
						return BroadcastIP;

					}
					/*
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
	                        .hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()
	                            && (inetAddress.getAddress().length == 4)) {
	                        //Log.d("WWWW", inetAddress.getHostAddress());
	                        return inetAddress.getHostAddress();
	                    }
	                }*/
				}
			}
		} catch (SocketException ex) {
			Log.e("getIpAddress", ex.toString());
		}
		return null;
	}  

	public void sendbroadcast() {
		Thread background=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String sendData = "DISCOVER.CAMERA.IP";
				try {
					DatagramSocket dsock;
					dsock = new DatagramSocket();
					dsock.setBroadcast(true);
					DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), 
							InetAddress.getByName(broidcast_ip), 49132);
					dsock.send(sendPacket);
					dsock.close();
				} catch (Exception e)   {
					Log.d("sendbroadcast","Exception!!!!!");
				}
			}
		});
		background.start();
	}
	///one click sharing download
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
				case NetFileDownload.NET_DOWNLOAD_STATR:
					NetFileDownload mDownload;
					String s= ((String)msg.obj).replace('\\','/');
					String filename[]=s.split("SD:");
					mDownload = new NetFileDownload(getActivity());
					mDownload.startDownload("http://"+CameraCommand.getCameraIp()+"/SD" + filename[1]);
					Log.e("FunctionList","path=" + s);
					break;
			}
			super.handleMessage(msg);
		}
	};

	public static void setMenuHandler(Handler hld)
	{
		menuHandler = hld;
	}
}

