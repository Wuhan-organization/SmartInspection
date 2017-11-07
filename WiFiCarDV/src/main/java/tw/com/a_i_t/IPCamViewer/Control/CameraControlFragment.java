package tw.com.a_i_t.IPCamViewer.Control ;

import java.net.URL;

import tw.com.a_i_t.IPCamViewer.CameraCommand;
import tw.com.a_i_t.IPCamViewer.FunctionListFragment;
import tw.com.a_i_t.IPCamViewer.MainActivity;
import tw.com.a_i_t.IPCamViewer.R ;

import android.app.Fragment ;
import android.os.AsyncTask;
import android.os.Bundle ;
import android.view.LayoutInflater ;
import android.view.MotionEvent ;
import android.view.View ;
import android.view.ViewGroup ;
import android.view.View.OnClickListener ;
import android.view.View.OnTouchListener ;
import android.widget.RelativeLayout ;

public class CameraControlFragment extends Fragment {

	/* Query property of  Net.WIFI_STA.AP.Switch*/
	/* if Net.WIFI_STA.AP.Switch = ENABLE => show STA setting Menu */
	private class check_WiFi_sw_en extends AsyncTask<URL, Integer, String> {
		protected void onPreExecute() {
			
			super.onPreExecute() ;
		}
		@Override
		protected String doInBackground(URL... params) {
			URL url = CameraCommand.commandHotSpotcheckUrl() ;
			if (url != null) {		
				return CameraCommand.sendRequest(url) ;
			}
			return null ;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result) ;
			if (result != null) {
				String err=result.substring(0, 3);
				if (err.equals("703")){
					FunctionListFragment.SetCameraSWModeEn(false);
				}else {
					String[] lines;	
					String[] lines_temp = result.split("Net.WIFI_STA.AP.Switch=");
					
					if ((lines_temp[1] != null)&&(lines_temp[0] != null)) {
						lines = lines_temp[1].split(System.getProperty("line.separator")) ;
						
						if (lines[0].equals("ENABLE"))
							FunctionListFragment.SetCameraSWModeEn(true);
						else
							FunctionListFragment.SetCameraSWModeEn(false);
					}else
						FunctionListFragment.SetCameraSWModeEn(false);
				}
			}else {
				FunctionListFragment.SetCameraSWModeEn(false);
			}
			MainActivity.addFragment(CameraControlFragment.this, new NetworkConfigurationsFragment()) ;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.camera_control, container, false) ;

		OnTouchListener onTouch = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.selected_background) ;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					v.setBackgroundResource(R.drawable.group_background) ;
				}
				return false ;
			}
		} ;

		RelativeLayout networkConfigurations = (RelativeLayout) view
				.findViewById(R.id.cameraControlNetworkConfigurations) ;

		networkConfigurations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new check_WiFi_sw_en().execute();		
				//MainActivity.addFragment(CameraControlFragment.this, new NetworkConfigurationsFragment()) ;
			}
		}) ;

		networkConfigurations.setOnTouchListener(onTouch) ;

		RelativeLayout cameraSettings = (RelativeLayout) view.findViewById(R.id.cameraControlCameraSettings) ;

		cameraSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.addFragment(CameraControlFragment.this, new CameraSettingsFragment()) ;
			}
		}) ;

		cameraSettings.setOnTouchListener(onTouch) ;

		return view ;
	}

}
