package tw.com.a_i_t.IPCamViewer.VideoView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import tw.com.a_i_t.IPCamViewer.MainActivity;


public class VideoViewer extends SurfaceView implements IVLCVout.Callback {
    static private String TAG = "VideoViewer";

    public final static int MSG_VIDEOVIEWER_STOP = 1001;
    public final static int MSG_VIDEOVIEWER_NEWLAYOUT = 1002;
    public final static int MSG_VIDEOVIEWER_START = 1003;

    private SurfaceHolder surfaceHolder;
    private LibVLC libvlc;
    private static MediaPlayer mMediaPlayer = null;
    private Context mContext;

    private int mVideoWidth;
    private int mVideoHeight;

    private int mVideoVisibleWidth;
    private int mVideoVisibleHeight;
    private int mSarNum ;
    private int mSarDen ;

    public int layoutW ;
    public int layoutH ;

    private static Handler mHandler;
    private static Media mMedia = null;

    public VideoViewer(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public VideoViewer(Context context,
                       AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public VideoViewer(Context context,
                       AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext()) ;
        String chroma = pref.getString("chroma_format", "") ;
        if (chroma.equals("YV12")) {
            surfaceHolder.setFormat(ImageFormat.YV12) ;
        } else if (chroma.equals("RV16")) {
            surfaceHolder.setFormat(PixelFormat.RGB_565) ;
            PixelFormat info = new PixelFormat() ;
            PixelFormat.getPixelFormatInfo(PixelFormat.RGB_565, info) ;
        } else {
            surfaceHolder.setFormat(PixelFormat.RGBX_8888) ;
            PixelFormat info = new PixelFormat() ;
            PixelFormat.getPixelFormatInfo(PixelFormat.RGBX_8888, info) ;
        }
    }

    public void setHandler(Handler hld){
        mHandler = hld;
    }

    @Override
    public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (width * height == 0)
            return;

        // store video size
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mSarNum = sarNum;
        mSarDen = sarDen;

        setSize(mVideoWidth, mVideoHeight);
        //setSize(1280, 720);
    }

    @Override
    public void onSurfacesCreated(IVLCVout ivlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout ivlcVout) {

    }

    @Override
    public void onHardwareAccelerationError(IVLCVout ivlcVout) {

    }

    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if(surfaceHolder == null || this == null)
            return;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dw = size.x;
        int dh = size.y;
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ;
        if (dw > dh && isPortrait || dw < dh && !isPortrait) {
            int d = dw ;
            dw = dh ;
            dh = d ;
        }

        // sanity check
        if (dw * dh == 0 || mVideoWidth * mVideoHeight == 0) {
            Log.e(TAG, "Invalid surface size") ;
            return ;
        }

        // compute the aspect ratio
        double ar, vw ;
        double density = (double) mSarNum / (double) mSarDen ;
        if (density == 1.0) {
			/* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth ;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight ;
        } else {
			/* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * density ;
            ar = vw / mVideoVisibleHeight ;
        }

        // compute the display aspect ratio
        double dar = (double) dw / (double) dh ;

//        switch (mCurrentSize) {
//            case SURFACE_BEST_FIT:
//                if (dar < ar)
//                    dh = (int) (dw / ar) ;
//                else
//                    dw = (int) (dh * ar) ;
//                break ;
//            case SURFACE_FIT_HORIZONTAL:
//                dh = (int) (dw / ar) ;
//                break ;
//            case SURFACE_FIT_VERTICAL:
//                dw = (int) (dh * ar) ;
//                break ;
//            case SURFACE_FILL:
//                break ;
//            case SURFACE_16_9:
                ar = 16.0 / 9.0 ;
                if (dar < ar)
                    dh = (int) (dw / ar) ;
                else
                    dw = (int) (dh * ar) ;
//                break ;
//            case SURFACE_4_3:
//                ar = 4.0 / 3.0 ;
//                if (dar < ar)
//                    dh = (int) (dw / ar) ;
//                else
//                    dw = (int) (dh * ar) ;
//                break ;
//            case SURFACE_ORIGINAL:
//                dh = mVideoVisibleHeight ;
//                dw = (int) vw ;
//                break ;
//        }

        // force surface buffer size
        surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight) ;

        // set display size
        ViewGroup.LayoutParams lp = this.getLayoutParams() ;
        lp.width = dw * mVideoWidth / mVideoVisibleWidth ;
        lp.height = dh * mVideoHeight / mVideoVisibleHeight;
        this.setLayoutParams(lp) ;

        layoutW = dw;
        layoutH = dh;
        // set frame size (crop if necessary)
//        lp = mSurfaceFrame.getLayoutParams() ;
//        lp.width = dw ;
//        lp.height = dh ;
//        mSurfaceFrame.setLayoutParams(lp) ;

        this.invalidate() ;

        Message message;
        //String obj = "OK";
        message = mHandler.obtainMessage();
        message.arg1 = MSG_VIDEOVIEWER_NEWLAYOUT;
        mHandler.sendMessage(message);
        /*
        // get screen size
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int w = size.x;
        int h = size.y;

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = w;
        lp.height = h;
        Log.e("02 3345678 ","LP width= " +  lp.width +  ",LP height=" + lp.height);
        this.setLayoutParams(lp);
        this.invalidate();
        */
    }
/*
    public void startPlay(String media)
    {
        Uri uri = Uri.parse(media);
        Media m = new Media(libvlc, uri);
        m.setHWDecoderEnabled(false, false);
        //m.addOption(":network-caching=2000");
        mMediaPlayer.setMedia(m);
        mMediaPlayer.play();
        surfaceHolder = getHolder();
        Canvas canvas = surfaceHolder.lockCanvas();
        //Clear surface by drawing on it
        canvas.drawColor(Color.BLACK);
        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    public void startLocalPlay(String media)
    {
        Media m = new Media(libvlc, media);
        m.setHWDecoderEnabled(false, false);
        //m.addOption(":network-caching=2000");
        mMediaPlayer.setMedia(m);
        mMediaPlayer.play();
        surfaceHolder = getHolder();
        Canvas canvas = surfaceHolder.lockCanvas();
        //Clear surface by drawing on it
        canvas.drawColor(Color.BLACK);
        surfaceHolder.unlockCanvasAndPost(canvas);

    }
*/
//    public void stopPlay()
//    {
//        mMediaPlayer.stop();
//    }

    public void createPlayer(String media,boolean rtsp,int nc) {
        releasePlayer();
        Uri uri;
        Media m;
        int isTcp;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( MainActivity.getAppContext());
        isTcp = preferences.getInt("_rtsp_tcp", 0);
        try {
            if (media.length() > 0) {
//                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
//                        0);
//                toast.show();
            }
            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            //options.add("--no-audio");
            options.add("--network-caching="+nc);
            options.add("--clock-jitter="+nc);
            if (isTcp == 1)
            options.add("--rtsp-tcp");

            //options.add("--enable-iomx");
            options.add("-vvv"); // verbosity

            libvlc = new LibVLC(options);
            //    libvlc.setOnHardwareAccelerationError(this);
            surfaceHolder = getHolder();
            surfaceHolder.setKeepScreenOn(true);

            ///clean screen
            Canvas canvas = surfaceHolder.lockCanvas();
            //Clear surface by drawing on it
            canvas.drawColor(Color.BLACK);
            surfaceHolder.unlockCanvasAndPost(canvas);

            // Create media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Set up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(this);
            //vout.setSubtitlesView(mSurfaceSubtitles);
            vout.addCallback(this);
            vout.attachViews();
            if (rtsp) {
                uri = Uri.parse(media);
                m = new Media(libvlc, uri);
            }
            else
                m = new Media(libvlc, media);
            m.setHWDecoderEnabled(false, false);
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
            mMedia = m;

        } catch (Exception e) {
            //releasePlayer();
            Toast.makeText(MainActivity.getAppContext(), "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    public void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        //vout.removeCallback(this);
        vout.detachViews();
        surfaceHolder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    public static boolean isPlaying() {
        if (mMediaPlayer == null)
            return false;
        return mMediaPlayer.isPlaying();
    }
    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<VideoViewer> mOwner;

        public MyPlayerListener(VideoViewer owner) {
            mOwner = new WeakReference<VideoViewer>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            VideoViewer player = mOwner.get();

            switch(event.type) {
                case MediaPlayer.Event.EndReached:
                    Log.w(TAG, "MediaPlayer EndReached");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                    Log.e(TAG, "MediaPlayer Playing");
                    if (mHandler != null) {
                        Log.e(TAG, "MediaPlayer Playing 1");
                        Message message;
                        //String obj = "OK";
                        message = mHandler.obtainMessage();
                        message.arg1 = MSG_VIDEOVIEWER_START;
                        mHandler.sendMessage(message);
                    }
                    break;
                case MediaPlayer.Event.Buffering:
                    //Log.e(TAG, "MediaPlayer Buffering:" + mMediaPlayer.getPosition());
                    Log.e(TAG, "MediaPlayer Buffering:" + event.getBuffering());

                    break;
                case MediaPlayer.Event.Paused:
                    break;
                case MediaPlayer.Event.Stopped:
                    Log.w(TAG, "MediaPlayer STOP");

                    if (mHandler != null) {
                        Message message;
                        //String obj = "OK";
                        message = mHandler.obtainMessage();
                        message.arg1 = MSG_VIDEOVIEWER_STOP;
                        mHandler.sendMessage(message);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    //Qos
    public static int getBitrate()
    {
        if ((mMedia != null) && (isPlaying())) {
            //Log.e(TAG,"bbb=" + mMedia.getBitrate()*8000);
            return (int)(mMedia.getBitrate()*8000);
        }
        return 0;
    }

    public static int getLostP()
    {
        if ((mMedia != null) && (isPlaying())) {
            //Log.e(TAG,"bbb=" + mMedia.getBitrate()*8000);
            return (int)(mMedia.getLostPictures());
        }
        return 0;
    }
}