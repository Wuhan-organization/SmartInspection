package tw.com.a_i_t.IPCamViewer.IPCam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.com.a_i_t.IPCamViewer.HelmetActivity;

public class IPCamWhistlerFileDwonload extends AsyncTask<URL, Long, Boolean> {

	String mFileName ;
	String mLocalFilename;
	String mDisplayFilename;
	Context mContext ;
	WifiLock mWifiLock ;
	String mIp ;
	boolean mCancelled ;
	PowerManager.WakeLock mWakeLock ;
	private ProgressDialog mProgressDialog ;
	Handler mHandler;
	

	IPCamWhistlerFileDwonload(Context context,Handler hld) {
		mContext = context ;
		mHandler = hld;
	}

	@Override
	protected void onPreExecute() {

		Log.i("DownloadTask", "onPreExecute") ;

		WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE) ;
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
		mDisplayFilename = urls[0].toString();
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

			File appDir = HelmetActivity.getAppDir() ;
			File file = new File(appDir, mFileName) ;

			mLocalFilename = appDir.getPath() + File.separator + mFileName;
			Log.i("Path", appDir.getPath() + File.separator + mFileName) ;

			if (file.exists()) {
				file.delete() ;
			}

			file.createNewFile() ;
			FileOutputStream fileOutput = new FileOutputStream(file) ;

			byte[] buffer = new byte[2048] ;
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
				inputStream.close() ;
				fileOutput.close() ;
				//Log.i("DownloadTask", "doInBackground disconnect START") ;
				urlConnection.disconnect() ;
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
			//Log.i("Progress", "Progress " + progress);
			mProgressDialog.setMax((int)max) ;
			mProgressDialog.setProgress((int)progress) ;
			mProgressDialog.setProgressNumberFormat("%1d/%2d " + unit) ;
		}
		super.onProgressUpdate(values) ;
	}

	private void cancelDownload() {
		mCancelled = true ;
	}

	@Override
	protected void onCancelled() {

		Log.i("DownloadTask", "onCancelled") ;

		if (mProgressDialog != null) {
			mProgressDialog.dismiss() ;
			mProgressDialog = null ;
		}
		//			sDownloadTask = null ;

		mWakeLock.release() ;
		mWifiLock.release() ;
		IPCamWhistlerReceiver.resetEid();
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
		//			sDownloadTask = null ;

		mWakeLock.release() ;
		mWifiLock.release() ;

		if (mContext != null) {
			if (!result) {
				Log.e("DOWNLOAD","DOWNLOAD FAILED:"+mDisplayFilename);
				if (!mCancelled) {
					Message message;
					//String obj = "OK";
					message = mHandler.obtainMessage();
					message.obj = mDisplayFilename;
					message.arg1 = IPCamWhistlerEvent.MSG_DOWNLOAD_FAIL;
					mHandler.sendMessage(message);
					
				} else {

				}
				
			} else {
				Log.e("DOWNLOAD","DOWNLOAD OK:" + mDisplayFilename);	
				if (mHandler != null) {
					Message message;
					//String obj = "OK";
					message = mHandler.obtainMessage();
					message.obj = mLocalFilename;
					message.arg1 = IPCamWhistlerEvent.MSG_DOWNLOAD_FINISHED;
					mHandler.sendMessage(message);
				}
			}
/*			if (!mCancelled) {
				if (mHandler != null) {
					Message message;
					//String obj = "OK";
					message = mHandler.obtainMessage();
					message.obj = mLocalFilename;
					message.arg1 = IPCamWhistlerEvent.MSG_DOWNLOAD_FINISHED;
					mHandler.sendMessage(message);
				}
				
			}
*/			
		}

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

