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
import java.io.FileOutputStream;  
import java.io.InputStreamReader; 
import java.util.Properties; 
import android.content.Context;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

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
	private String channeldef ="rtsp://58.241.134.33:554/301";

	private Bundle extras;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private Properties prop;
	private Context context;
	//private String PropPath1 = "/data/data/"+ getPackageName()+"/shared_prefs/config.properties";
	private String PropPath2 = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/com.wzf.LiveDemo/shared_prefs/";
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
		copyAssetsToSD();
		TestProp();
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
	                		path =channeldef;
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
							path =channeldef;
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
			String filepath="/mnt/sdcard/LiveDemo/";
			InputStreamReader read=null;

			if((new File(filepath).isDirectory())){
				read = new InputStreamReader(new FileInputStream(filepath+filename), encoding);
				Log.d(TAG, "Read File From SDCARD.");
			}
			else{
				read = new InputStreamReader(getResources().getAssets().open(filename),encoding); 
				Log.d(TAG, "Read File From Assets.");
			}

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

	//读取配置文件 
	public Properties loadConfig(Context context,String file) {
	Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}
	//保存配置文件
	public boolean saveConfig(Context context,String file, Properties properties) {
		try {
			File fil=new File(file);
			if(!fil.exists())
			fil.createNewFile();
			FileOutputStream s = new FileOutputStream(fil);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void TestProp(){
		boolean b=false;
		String s="";
		int i=0;
		prop=loadConfig(context,PropPath2+"config.properties");
		if(prop==null){
			//配置文件不存在的时候创建配置文件 初始化配置信息
			Log.d(TAG, "config file isn't exist!");
			prop=new Properties();
			prop.put("bool", "yes");
			prop.put("string", "aaaaaaaaaaaaaaaa");
			prop.put("int", "110");//也可以添加基本类型数据 get时就需要强制转换成封装类型
			saveConfig(context,PropPath2+"config.properties",prop);
		}
		
		prop.put("bool", "no");//put方法可以直接修改配置信息，不会重复添加
		b=(((String)prop.get("bool")).equals("yes"))?true:false;//get出来的都是Object对象 如果是基本类型 需要用到封装类
		s=(String)prop.get("string");
		i=Integer.parseInt((String)prop.get("int"));
		saveConfig(context,PropPath2+"config.properties",prop);
	}

	void copyAssetsToSD() { 
            String[] files;             
            try    
        {    
            files = this.getResources().getAssets().list("");    
        }    
        catch (IOException e1)    
        {    
            return;    
        }   
        //String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/com.wzf.LiveDemo/shared_prefs/";
		//String DB_PATH = "/data/data/"+ getPackageName()+"/shared_prefs/";
		//Log.d(TAG, "DB_PATH ="+DB_PATH);
        File mWorkingPath = new File(PropPath2);
        //if this directory does not exists, make one. 
        if(!mWorkingPath.exists())    
        {    
            Log.d(TAG, "directory does not exists, make one");
            if(!mWorkingPath.mkdirs())    
            {    
                 Log.e(TAG, "make directory fail.");
            }    
        }    
        
        for(int i = 0; i < files.length; i++)    
        {    
            try    
            {    
                String fileName = files[i];    
        	Log.d(TAG, "fileName ="+fileName);
                if(fileName.compareTo("images") == 0 ||    
                        fileName.compareTo("sounds") == 0 ||    
                        fileName.compareTo("webkit") == 0)    
                     {    
                         continue;    
                     } 

        
                File outFile = new File(mWorkingPath, fileName);    
                if(outFile.exists()) continue;    
        
                InputStream in = getAssets().open(fileName);    
                OutputStream out = new FileOutputStream(outFile);    
        
                // Transfer bytes from in to out   
                byte[] buf = new byte[1024];    
                int len;    
                while ((len = in.read(buf)) > 0)    
                {    
                    out.write(buf, 0, len);    
                }    
        
                in.close();    
                out.close();    
            }    
            catch (FileNotFoundException e)    
            {    
                e.printStackTrace();    
            }    
            catch (IOException e)    
            {    
                e.printStackTrace();    
            }    
        }
    }    

}
