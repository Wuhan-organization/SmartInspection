package tw.com.a_i_t.IPCamViewer.FileBrowser ;

import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.net.HttpURLConnection ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.ArrayList ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.Locale ;

import tw.com.a_i_t.IPCamViewer.CameraCommand ;
import tw.com.a_i_t.IPCamViewer.CameraPeeker;
import tw.com.a_i_t.IPCamViewer.CameraSniffer;
import tw.com.a_i_t.IPCamViewer.FunctionListFragment;
import tw.com.a_i_t.IPCamViewer.MainActivity ;
import tw.com.a_i_t.IPCamViewer.Property.IPCamProperty;
import tw.com.a_i_t.IPCamViewer.R ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.Model.FileNode ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.Model.FileNode.Format;
import android.app.Activity ;
import android.app.AlertDialog ;
import android.app.Fragment ;
import android.app.Notification ;
import android.app.NotificationManager ;
import android.app.PendingIntent ;
import android.app.ProgressDialog ;
import android.content.BroadcastReceiver;
import android.content.Context ;
import android.content.DialogInterface ;
import android.content.Intent ;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo ;
import android.net.NetworkInfo;
import android.net.Uri ;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager ;
import android.net.wifi.WifiManager.WifiLock ;
import android.os.AsyncTask ;
import android.os.Bundle ;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager ;
import android.util.Log ;
import android.view.LayoutInflater ;
import android.view.View ;
import android.view.View.OnClickListener ;
import android.view.ViewGroup ;
import android.webkit.MimeTypeMap ;
import android.widget.AdapterView ;
import android.widget.AdapterView.OnItemClickListener ;
import android.widget.ArrayAdapter;
import android.widget.Button ;
import android.widget.CheckedTextView ;
import android.widget.ListView ;
import android.widget.Spinner;
import android.widget.TextView ;
import android.widget.Toast;

public class FileBrowserFragment extends Fragment {

	public final static String TAG = "FileBrowserFragment" ;
	public static final String DEFAULT_PATH = "/cgi-bin/Config.cgi" ;
	public static final String DEFAULT_DIR = "DCIM" ;
	private ProgressDialog mProgDlg;
	private int	mTotalFile;
	private int mfrom;
	private boolean mCancelDelete;
	private static int mFilelistId = 0;
	
	private boolean isPlackbackMode = false;
	
	DownloadFileListTask flistdTask = null;
	DownloadFileListTask.ContiunedDownloadTask cflistdTask = null;
	
	static boolean bflistdTask;

	//for folder selection
	private String allDB[] = {"DCIM","Normal", "Photo", "Event", "Parking"};
//	private String standardDB[] = {"DCIM"};
	private String folderDB[] = allDB;

	private BroadcastReceiver mNetworkReceiver;

	class MyPeeker extends CameraPeeker{
		FileBrowserFragment theFrag;
		MyPeeker(FileBrowserFragment frag) {
			theFrag = frag;
		}
		public void Update(String s) {
			theFrag.Reception(s);
		}
	}
	//sniffer handler
	public FileBrowserFragment() {
		FunctionListFragment.setMenuHandler(sfHandler);
		//CameraSniffer sniffer = FunctionListFragment.GetCameraSniffer();
		//MyPeeker peeker = new MyPeeker(this);
		//sniffer.SetPeeker(peeker,getActivity());
	}
	
	public void Reception(String s) {
		// Log.i("BROWSER", s);
	}

	private class DownloadFileListTask extends AsyncTask<FileBrowser, Integer, FileBrowser> {

		private class ContiunedDownloadTask extends AsyncTask<FileBrowser, Integer, FileBrowser> {

			@Override
			protected FileBrowser doInBackground(FileBrowser... browsers) {

				mfrom = browsers[0].retrieveFileList(mFilelistId, mDirectory, FileNode.Format.all, mfrom) ;

				return browsers[0] ;
			}

			@Override
			protected void onPostExecute(FileBrowser result) {

				Activity activity = getActivity() ;

				if (activity == null)
					return ;

				if (activity != null) {

					List<FileNode> fileList = result.getFileList() ;

					sFileList.addAll(fileList) ;
					mFileListAdapter.notifyDataSetChanged() ;

					if (!result.isCompleted() && fileList.size() != 0) {
						mFileListTitle.setText(mFileBrowser + " : " + mReading + " " + mDirectory + " ("
								+ sFileList.size() + " " + mItems + ")") ;
						cflistdTask = new ContiunedDownloadTask();
						cflistdTask.execute(result) ;
						//new ContiunedDownloadTask().execute(result) ;
					} else {
						mFileListTitle.setText(mFileBrowser + " : " + mDirectory + " (" + sFileList.size()
								+ " " + mItems + ")") ;
						setWaitingState(false) ;
						mSwitchButton.setEnabled(true);
						mBrowserFolder.setEnabled(true) ;
						bflistdTask = false;
					}
				}
			}
		}

		@Override
		protected void onPreExecute() {

			setWaitingState(true) ;
			mSwitchButton.setEnabled(false);
			sFileList.clear() ;
			mFileListAdapter.notifyDataSetChanged() ;

			sSelectedFiles.clear() ;
			mSaveButton.setEnabled(false) ;
			mOpenButton.setEnabled(false) ;
			mDeleteButton.setEnabled(false) ;
			mBrowserFolder.setEnabled(false) ;

			mfrom = 0;
			mFileListTitle.setText(mFileBrowser + " : " + mReading + " " + mDirectory) ;
			super.onPreExecute() ;
		}

		@Override
		protected FileBrowser doInBackground(FileBrowser... browsers) {
			Log.i("Browser", "From" + mfrom);
			mfrom = browsers[0].retrieveFileList(mFilelistId, mDirectory, FileNode.Format.all, 0) ;
			return browsers[0] ;
		}

		@Override
		protected void onPostExecute(FileBrowser result) {

			Activity activity = getActivity() ;

			if (activity != null) {

				List<FileNode> fileList = result.getFileList() ;

				sFileList.addAll(fileList) ;
				mFileListAdapter.notifyDataSetChanged() ;

				if (!result.isCompleted() && fileList.size() > 0) {
					mFileListTitle.setText(mFileBrowser + " : " + mReading + " " + mDirectory + " ("
							+ sFileList.size() + " " + mItems + ")") ;
					cflistdTask = new ContiunedDownloadTask();
					cflistdTask.execute(result) ;
					//new ContiunedDownloadTask().execute(result) ;
				} else {
					mFileListTitle.setText(mFileBrowser + " : " + mDirectory + " (" + sFileList.size() + " "
							+ mItems + ")") ;
					setWaitingState(false) ;
					mSwitchButton.setEnabled(true);
					mBrowserFolder.setEnabled(true) ;
					bflistdTask = false;
					
				}
			}
		}
	}

	private String mIp ;
	private String mPath ;
	private String mDirectory ;

	private static final String KEY_IP = "ip" ;
	private static final String KEY_PATH = "path" ;
	private static final String KEY_DIRECTORY = "directory" ;

	private static ArrayList<FileNode> sFileList = new ArrayList<FileNode>() ;
	private static List<FileNode> sSelectedFiles = new LinkedList<FileNode>() ;
	private static FileListAdapter mFileListAdapter ;

	private ListView mFileListView ;
	private TextView mFileListTitle ;
	private Button mSaveButton ;
	private Button mDeleteButton ;
	private Button mOpenButton ;
	private Button mSwitchButton ;
	private Spinner mBrowserFolder;
	private boolean fistSelect = true;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	private String mFileBrowser ;
	private String mReading ;
	private String mItems ;

	//file lock symbol
	private boolean mFileLockFlag ;

	public static FileBrowserFragment newInstance(String ip, String url, String directory) {

		FileBrowserFragment fragment = new FileBrowserFragment() ;

		Bundle args = new Bundle() ;

		if (ip != null)
			args.putString(KEY_IP, ip) ;

		if (url != null)
			args.putString(KEY_PATH, url) ;

		if (directory != null)
			args.putString(KEY_DIRECTORY, directory) ;

		fragment.setArguments(args) ;

		return fragment ;
	}

	private static int sNotificationCount = 0 ;
	private static DownloadTask sDownloadTask = null ;

	private static class DownloadTask extends AsyncTask<URL, Long, Boolean> {

		String mFileName ;
		Context mContext ;
		WifiLock mWifiLock ;
		String mIp ;
		boolean mCancelled ;
		PowerManager.WakeLock mWakeLock ;

		private ProgressDialog mProgressDialog ;

		DownloadTask(Context context) {

			mContext = context ;
		}

		@Override
		protected void onPreExecute() {

			Log.i("DownloadTask", "onPreExecute") ;

			WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE) ;
			mWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "DownloadTask") ;
			mWifiLock.acquire() ;

			PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE) ;
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DownloadTask") ;
			mWakeLock.acquire() ;

			mCancelled = false ;
			showProgress(mContext) ;

			super.onPreExecute() ;
		}

		@Override
		protected Boolean doInBackground(URL... urls) {

			Log.i("DownloadTask", "doInBackground " + urls[0]) ;

			try {
				mIp = urls[0].getHost() ;

				HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection() ;
				urlConnection.setRequestMethod("GET") ;
				urlConnection.setConnectTimeout(3000) ;
				urlConnection.setReadTimeout(10000) ;
				urlConnection.setUseCaches(false) ;
				urlConnection.setDoInput(true) ;
				urlConnection.setRequestProperty("Accept-Encoding", "identity");
				urlConnection.connect() ;
				InputStream inputStream = urlConnection.getInputStream() ;

				mFileName = urls[0].getFile().substring(urls[0].getFile().lastIndexOf(File.separator) + 1) ;

				File appDir = MainActivity.getAppDir() ;
				File file = new File(appDir, mFileName) ;

				Log.i("Path", appDir.getPath() + File.separator + mFileName) ;

				if (file.exists()) {
					file.delete() ;
				}

				file.createNewFile() ;
				FileOutputStream fileOutput = new FileOutputStream(file) ;

				byte[] buffer = new byte[1024] ;
				int bufferLength = 0 ;
				try {
					Log.i("progress", "Content-Length: " + Long.valueOf(urlConnection.getContentLength()));
					while ((bufferLength = inputStream.read(buffer)) > 0) {
						publishProgress(Long.valueOf(urlConnection.getContentLength()), file.length());
						//Log.i("Write", "Write " + bufferLength);
						fileOutput.write(buffer, 0, bufferLength) ;
						if (mCancelled) {
							urlConnection.disconnect();
							break ;
						}
					}
				} finally {
					//Log.i("DownloadTask", "doInBackground close START") ;
					//inputStream.close() ;
					//fileOutput.close() ;
					//Log.i("DownloadTask", "doInBackground disconnect START") ;
					//urlConnection.disconnect() ;
					//Log.i("DownloadTask", "doInBackground disconnect FINISHED") ;
				}
				if (mCancelled && file.exists()) {
					file.delete() ;
					return false ;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace() ;
				return false ;
			}

			return true ;
		}

		@Override
		protected void onProgressUpdate(Long... values) {

			if (mProgressDialog != null) {
				long max = values[0].intValue() ;
				long progress = values[1].intValue() ;
				String unit = "Bytes" ;
				
				mProgressDialog.setTitle("Downloading " + mFileName) ;
				if (max != -1) {
					if (max > 1024) {
						max /= 1024 ;
						progress /= 1024 ;
						unit = "KB" ;
					}
					
					if (max > 1024) {
						max /= 1024 ;
						progress /= 1024 ;
						unit = "MB" ;
					}
				} else {
					progress /= (1024 * 1024);
					max = progress * 2;
					unit = "KB" ;
				}
				Log.i("Progress", "Progress " + progress);
				mProgressDialog.setMax((int)max) ;
				mProgressDialog.setProgress((int)progress) ;
				mProgressDialog.setProgressNumberFormat("%1d/%2d " + unit) ;
			}
			super.onProgressUpdate(values) ;
		}

		private void cancelDownload() {

			mCancelled = true ;
			
			for (FileNode fileNode : sSelectedFiles) {
				fileNode.mSelected = false ;
			}
			sSelectedFiles.clear() ;
			mFileListAdapter.notifyDataSetChanged() ;
		}

		@Override
		protected void onCancelled() {

			Log.i("DownloadTask", "onCancelled") ;

			if (mProgressDialog != null) {
				mProgressDialog.dismiss() ;
				mProgressDialog = null ;
			}
			sDownloadTask = null ;

			mWakeLock.release() ;
			mWifiLock.release() ;

			super.onCancelled() ;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			Log.i("DownloadTask", "onPostExecute " + mFileName + " " + (mCancelled ? "CANCELLED"
					: result ? "SUCCESS" : "FAIL")) ;

			if (mProgressDialog != null) {
				mProgressDialog.dismiss() ;
				mProgressDialog = null ;
			}
			sDownloadTask = null ;

			mWakeLock.release() ;
			mWifiLock.release() ;

			if (mContext != null) {
				String ext = mFileName.substring(mFileName.lastIndexOf(".") + 1).toLowerCase(Locale.US) ;
				int	nIcon;
				if (ext.equalsIgnoreCase("jpg")) {
					nIcon = R.drawable.type_photo;
				} else {
					nIcon = R.drawable.type_video;
				}
				if (!result) {
					if (!mCancelled) {
						Notification notification = new Notification.Builder(mContext)
								.setContentTitle(mFileName).setSmallIcon(nIcon)
								.setContentText("Download Failed").getNotification() ;

						NotificationManager notificationManager = (NotificationManager) mContext
								.getSystemService(Context.NOTIFICATION_SERVICE) ;

						notification.flags |= Notification.FLAG_AUTO_CANCEL ;

						notificationManager.notify(sNotificationCount++, notification) ;
					} else {
						Notification notification = new Notification.Builder(mContext)
								.setContentTitle(mFileName).setSmallIcon(nIcon)
								.setContentText("Download Cancelled").getNotification() ;

						NotificationManager notificationManager = (NotificationManager) mContext
								.getSystemService(Context.NOTIFICATION_SERVICE) ;

						notification.flags |= Notification.FLAG_AUTO_CANCEL ;

						notificationManager.notify(sNotificationCount++, notification) ;

					}
				} else {

					Uri uri = Uri.parse("file://" + MainActivity.sAppDir + File.separator + mFileName) ;

					String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ;

					Log.i("MIME", ext + "  ==>  " + mimeType) ;

					Log.i("Path", uri.toString()) ;

					Intent intent = new Intent(Intent.ACTION_VIEW) ;
					intent.setDataAndType(uri, mimeType) ;

					PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0) ;
					Notification notification = new Notification.Builder(mContext).setContentTitle(mFileName)
							.setSmallIcon(nIcon).setContentText("Download Completed")
							.setContentIntent(pIntent).getNotification() ;

					NotificationManager notificationManager = (NotificationManager) mContext
							.getSystemService(Context.NOTIFICATION_SERVICE) ;

					notification.flags |= Notification.FLAG_AUTO_CANCEL ;

					notificationManager.notify(sNotificationCount++, notification) ;
				}
			}
			if (!mCancelled)
				downloadFile(mContext, mIp) ;
			
			super.onPostExecute(result) ;
		}

		void showProgress(Context context) {
			mContext = context ;
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss() ;
			}
			mProgressDialog = new ProgressDialog(mContext) ;
			mProgressDialog.setTitle("Downloading") ;
			mProgressDialog.setMessage("Please wait") ;
			mProgressDialog.setCancelable(false) ;
			mProgressDialog.setMax(100) ;
			mProgressDialog.setProgress(0) ;
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) ;
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelDownload() ;
							dialog.dismiss() ;
						}
					}) ;
			mProgressDialog.show() ;
		}

		void hideProgress() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss() ;
				mProgressDialog = null ;
			}
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;

		mIp = getArguments().getString(KEY_IP) ;

		if (mIp == null) {
			WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE) ;

			DhcpInfo dhcpInfo = wifiManager.getDhcpInfo() ;

			if (dhcpInfo != null && dhcpInfo.gateway != 0) {

				mIp = MainActivity.intToIp(dhcpInfo.gateway) ;
			}
		}

		mPath = getArguments().getString(KEY_PATH) ;

		if (mPath == null) {

			mPath = DEFAULT_PATH ;
		}

		mDirectory = getArguments().getString(KEY_DIRECTORY) ;

		if (mDirectory == null) {

			mDirectory = DEFAULT_DIR ;
		}
		mNetworkReceiver = new WifiState();
	}

	private static void downloadFile(final Context context, String ip) {
		if (sSelectedFiles.size() == 0) {
			return ;
		}
		FileNode fileNode = sSelectedFiles.remove(0) ;
		fileNode.mSelected = false ;
		mFileListAdapter.notifyDataSetChanged() ;
		final String filename = fileNode.mName.substring(fileNode.mName.lastIndexOf("/") + 1) ;
		final String urlString = "http://" + CameraCommand.getCameraIp()/*ip*/ + fileNode.mName ;

		File appDir = MainActivity.getAppDir() ;
		final File file = new File(appDir, filename) ;

		Log.i("Path", appDir.getPath() + File.separator + filename) ;

		if (file.exists()) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create() ;
			alertDialog.setTitle(filename) ;
			alertDialog.setMessage("File already exists, overwrite?") ;
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss() ;
				}
			}) ;

			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					file.delete() ;
					try {
						sDownloadTask = new DownloadTask(context) ;
						sDownloadTask.execute(new URL(urlString)) ;
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace() ;
					}
					dialog.dismiss() ;
				}
			}) ;

			alertDialog.show() ;

		} else {
			try {
				sDownloadTask = new DownloadTask(context) ;
				sDownloadTask.execute(new URL(urlString)) ;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace() ;
			}
		}
	}

	private class CameraDeleteFile extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true);
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			FileNode fileNode = sSelectedFiles.get(0) ;
			URL url = CameraCommand.commandSetdeletesinglefileUrl(fileNode.mName);
			if (url != null) {
				return CameraCommand.sendRequest(url);
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			FileNode fileNode = sSelectedFiles.remove(0);
			Log.d(TAG, "delete file response:"+result) ;
			if (result != null && (result.equals("709\n???\n") != true) && (result.contains("723")!= true) ) {
				fileNode.mSelected = false ;
				sFileList.remove(fileNode);
				mFileListAdapter.notifyDataSetChanged() ;
				mFileListTitle.setText(mFileBrowser + " : " + mDirectory + " (" + sFileList.size()
						+ " " + mItems + ")") ;
				mProgDlg.setMessage("Please wait, deleteing " + fileNode.mName);
				mProgDlg.setProgress(mTotalFile - sSelectedFiles.size()) ;
				if (sSelectedFiles.size() > 0 && !mCancelDelete) {
					new CameraDeleteFile().execute();
				} else {
					if (mProgDlg != null) {
						mProgDlg.dismiss() ;
						mProgDlg= null ;
					}
					mFileListTitle.setText(mFileBrowser + " : " + mDirectory + " (" + sFileList.size()
							+ " " + mItems + ")") ;
					setWaitingState(false);
					if (mFileLockFlag)
					Toast.makeText(activity,
						activity.getResources().getString(R.string.label_file_locked),
						Toast.LENGTH_SHORT).show();
				}
			}
			else if (activity != null) {
				if (result.contains("723")) {
					Log.d(TAG, "delete file read only") ;
					mFileLockFlag = true;
				}

				fileNode.mSelected = false ;
				mFileListAdapter.notifyDataSetChanged() ;
				if (sSelectedFiles.size() > 0 && !mCancelDelete) {
					new CameraDeleteFile().execute();
				}else {
					if (mProgDlg != null) {
						mProgDlg.dismiss() ;
						mProgDlg= null ;
					}
					setWaitingState(false);
					if (mFileLockFlag)
					Toast.makeText(activity,
							activity.getResources().getString(R.string.label_file_locked),
							Toast.LENGTH_SHORT).show();
					mSaveButton.setEnabled(false) ;
					mDeleteButton.setEnabled(false) ;
					mOpenButton.setEnabled(false) ;
				}
//				Toast.makeText(activity,
//						activity.getResources().getString(R.string.message_command_failed),
//						Toast.LENGTH_SHORT).show();
//				setWaitingState(false);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.browser, container, false) ;
		
		mFileListAdapter = new FileListAdapter(inflater, sFileList) ;

		mFileBrowser = getActivity().getResources().getString(R.string.label_file_browser) ;
		mReading = getActivity().getResources().getString(R.string.label_reading) ;
		mItems = getActivity().getResources().getString(R.string.label_items) ;

		mFileListTitle = (TextView) view.findViewById(R.id.browserTitle) ;
		mFileListTitle.setText(mFileBrowser + " : " + mDirectory) ;

		mSaveButton = (Button) view.findViewById(R.id.browserDownloadButton) ;
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadFile(getActivity(), mIp) ;
				mSaveButton.setEnabled(false) ;
				mDeleteButton.setEnabled(false) ;
				mOpenButton.setEnabled(false) ;
			}
		}) ;

		mDeleteButton = (Button) view.findViewById(R.id.browserDeleteButton) ;
		mDeleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTotalFile = sSelectedFiles.size();
				if (mTotalFile > 0) {
					mProgDlg = new ProgressDialog(getActivity());
					mProgDlg.setCancelable(false);
					mProgDlg.setMax(mTotalFile) ;
					mProgDlg.setProgress(0);
					mProgDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) ;
					mProgDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									mCancelDelete = true;
								}
							}) ;
					mProgDlg.setTitle("Delete file in Camera");
					mProgDlg.setMessage("Please wait ...");
					mCancelDelete = false;
					mProgDlg.show();
					mFileLockFlag = false;
					new CameraDeleteFile().execute();
				}
//				mSaveButton.setEnabled(false) ;
//				mDeleteButton.setEnabled(false) ;
//				mOpenButton.setEnabled(false) ;
			}
		}) ;
		
		mOpenButton = (Button) view.findViewById(R.id.browserOpenButton) ;
		mOpenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileNode fileNode = sSelectedFiles.get(0);
				if (fileNode.mFormat == Format.mov || fileNode.mFormat == Format.mp4 || 
					fileNode.mFormat == Format.avi) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					/* CarDV WiFi Support Video container is 3GP (.MOV) */
					/* For HTTP File Streaming */
					intent.setDataAndType(Uri.parse("http://" + CameraCommand.getCameraIp() + fileNode.mName), "video/3gp") ;
					startActivity(intent);

					/* For HTML5 Video Streaming */
//					String filename = fileNode.mName.replaceAll("/", "\\$");
//					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//								Uri.parse("http://"+mIp+"/cgi-bin/Config.cgi?action=play&property=" + filename));
//					startActivity(browserIntent);
//					mSaveButton.setEnabled(false) ;
//					mDeleteButton.setEnabled(false) ;
//					mOpenButton.setEnabled(false) ;
				}else if (fileNode.mFormat == Format.jpeg)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					/* CarDV WiFi Support Video container is 3GP (.MOV) */
					/* For HTTP File Streaming */
					intent.setDataAndType(Uri.parse("http://" + CameraCommand.getCameraIp() + fileNode.mName), "image/jpeg") ;
					startActivity(intent);
				}
			}
		}) ;
		
		mSwitchButton = (Button) view.findViewById(R.id.browserCamswitch) ;
		if (mFilelistId == 0)
			mSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
		else if (mFilelistId == 1) 
			mSwitchButton.setText(getResources().getString(R.string.label_camera_switchtorear));
		mSwitchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSwitchButton.setEnabled(false);
				if (mFilelistId == 0) {
					mFilelistId = 1;
					mSwitchButton.setText(getResources().getString(R.string.label_camera_switchtorear));
				}else if (mFilelistId == 1) {
					mFilelistId = 0;
					mSwitchButton.setText(getResources().getString(R.string.label_camera_switchtofront));
				}
				startDownload();
				//new EnterPlaybackMode().execute();
			}

		}) ;
		
		mBrowserFolder = (Spinner)view.findViewById(R.id.browserfolder);

//		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
//		mBrowserFolder.setAdapter(adapter);
        //
		mBrowserFolder.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	if(fistSelect)//
            	{
            		fistSelect = false;
            		return;
            	}
                // TODO Auto-generated method stub
				if (IPCamProperty.FolderType == IPCamProperty.PL_FOLDERTYPE.UNKNOWN)
					mDirectory = folderDB[arg2];
				else
					mDirectory = IPCamProperty.FolderList.get(arg2);
				startDownload();
				//new EnterPlaybackMode().execute();

            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
                ////myTextView.setText("NONE");    
                ///arg0.setVisibility(View.VISIBLE);    
            }    
        });    
		
		mFileListView = (ListView) view.findViewById(R.id.browserList) ;
		mFileListView.setAdapter(mFileListAdapter) ;
		mFileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE) ;

		mFileListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ViewTag viewTag = (ViewTag) view.getTag() ;
				if (viewTag != null) {

					FileNode file = viewTag.mFileNode ;

					CheckedTextView checkBox = (CheckedTextView) view.findViewById(R.id.fileListCheckBox) ;
					checkBox.setChecked(!checkBox.isChecked()) ;
					file.mSelected = checkBox.isChecked() ;

					if (file.mSelected)
						sSelectedFiles.add(file) ;
					else
						sSelectedFiles.remove(file) ;

					if (sSelectedFiles.size() > 0) {
						if(sSelectedFiles.size() < 2) {
							if (sSelectedFiles.get(0).mFormat == Format.mov ||
									sSelectedFiles.get(0).mFormat == Format.mp4 ||
									sSelectedFiles.get(0).mFormat == Format.jpeg ||
									sSelectedFiles.get(0).mFormat == Format.avi) {
								mOpenButton.setEnabled(true);
							}
						} else {
							mOpenButton.setEnabled(false) ;
						}
						mSaveButton.setEnabled(true) ;
						mDeleteButton.setEnabled(true) ;
					} else {
						mOpenButton.setEnabled(false) ;
						mSaveButton.setEnabled(false) ;
						mDeleteButton.setEnabled(false) ;
					}
				}
			}
		}) ;

		return view ;
	}
	@Override
	public void onDestroy() {
		//new ExitPlaybackMode().execute();
		//mFileListAdapter.notifyDataSetChanged() ;
		super.onDestroy();
	}
	private boolean mWaitingState = false ;
	private boolean mWaitingVisible = false ;

	private void setWaitingState(boolean waiting) {

		mFileListView.setClickable(!waiting) ;

		if (mWaitingState != waiting) {

			mWaitingState = waiting ;
			setWaitingIndicator(mWaitingState, mWaitingVisible) ;
		}
	}

	private void setWaitingIndicator(boolean waiting, boolean visible) {

		if (!visible)
			return ;

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
	public void onResume() {

		restoreWaitingIndicator() ;
		if (!isPlackbackMode) {
			Log.e(TAG,"===== not playback mode ==========");
			IPCamProperty.getProperty(pHandler);
		}else
		{
			Log.e(TAG,"===== in playback mode ==========");
		}
			//new EnterPlaybackMode().execute();

		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		//filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		getActivity().registerReceiver(mNetworkReceiver, filter);
		super.onResume() ;
	}

	@Override
	public void onPause() {
		clearWaitingIndicator() ;

		if (sDownloadTask != null) {

			sDownloadTask.hideProgress() ;
		}
		
		new ExitPlaybackMode().execute();
		mFileListAdapter.notifyDataSetChanged() ;
		getActivity().unregisterReceiver(mNetworkReceiver);


		super.onPause() ;
	}

	private class EnterPlaybackMode extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandEnterPlayback() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			if (!isPlackbackMode)
				startDownload();
			isPlackbackMode = true;
			setWaitingState(false) ;
			super.onPostExecute(result) ;
		}
	}		
	
	private void startDownload() {
		Activity activity = getActivity() ;
		if (activity != null) {	
			if (sDownloadTask != null) {
				sDownloadTask.showProgress(getActivity()) ;
			} else {

				try {
					if ((bflistdTask != true)) {
	        			bflistdTask = true;
	        			flistdTask = new DownloadFileListTask();
	        			flistdTask.execute(new FileBrowser(new URL("http://" + mIp + mPath),
							FileBrowser.COUNT_MAX)) ;
	        		}else {
	        			if (flistdTask != null)
	        				flistdTask.cancel(false);
	        			if (cflistdTask != null)
	        				cflistdTask.cancel(false);
	        			
	        			flistdTask = new DownloadFileListTask();
	        			flistdTask.execute(new FileBrowser(new URL("http://" + mIp + mPath),
							FileBrowser.COUNT_MAX)) ;
	        		}
					//new DownloadFileListTask().execute(new FileBrowser(new URL("http://" + mIp + mPath),
					//		FileBrowser.COUNT_MAX)) ;
				} catch (MalformedURLException e) {
					e.printStackTrace() ;
					mSwitchButton.setEnabled(true);
				}
			}
		}
	}
	
	
	private class ExitPlaybackMode extends AsyncTask<URL, Integer, String> {
		@Override
		protected void onPreExecute() {
			setWaitingState(true) ;
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandExitPlayback() ;
			if (url != null) {
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			Activity activity = getActivity() ;
			if (activity != null) {	
			}
			setWaitingState(false) ;
			super.onPostExecute(result) ;
		}
	}

	private Handler pHandler = new Handler() {
		public void handleMessage(Message msg) {
			int idx = 0;
			if (IPCamProperty.FolderType == IPCamProperty.PL_FOLDERTYPE.UNKNOWN) {
				//mFileListTitle.setText(mFileBrowser + " : " + standardDB[0]) ;
				mDirectory = allDB[0];
				folderDB = allDB;
				while (idx < folderDB.length) {
					list.add(folderDB[idx]);
					idx++;
				}
			}else {
				Log.e(TAG, "LIST SIZE=" + IPCamProperty.FolderList.size());
				//mFileListTitle.setText(mFileBrowser + " : " + IPCamProperty.FolderList.get(idx)) ;
				mDirectory = IPCamProperty.FolderList.get(0);
				while (idx < IPCamProperty.FolderList.size()) {
					list.add(IPCamProperty.FolderList.get(idx));
					idx++;
				}
			}
			adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mBrowserFolder.setAdapter(adapter);

			//new EnterPlaybackMode().execute();
			super.handleMessage(msg);
		}
	};

	//sniffer handler
	private Handler sfHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("Browser","Get sniffer message = " + (String)msg.obj);
			Reception((String)msg.obj);
		}
	};

	///check wifi online
	public class WifiState extends BroadcastReceiver {

		String TAG = getClass().getSimpleName();
		private Context mContext;

		@Override
		public void onReceive(Context context, Intent intent) {
			mContext = context;
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
						networkInfo.isConnected()) {
					// Wifi is connected
					Log.e(TAG, " Wifi Connected ");
					new EnterPlaybackMode().execute();
				}
			}
			else if (intent.getAction().equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION))
			{
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
				if (wifiState == WifiManager.WIFI_STATE_DISABLED)
				{
					Log.e(TAG, "Wifi  Lost ");
				}
			}
		}
	}
}
