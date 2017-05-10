package com.sptd.eyun.service;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;







import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.sptd.eyun.R;
import com.sptd.eyun.finals.FileConfig;
import com.sptd.util.ApkUtil;
import com.sptd.util.HandlerUtil;

/**
 * 下载线程
 * <p/>
 * <br/> Author:罗文忠
 * <br/> Version: 1.0
 * <br/> Date: 2013-4-26
 */
public class UpdateService extends Service {

    /**
     * apk包的大小
     */
    private Float apkSize;
    /**
     * apk包的路径
     */
    private String apkPath;
    /**
     * 本地apk
     */
    private String apkLocal;
    /**
     * 缓存apk路径
     */
    private String tempApkPath;
    /**
     * 通知栏服务
     */
    private NotificationManager notificationManager;
    /**
     * 通知栏对象
     */
    private Notification notification;

    /**
     * 当前下载百分比
     */
    private double currentPercent = 0;

    private static final int NOTIFYCATION_DOWNLOAD_ID = 1990052301;

    private static final int LOAD_APK_UPDATE = 0;
    private static final int LOAD_APK_FINASH = 1;
    private static final int LOAD_APK_ERROR = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    // @Override
    // public void onStart(Intent intent, int startId) {
    // super.onStart(intent, startId);
    // }
    // 在2.0之后，推荐覆盖onStartCommand方法，而为了向前兼容，在onStartCommand依然会调用onStart方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
//            apkSize =Float.parseFloat(SpUtil.getSpUtil(TApplication.userInfoBean.getUserNo(), Activity.MODE_PRIVATE).getSPValue(getString(R.string.intent_key_apksize), ""));
//            apkPath = SpUtil.getSpUtil(TApplication.userInfoBean.getUserNo(), Activity.MODE_PRIVATE).getSPValue(getString(R.string.intent_key_apkpath), "");
        	
//			apkSize = Float.parseFloat(intent.getExtras().getString(getString(R.string.intent_key_apksize)));
			apkPath = intent.getExtras().getString("urlapk");
            apkLocal = FileConfig.PATH_DOWNLOAD + apkPath.substring(apkPath.lastIndexOf("/") + 1);
            tempApkPath = apkLocal.replace(".apk", ".temp");
            startDownLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新通知栏进度条
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     * <br/> Date: 2013-4-26
     */
    private void updateNotification() {
        currentPercent = new File(tempApkPath).length() / apkSize * 100;
        notification.contentView.setTextViewText(R.id.view_download_txt_title, getString(R.string.process_download_wait) + (int) currentPercent + "%");
        notification.contentView.setProgressBar(R.id.view_download_progress, 100, (int) currentPercent, false);
        notificationManager.notify(NOTIFYCATION_DOWNLOAD_ID, notification);
    }

    /**
     * 显示通知栏
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     * <br/> Date: 2013-4-27
     */
    private void showNotification() {
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        notificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);

        notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = getString(R.string.process_download_wait);
        notification.when = System.currentTimeMillis();
        // 通知栏显示所用到的布局文件
        notification.contentView = new RemoteViews(getPackageName(), R.layout.view_download_notify);
        notification.contentView.setTextViewText(R.id.view_download_txt_title, getString(R.string.process_download_wait) + currentPercent + "%");
        notification.contentView.setProgressBar(R.id.view_download_progress, 100, (int) currentPercent, true);
        notification.contentIntent = pIntent;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(NOTIFYCATION_DOWNLOAD_ID, notification);

    }

    /**
     * 开始下载文件
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     */
    private void startDownLoad() {
        handler.postDelayed(update, 1000);
        new Thread(new Runnable() {

            @Override
            public void run() {
                downloadFile(apkPath, apkLocal);
                if (currentPercent < 100) {
                    loadError();
                }
            }
        }).start();
    }

    /**
     * 下载文件.下载文件存储至指定路径.
     *
     * @param path     下载路径.
     * @param savePath 存储路径.
     * @return 下载成功返回true, 若下载失败则返回false.
     * <br/> Author:刘艺谋
     * <br/> Version: 1.0, 2013-3-6
     * @throws MalformedURLException 建立连接发生错误抛出MalformedURLException.
     * @throws IOException           下载过程产生错误抛出IOException.
     */
    public void downloadFile(String path, String savePath) {

        File tempFile = new File(tempApkPath);
        if (!new File(FileConfig.PATH_DOWNLOAD).exists()) {
            new File(FileConfig.PATH_DOWNLOAD).mkdirs();
        }

        if (new File(savePath).exists()) {
            HandlerUtil.sendMessage(handler, LOAD_APK_FINASH);
            currentPercent = 100;
            return;
        } else {// 清理之前下载的旧版APK文件
            for (File file : new File(FileConfig.PATH_DOWNLOAD).listFiles()) {
                file.delete();
            }
        }

        InputStream input = null;
        OutputStream output = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            apkSize=(float) conn.getContentLength();
            Log.v("urlapk", path);
            Log.v("apkSize", ""+ conn.getContentLength());
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
 //           conn.connect();
            
            int code = conn.getResponseCode();
            
            if (code == 200) {
                input = conn.getInputStream();

                tempFile.createNewFile(); // 创建文件
                output = new FileOutputStream(tempFile);
                byte buffer[] = new byte[1024];
                int read = 0;
                while ((read = input.read(buffer)) != -1) { // 读取信息循环写入文件
                    output.write(buffer, 0, read);
                }
                output.flush();
                currentPercent = 100;
                tempFile.renameTo(new File(savePath));
                HandlerUtil.sendMessage(handler, LOAD_APK_FINASH);
            } else {
                HandlerUtil.sendMessage(handler, LOAD_APK_ERROR);
            }
        } catch (MalformedURLException e) {
            HandlerUtil.sendMessage(handler, LOAD_APK_ERROR);
        } catch (IOException e) {
            HandlerUtil.sendMessage(handler, LOAD_APK_ERROR);
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 异步消息处理对象
     * <p/>
     * <br/> Version: 1.0
     * <br/> Date: 2013-4-26
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOAD_APK_UPDATE:
                    updateNotification();
                    break;
                case LOAD_APK_FINASH:
                    finish();
                    break;
                case LOAD_APK_ERROR:
                    loadError();
                    break;
            }

        }

    };

    /**
     * 更新下载进度的线程
     */
    private Runnable update = new Runnable() {

        @Override
        public void run() {

            if (currentPercent < 100) {
                HandlerUtil.sendMessage(handler, LOAD_APK_UPDATE);
                handler.postDelayed(update, 1000);
            }

        }
    };

    /**
     * 完成下载
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     * <br/> Date: 2013-4-27
     */
    private void finish() {
        currentPercent = 100;
        handler.removeCallbacks(update);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView.setViewVisibility(R.id.view_download_txt_msg, View.VISIBLE);
        notification.contentView.setViewVisibility(R.id.view_download_progress, View.GONE);

        setInstallApk(apkLocal);
        notification.contentView.setTextViewText(R.id.view_download_txt_title, getString(R.string.hint_download_finish));
        notification.contentView.setTextViewText(R.id.view_download_txt_msg, getString(R.string.hint_download_finish_install));
        notificationManager.notify(NOTIFYCATION_DOWNLOAD_ID, notification);

//        Intent intent = new Intent();
//        intent.setAction("com.utoow.diver.service.UpdateService");
//        stopService(intent);
  //      stopService(new Intent().setClass(this, LocationService.class));

        ApkUtil.installApk(new File(apkLocal),this);

    }

    /**
     * 下载失败
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     * <br/> Date: 2013-5-12
     */
    private void loadError() {
        handler.removeCallbacks(update);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView.setViewVisibility(R.id.view_download_txt_msg, View.VISIBLE);
        notification.contentView.setViewVisibility(R.id.view_download_progress, View.GONE);
        notification.contentView.setTextViewText(R.id.view_download_txt_title, getString(R.string.hint_download_error));
        notification.contentView.setTextViewText(R.id.view_download_txt_msg, getString(R.string.hint_download_error_check));
        notificationManager.notify(NOTIFYCATION_DOWNLOAD_ID, notification);
        Intent intent = new Intent();
        intent.setAction("com.sptd.eyun.service.UpdateService");
        stopService(intent);
    }

    /**
     * 设置点击后安装apk
     * <p/>
     * <br/> Author:罗文忠
     * <br/> Version: 1.0
     * <br/> Date: 2013-4-27
     *
     * @param path
     */
    private void setInstallApk(String path) {

        File file = new File(path);

        if (file.exists()) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notification.contentIntent = pendingIntent;

        } else {
            // TODO 下载失败
        }

    }

}
