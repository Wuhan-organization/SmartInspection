package tw.com.a_i_t.IPCamViewer.FileBrowser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList ;
import java.util.LinkedList;
import java.util.List;

import tw.com.a_i_t.IPCamViewer.CameraCommand;
import tw.com.a_i_t.IPCamViewer.R ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.Model.FileNode ;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater ;
import android.view.View ;
import android.view.ViewGroup ;
import android.widget.BaseAdapter ;
import android.widget.CheckedTextView ;
import android.widget.ImageView ;
import android.widget.TextView ;

public class FileListAdapter extends BaseAdapter {

	private LayoutInflater mInflater ;
	private ArrayList<FileNode> mFileList ;

	public FileListAdapter(LayoutInflater inflater, ArrayList<FileNode> fileList) {

		mInflater = inflater ;
		mFileList = fileList ;
	}

	@Override
	public int getCount() {

		return mFileList == null ? 0 : mFileList.size() ;
	}

	@Override
	public Object getItem(int position) {

		return mFileList == null ? null : mFileList.get(position) ;
	}

	@Override
	public long getItemId(int position) {

		return position ;
	}
	
	@Override
	public void notifyDataSetChanged() {
		Log.d("notifyDataSetChanged","notifyDataSetChanged");
		for (DownloadthumbnailTask task : thumbnailTaskList) {
			task.cancel(false);
		}
		thumbnailTaskList.clear();
		super.notifyDataSetChanged();
	}
	
	private List<DownloadthumbnailTask> thumbnailTaskList = new LinkedList<DownloadthumbnailTask>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewTag viewTag ;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.filelist_row, null) ;
			viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.fileListThumbnail),
					(TextView) convertView.findViewById(R.id.fileListName),
					(TextView) convertView.findViewById(R.id.fileListTime),
					(TextView) convertView.findViewById(R.id.fileListSize), 
					mFileList.get(position),
					(CheckedTextView) convertView.findViewById(R.id.fileListCheckBox)) ;
			convertView.setTag(viewTag) ;

		} else {
			viewTag = (ViewTag) convertView.getTag() ;
			viewTag.mThumbnail.setImageResource(R.drawable.type_all); 
			if (viewTag.mThumbnailTask != null) {
				viewTag.mThumbnailTask.cancel(false);
				viewTag.mThumbnailTask = null;
			}
		}
		
		//viewTag.mPos = position; 
		viewTag.mFileNode = mFileList.get(position) ;
		String filename = viewTag.mFileNode.mName.substring(viewTag.mFileNode.mName.lastIndexOf("/") + 1) ;
		viewTag.mFilename.setText(filename) ;
		viewTag.mTime.setText(viewTag.mFileNode.mTime) ;
		viewTag.setSize(viewTag.mFileNode.mSize) ;
		viewTag.mCheckBox.setChecked(viewTag.mFileNode.mSelected) ;
		
		if (viewTag.mFileNode.mTmb == null) {
				viewTag.mThumbnailTask = new DownloadthumbnailTask();
				viewTag.mThumbnailTask.execute(viewTag);
		}else {
			viewTag.mThumbnail.setImageBitmap(viewTag.mFileNode.mTmb);
		}

		return convertView ;
	}

	public class DownloadthumbnailTask extends AsyncTask<ViewTag, Integer, Bitmap> {
		///first go
		ViewTag thumbview;
		HttpURLConnection conn = null;
		InputStream input = null;
		@Override
		protected void onPreExecute() {
			thumbnailTaskList.add(this);
			super.onPreExecute() ;
		}
		@Override
		protected Bitmap doInBackground(ViewTag... params ) {
			URL url = null;
			int count;

			thumbview = params[0];
			try {
				String path=params[0].mFileNode.mName;
				if (path.indexOf("SD/") < 0) {//action do not has "SD/", CarDV has "SD/" in path 
					url = new URL("http://" + CameraCommand.getCameraIp() + "/thumb" 
							+ path);
				}else
					url = new URL("http://" + CameraCommand.getCameraIp() + "/thumb" 
							+ params[0].mFileNode.mName.substring(params[0].mFileNode.mName.lastIndexOf("SD") + 2));

				conn = (HttpURLConnection)url.openConnection() ;
				conn.setUseCaches(false);
				conn.setDoInput(true);
				conn.setConnectTimeout(6000);
				conn.setReadTimeout(6000);
				conn.connect();

				input = new BufferedInputStream(/*url.openStream()*/conn.getInputStream(),10*1024);

				byte data_recv[] = new byte[1024];
				byte data_out[] = new byte[80*1024];

				int total = 0;
				while ((count = input.read(data_recv)) != -1) {
					System.arraycopy(data_recv,0,data_out,total,count);
					total += count;
				}

				input.close();
				conn.disconnect();
				Bitmap bm =BitmapFactory.decodeByteArray(data_out, 0, total);
				return bm;

			} catch (Exception e) {
				Log.d("Error: ", "get thumbnail exception!!!!!!!");
			}
			try {
				if (input!= null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn!=null)
				conn.disconnect();
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result) ;
			if (result != null) {
				thumbview.mFileNode.mTmb = result.copy(result.getConfig(), true);
				//thumbview.mFileNode.mTmb = result;
				thumbview.mThumbnail.setImageBitmap(thumbview.mFileNode.mTmb);
			}else {
				Log.d("getThumbnail","getThumbnail = NULL");
				thumbview.mFileNode.mTmb = null;
			}
			thumbnailTaskList.remove(this);
			thumbview.mThumbnailTask = null;
		}
		@Override
		protected void onCancelled(Bitmap result) {

			Log.d("getThumbnail","ONCANCEL");
			try {
				if (input!= null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (conn!=null)
				conn.disconnect();
			onCancelled();
		}  
	}
}
