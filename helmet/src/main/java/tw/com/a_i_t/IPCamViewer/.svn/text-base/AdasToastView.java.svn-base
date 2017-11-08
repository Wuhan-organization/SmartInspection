package tw.com.a_i_t.IPCamViewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AdasToastView extends Toast {
	View adasView;
	public AdasToastView(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		adasView = inflater.inflate( R.layout. adas_view, null );	
		this.setView(adasView);
		// TODO Auto-generated constructor stub
	}	
	public void setAdasIcon(int id) {
		ImageView imageView = (ImageView)adasView.findViewById(R.id.adasimage);
		imageView.setImageResource(id);
	}
}
