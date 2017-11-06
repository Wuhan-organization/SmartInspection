package tw.com.a_i_t.IPCamViewer.Property;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.com.a_i_t.IPCamViewer.CameraCommand;
import tw.com.a_i_t.IPCamViewer.CameraSniffer;
import tw.com.a_i_t.IPCamViewer.FunctionListFragment;

/**
 * Created by chrison.feng on 2016/7/15.
 */

public class IPCamProperty {
    static String PropertyTAG;
    static Handler pHandler;
    enum PL_STATUS {UNKNOWN ,READY, NONE};
    private static PL_STATUS plState = PL_STATUS.UNKNOWN;

    //public property for query
    public enum PL_CAMTYPE {UNKNOWN ,CARCAM, ATIONCAM};
    public static PL_CAMTYPE CamType = PL_CAMTYPE.UNKNOWN;
    /* DATETIME: save to DCIM folder, STANDARD: save to Normal,Photo,Event,Park */
    public enum PL_FOLDERTYPE {UNKNOWN ,READY};
    public static PL_FOLDERTYPE FolderType = PL_FOLDERTYPE.UNKNOWN;

    public static List<String> FolderList = new ArrayList<String>();
    //private List<String> list = new ArrayList<String>();
    public static void init() {
        pHandler = null;
        plState = PL_STATUS.UNKNOWN;
        FolderList.clear();

    }

    public static void getProperty(Handler hld) {
        pHandler = hld;

        if (plState == PL_STATUS.UNKNOWN) {
            Log.e(PropertyTAG, "Need download xml");
            new GetCameraMenu_Property().execute();
        }else {
            if (pHandler != null)
                pHandler.sendMessage(pHandler.obtainMessage());
        }

    /*
        else
            if (pHandler != null) {
                Log.e(PropertyTAG, "No need re-download xml");
                pHandler.sendMessage(pHandler.obtainMessage());
            }
            */
    }

    ///Property Paser
    private static void parseProperty(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "utf-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d(PropertyTAG,"tag = "+parser.getName());
                        if ("camtype".equals(parser.getName())) {
                            if ("CarCam".equals(parser.nextText()))
                                CamType = PL_CAMTYPE.CARCAM;
                            else if ("ActionCam".equals(parser.nextText()))
                                CamType = PL_CAMTYPE.ATIONCAM;
                        }else if ("filefolder".equals(parser.getName())) {
                                FolderType = PL_FOLDERTYPE.READY;
                        }else if ("folder".equals(parser.getName())) {
                            FolderList.add(parser.nextText());
                        }
                        break;
                    }
                    event = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }  finally {
            in.close();
        }
    }
    /////task
    private static class GetCameraMenu_Property extends AsyncTask<URL, Integer, String> {
        protected void onPreExecute() {
            super.onPreExecute() ;
        }
        @Override
        protected String doInBackground(URL... params) {
            URL url = null ;
            try {
                CameraSniffer sniffer = FunctionListFragment.GetCameraSniffer();
                String ip = sniffer.getCameraIp();
                if (ip != null)
                    url = new URL("http://" + ip  + "/cgi-bin/Config.cgi?action=get&property=Camera.Menu.Property");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url != null) {
                return CameraCommand.sendRequest(url) ;
            }
            return null ;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                InputStream in = null;
                try {
                    in = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    if (result.length() > 0) {
                        if (result.contains("703")) {
                            Log.e(PropertyTAG, "No property xml");
                            plState = PL_STATUS.NONE;
                        }else {
                            parseProperty(in);
                            //UpdateMenu();
                            Log.e(PropertyTAG, "Get property xml finished");
                            plState = PL_STATUS.READY;
                        }
                    } else {
                        Log.e(PropertyTAG,"No property xml");
                        plState = PL_STATUS.NONE;
                    }
                    //in.close();
                } catch (IOException e) {
                    return;
                } catch (XmlPullParserException e) {
                    return;
                }
            } else {
                Log.e(PropertyTAG,"No property data");
                plState = PL_STATUS.NONE;
            }
            if (pHandler != null)
                pHandler.sendMessage(pHandler.obtainMessage());
            super.onPostExecute(result) ;
        }
    }
}

