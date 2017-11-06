package tw.com.a_i_t.IPCamViewer.FileUtility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tw.com.a_i_t.IPCamViewer.MainActivity;
import tw.com.a_i_t.IPCamViewer.R;

/**
 * Created by chrison.feng on 2016/8/18.
 */
public class NetFileDownload {
    public static final int NET_DOWNLOAD_STATR =     0;
    public final int NET_DOWNLOAD_CANCEL =    1;
    public static final int NET_DOWNLOAD_FAILED =     2;
    public  static final int NET_DOWNLOAD_FINISHED =  3;

    private Handler mHandler;
    private Context mContext;
    private NetFileDownloadTask mDTask;
    //String mPath;

    public NetFileDownload(Activity context){
        mContext = context;
    }

    public void startDownload(String path){
        //mHandler = hld;
        //mPath = path;
        mDTask = new NetFileDownloadTask(mContext);//,hld);
        try {
            mDTask.execute(new URL(path));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class NetFileDownloadTask extends AsyncTask<URL, Long, Boolean> {

        String mFileName ;
        Context mContext ;
        WifiManager.WifiLock mWifiLock ;
        String mIp ;
        boolean mCancelled ;
        PowerManager.WakeLock mWakeLock ;
        //Handler mhld;

        private ProgressDialog mProgressDialog = null ;

        public NetFileDownloadTask(Context context/*,Handler hld*/) {
            mContext = context ;
            //mhld = hld;
        }

        @Override
        protected void onPreExecute() {

            Log.e("DownloadTask", "onPreExecute") ;

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
                            inputStream.close() ;
                            fileOutput.close() ;
                            urlConnection.disconnect();
                            break ;
                        }
                    }
                } finally {
                    inputStream.close() ;
                    fileOutput.close() ;
                    urlConnection.disconnect() ;
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
        }

        @Override
        protected void onCancelled() {

            Log.i("DownloadTask", "onCancelled") ;

            if (mProgressDialog != null) {
                mProgressDialog.dismiss() ;
                mProgressDialog = null ;
            }

            mWakeLock.release() ;
            mWifiLock.release() ;

            super.onCancelled() ;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Log.i("DownloadTask", "onPostExecute " + mFileName + " " + (mCancelled ? "CANCELLED"
                    : result ? "SUCCESS" : "FAIL")) ;

            if (mProgressDialog != null) {
                Log.e("DownloadTask", "onPostExecute close dialog") ;
                mProgressDialog.dismiss() ;
                mProgressDialog = null ;
            }

            mWakeLock.release() ;
            mWifiLock.release() ;

            if (mContext != null) {
                if (!result) {
                    if (!mCancelled) {
                        if (mContext != null)
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.label_dowload_failed),
                                    Toast.LENGTH_LONG).show() ;
                    } else {
                        if (mContext != null)
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.label_dowload_cancel),
                                    Toast.LENGTH_LONG).show() ;
                    }
                } else {
                    if (mContext != null)
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.label_dowload_success),
                            Toast.LENGTH_LONG).show() ;
                }
            }
            super.onPostExecute(result) ;
        }

        void showProgress(Context context) {
            mContext = context ;

            if (mProgressDialog != null && mProgressDialog.isShowing()) {

                Log.e("DownloadTask", "close") ;
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
}
