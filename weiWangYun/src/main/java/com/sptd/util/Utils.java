package com.sptd.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class Utils {
	 /**
     * 获取视频缩略图（这里获取第一帧）
     * @param filePath
     * @return
     */
    public static  Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    
    public static String formatData(long time){
    	try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(new Date(time));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
}
