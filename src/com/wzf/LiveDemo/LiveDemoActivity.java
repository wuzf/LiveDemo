package com.wzf.LiveDemo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.net.Uri;
import java.io.BufferedReader;    
import java.io.File;    
import java.io.FileInputStream;    
import java.io.InputStreamReader; 



public class LiveDemoActivity extends Activity implements
	    OnBufferingUpdateListener, OnCompletionListener,
	    OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {
	
	private static final String TAG = "LiveDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private Integer currentid = 1;
	//private String cctv13 = "http://v.iseeyoo.cn/video/2012/01/26/3863b5e6-4801-11e1-89a1-001e0bd5b3ca_041832_001.mp4";
	private String cctv13 ="rtsp://58.241.134.33:554/301";//"rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"; //
	private String cctv6  ="http://112.111.143.137:80/30285NfWL5jwG------Y-5cWss.5l5m5mXHJkkJ0-XJ0k-5lH--05mwG------Y-51kr0Y5c5c0X--5N5mfWL5j5Nvf5j-5cL5cL5N5mvf5j5N.I.o5jRY-0Y5cg5c0X--5c-5N5m.I.o5j5NpoF5jXHJkkJ0-XJ00g5lH-5cXHJkkJ0-XJ00r5lH-H-5c-5N5mpoF5j5NxdoB5jSewTTu5w-XHkX00RH-0r5cXHJkX0J0RHJ0r5N5mxdoB5j5NK8z5jvBovIp5lRU5cdfdK5lk0HJkJ0RrJ0Xr5lH-5c.sQ.o5l5cds5lDCF5csA5l0RggRg-HH05cZK5lXHJkkJ0-XJ00H5N5mK8z5j/temp.flv?start=1344294000000&end=1344301200000&key=1344340908000";
	private String cctv4 ="http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323931387C317C343030307C434354562D347C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323931385B2F74735DVSDNSOOONERCOM00";
	private String cctv3 ="http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323931377C317C343030307C434354562D337C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323931375B2F74735DVSDNSOOONERCOM00";
	private String cctv2 ="http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323931367C317C343030307C434354562D327C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323931365B2F74735DVSDNSOOONERCOM00";
	private String cctv1 ="http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323931357C317C343030307C434354562D317C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323931355B2F74735DVSDNSOOONERCOM00";
	private Bundle extras;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	
		/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.mediaplayer_2);
	    mPreview = (SurfaceView) findViewById(R.id.surface);
	    holder = mPreview.getHolder();
	    holder.addCallback(this);
	    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    Log.d(TAG,"SDK Version: "+Build.VERSION.SDK_INT);
	    //extras = getIntent().getExtras();
	}
	
	private void playVideo(Integer Media,Integer pathid) {
	    doCleanUp();
	    Log.d(TAG,"\n playVideo: Media"+Media+"pathid"+pathid);
	    try {
	
	        switch (Media) {
	            case LOCAL_VIDEO:  // 4
	                /*
	                 * TODO: Set the path variable to a local media file path.
	                 */
	                //path = "/mnt/flash/d1.AVI";
	                //path = "/mnt/sda1/1080.avi";
	                path = "/mnt/sdcard/test1.mp4";
	                if (path == "") {
	                    // Tell the user to provide a media file URL.
	                    Toast
	                            .makeText(
	                            		LiveDemoActivity.this,
	                                    "Please edit MediaPlayerDemo_Video Activity, "
	                                            + "and set the path variable to your media file path."
	                                            + " Your media file must be stored on sdcard.",
	                                    Toast.LENGTH_LONG).show();
	
	                }
	                break;
	            case STREAM_VIDEO: // 5
	                /*
	                 * TODO: Set path variable to progressive streamable mp4 or
	                 * 3gpp format URL. Http protocol should be used.
	                 * Mediaplayer can only play "progressive streamable
	                 * contents" which basically means: 1. the movie atom has to
	                 * precede all the media data atoms. 2. The clip has to be
	                 * reasonably interleaved.
	                 * 
	                 */
	                //path = "rtsp://58.241.134.33:554/11";

	                	switch(pathid)
	                	{
	                	case 13:path =cctv13;break;
	                	case 6:
														path=readtxt("/mnt/sda/sda1/1.txt");
														if((path == "")||(path==null))
														{
	                						path =cctv6;
	                					}
	                					break;
	                	case 4:path =cctv4;break;
	                	case 3:path =cctv3;break;
	                	case 2:path =cctv2;break;
	                	case 1:path =cctv1;break;
	                	default:path = "http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323932357C317C343030307C434354562D31337C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323932355B2F74735DVSDNSOOONERCOM00";break;
	                	}


	                if (path == "") {
	                    // Tell the user to provide a media file URL.
	                    Toast
	                            .makeText(
	                            		LiveDemoActivity.this,
	                                    "Please edit MediaPlayerDemo_Video Activity,"
	                                            + " and set the path variable to your media file URL.",
	                                    Toast.LENGTH_LONG).show();
	
	                }
	
	                break;
	
	
	        }
	    	                    Toast
	                            .makeText(
	                            		LiveDemoActivity.this,
	                                    "current patch:"+path,
	                                    Toast.LENGTH_LONG).show();
			Log.d(TAG,"path="+path);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDisplay(holder);
//	        mMediaPlayer.setDataSource(path);
	        mMediaPlayer.setDataSource(this, Uri.parse(path));   
			mMediaPlayer.setOnBufferingUpdateListener(this);
		    mMediaPlayer.setOnCompletionListener(this);
		    mMediaPlayer.setOnPreparedListener(this);
		    mMediaPlayer.setOnVideoSizeChangedListener(this);
		    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	        mMediaPlayer.prepare();	
	    } catch (Exception e) {
	        Log.e(TAG, "error: " + e.getMessage(), e);
	    }
	}
	
	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
	    Log.d(TAG, "onBufferingUpdate percent:" + percent);
	
	}
	
	public void onCompletion(MediaPlayer arg0) {
	    Log.d(TAG, "onCompletion called");
	}
	
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	    Log.v(TAG, "onVideoSizeChanged called");
	    if (width == 0 || height == 0) {
	        Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
	        return;
	    }
	    mIsVideoSizeKnown = true;
	    mVideoWidth = width;
	    mVideoHeight = height;
	    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
	        startVideoPlayback();
	    }
	}
	
	public void onPrepared(MediaPlayer mediaplayer) {
	    Log.d(TAG, "onPrepared called");
	    mIsVideoReadyToBePlayed = true;
	    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
	        startVideoPlayback();
	    }
	}
	
	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
	    Log.d(TAG, "surfaceChanged called");
	
	}
	
	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
	    Log.d(TAG, "surfaceDestroyed called");
	}
	
	
	public void surfaceCreated(SurfaceHolder holder) {
	    Log.d(TAG, "surfaceCreated called");
	    //playVideo(extras.getInt(MEDIA));
	    playVideo(5,13);
	
	
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    releaseMediaPlayer();
	    doCleanUp();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    releaseMediaPlayer();
	    doCleanUp();
	}
	
	private void releaseMediaPlayer() {
	    if (mMediaPlayer != null) {
	        mMediaPlayer.release();
	        mMediaPlayer = null;
	    }
	}
	
	private void doCleanUp() {
	    mVideoWidth = 0;
	    mVideoHeight = 0;
	    mIsVideoReadyToBePlayed = false;
	    mIsVideoSizeKnown = false;
	}
	
	private void startVideoPlayback() {
	    Log.v(TAG, "startVideoPlayback");
	    holder.setFixedSize(mVideoWidth, mVideoHeight);
	    mMediaPlayer.start();
	    
	}
	
	public void resetvideo()
	{
			    	//mMediaPlayer.stop();
	    		    //releaseMediaPlayer();
	    		    mMediaPlayer.reset();
	    		    if(13==currentid)
	    		       	currentid=1;	    		    	
	    		    else if(1==currentid)
	    		       	currentid=2;
	    		    else if(2==currentid)
	    		       	currentid=3;
	    		    else if(3==currentid)
	    		       	currentid=4;
	    		    else if(4==currentid)
	    		       	currentid=6;
	    		    else 
	    		       	currentid=13;
	    		    //playVideo(5,currentid);	
	    		    
	    		    switch(currentid)
	                	{
	                	case 13:path =cctv13;break;
	                	case 6:path =cctv6;break;
	                	case 4:path =cctv4;break;
	                	case 3:path =cctv3;break;
	                	case 2:path =cctv2;break;
	                	case 1:path =cctv1;break;
	                	default:path = "http://fjwxbiz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303036307C323932357C317C343030307C434354562D31337C687474705B2F63686E5D5B74735D317C687474703A2F2F35382E32322E3130352E3132303A383030312F445830303030303036305F323932355B2F74735DVSDNSOOONERCOM00";break;
	                	}
	                try
	                {
		                mMediaPlayer.setDataSource(path);
		                mMediaPlayer.prepare();	
		               	if (path == "") 
		               	{
	                    // Tell the user to provide a media file URL.
	                    Toast
	                            .makeText(
	                            		LiveDemoActivity.this,
	                                    "current patch:"+path,
	                                    Toast.LENGTH_LONG).show();
	                }
	                
	            	}catch (Exception e) 
	            	{
	        			Log.e(TAG, "error: " + e.getMessage(), e);
	    			}
	}
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

			if((keyCode == KeyEvent.KEYCODE_MEDIA_NEXT)||(keyCode == KeyEvent.KEYCODE_DPAD_DOWN )||(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ))
			{
				resetvideo();
	    }
	    else if((keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)||(keyCode == KeyEvent.KEYCODE_DPAD_CENTER ))
	    {
	    	if(mMediaPlayer.isPlaying())
	    		mMediaPlayer.pause();
	    	else
	    		mMediaPlayer.start();
	    }
	    else
	        	return super.onKeyDown(keyCode, msg);
    	return (true);
    }
    
  public String readtxt(String filename) {  
  	String lineTXT = null;   
    try {    
        String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )    
        File file = new File(filename);    // "c:/aa.txt"
        if (file.isFile() && file.exists()) {    
            InputStreamReader read = new InputStreamReader(    
                    new FileInputStream(file), encoding);    
            BufferedReader bufferedReader = new BufferedReader(read);    
              
            //while ((lineTXT = bufferedReader.readLine()) != null) {    
            //     System.out.println(lineTXT.toString().trim());    
            //}   
            lineTXT = bufferedReader.readLine();
            read.close(); 
        }else{    
            System.out.println("找不到指定的文件！");    
        }    
    } catch (Exception e) {    
        System.out.println("读取文件内容操作出错");    
        e.printStackTrace();    
    }  
    return lineTXT;     
	}      
}
