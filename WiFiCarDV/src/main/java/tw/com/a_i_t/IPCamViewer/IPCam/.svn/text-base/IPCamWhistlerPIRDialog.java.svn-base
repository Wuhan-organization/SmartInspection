package tw.com.a_i_t.IPCamViewer.IPCam;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import tw.com.a_i_t.IPCamViewer.R;

/**
 * Created by chrison.feng on 2016/5/31.
 */
public class IPCamWhistlerPIRDialog extends Dialog {

    View pirView;
    Button byes;
    Button bno;
    boolean isDownloaded = false;
    boolean isVideoEvt = false;


    public IPCamWhistlerPIRDialog(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        pirView = inflater.inflate( R.layout. image_dialog, null );
        byes = (Button)pirView.findViewById(R.id.dbbtnyes);
        bno = (Button)pirView.findViewById(R.id.dbbtnno);

        this.setContentView(pirView);

    }

    public void setPirImg(String path) {
        ImageView imageView = (ImageView)pirView.findViewById(R.id.dbpicture);
        File imgFile = new  File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
            imageView.setImageBitmap(myBitmap);
        }

    }

    public void setPirText(String text) {
        TextView txtView = (TextView)pirView.findViewById(R.id.dbtext);
        txtView.setText(text);

    }



}
