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
	private Integer currentid = 0;
	//private String channel13 = "http://v.iseeyoo.cn/video/2012/01/26/3863b5e6-4801-11e1-89a1-001e0bd5b3ca_041832_001.mp4";
	private String channel6 ="rtsp://58.241.134.33:554/301";//"rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"; //
	private String channel5  ="rtsp://116.199.127.68/guoji";
	private String channel4 ="rtsp://116.199.127.68/huayu";
	private String channel3 ="rtsp://116.199.127.68/bengang";
	private String channel2 ="rtsp://116.199.127.68/fenghuang";
	private String channel1 ="rtsp://116.199.127.68/gztv_sport";
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


				path=readtxt(String.valueOf(pathid));//

				if((path == "")||(path==null))
				{
	                		path =channel1;
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
	    playVideo(STREAM_VIDEO,currentid);
	
	
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
	
	public void resetvideo(int key)
	{
			    	//mMediaPlayer.stop();
	    		    //releaseMediaPlayer();
	    		    mMediaPlayer.reset();
		int maxid=0;
		try{ 
			maxid=Integer.parseInt(readtxt("chnum.txt")); 
			Log.d(TAG, "maxid==" + maxid);
		} 
		catch(NumberFormatException   e) 
		{ 
			
		}

		if(1==key)
		{
			Log.d(TAG, "+++++++++++++");
			currentid=++currentid%maxid;
			Log.d(TAG, "currentid1==" + currentid);
			if(maxid==currentid)
				currentid=0;
			Log.d(TAG, "currentid2==" + currentid);
		}
		else if(0==key) 
		{	
			Log.d(TAG, "------------");
			if(0==currentid)
				currentid=maxid;
			currentid=--currentid%maxid;	
		}
				path=readtxt(String.valueOf(currentid));
				Log.d(TAG, "path==================" + path);
				if((path == "")||(path==null))
				{
							path =channel1;
							Log.d(TAG, "update path==================" + path);
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
			resetvideo(1);
		}
		else if((keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS)||(keyCode == KeyEvent.KEYCODE_DPAD_UP )||(keyCode == KeyEvent.KEYCODE_DPAD_LEFT ))
		{
			resetvideo(0);
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
			//InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);    
			InputStreamReader read = new InputStreamReader(getResources().getAssets().open(filename),encoding); 
			BufferedReader bufferedReader = new BufferedReader(read);    
              
			//while ((lineTXT = bufferedReader.readLine()) != null) {    
			//     System.out.println(lineTXT.toString().trim());    
			//}   
			lineTXT = bufferedReader.readLine();
			read.close(); 
		} catch (Exception e) {    
			System.out.println("读取文件内容操作出错");    
			e.printStackTrace();    
		}  
		return lineTXT;     
	}      
}
