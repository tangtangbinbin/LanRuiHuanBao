package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateVersionUtil {
    private static final String packageName = "com.zcy.qualitysupervisionandmanagement";
    private static final String apkName = "质量监督管理系统" + File.pathSeparator + ".apk";
    private String apkUrl = "";
    private int mustUpdate = 0;
    private String versionCodeFromServer = "";


    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private float progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Activity mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private TextView progressTextView;

    private Dialog mDownloadDialog;

    public UpdateVersionUtil(Activity context, String apkUrl, String versionCodeFromServer, int mustUpdate) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.versionCodeFromServer = versionCodeFromServer;
        this.mustUpdate = mustUpdate;
    }
    public UpdateVersionUtil(Activity context, String apkUrl ) {
        this.mContext = context;
        this.apkUrl = apkUrl;
    }

    /**
     * 版本更新检测
     */
    public void checkVerisonUpdate() {
        try {
            int versionCodeFromServerInt = Integer.valueOf(versionCodeFromServer);
            int appVersion = getVersionCode(mContext);
            Log.e("服务器上的版本号：", versionCodeFromServerInt + "");
            Log.e("APP上的版本号：", appVersion + "");
            if (appVersion < versionCodeFromServerInt) {
               // showNoticeDialog();
            }
        } catch (Exception e) {
           // showNoticeDialog();
        }

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("versionCode", versionCode + "");
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
  /*  public void showNoticeDialog() {
        if (mustUpdate == ConstantKey.MUST_UPDATE) {
            DialogUtils.showCustionTwoButtonDialog(mContext, "检测到新版本，由于服务器升级，APP需要更新后才能使用",
                    "暂不更新", "立即更新",
                    new DialogBuilder.OnDialogButtonClickListener() {
                        @Override
                        public void onDialogButtonClick(Context context, DialogBuilder builder, Dialog dialog,
                                                        int dialogId, int which) {
                            if (which == BUTTON_LEFT) {
                                dialog.dismiss();
                                mContext.finish();
                            } else {
                                dialog.dismiss();
                                // 显示下载对话框
                                showDownloadDialog();
                            }
                        }
                    });
        } else {
            DialogUtils.showCustionTwoButtonDialog(mContext, "检测到新版本，立即更新吗?", "暂不更新", "立即更新",
                    new DialogBuilder.OnDialogButtonClickListener() {
                        @Override
                        public void onDialogButtonClick(Context context, DialogBuilder builder, Dialog dialog,
                                                        int dialogId, int which) {
                            if (which == BUTTON_LEFT) {
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                // 显示下载对话框
                                showDownloadDialog();
                            }
                        }
                    });
        }
    }
*/
    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_update_version, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        progressTextView = (TextView) v.findViewById(R.id.progressTextView);
        builder.setView(v);
        builder.setCancelable(false);
        //if (mustUpdate != ConstantKey.MUST_UPDATE) {
            builder.setNegativeButton("后台下载", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                   // MyToastUtils.showToast("已转入后台下载");
                }
            });
            // 取消更新
            builder.setPositiveButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 设置取消状态
                    cancelUpdate = true;

                }
            });
       // }

        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }


    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new AsyncTask<Integer, Float, Boolean>() {
            @Override
            protected void onProgressUpdate(Float... values) {
                mProgress.setProgress((int) progress);

                progressTextView.setText(progress + "%");
                super.onProgressUpdate(values);

            }

            @Override
            protected Boolean doInBackground(Integer... params) {
                try {
                    // 判断SD卡是否存在，并且是否具有读写权限
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        // 获得存储卡的路径
                        String sdpath = Environment.getExternalStorageDirectory() + File.separator;
                        mSavePath = sdpath + "download";
                        URL url = new URL(apkUrl);
                        // 创建连接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        // 获取文件大小
                        double length = conn.getContentLength();
                        // 创建输入流
                        InputStream is = conn.getInputStream();

                        File file = new File(mSavePath);
                        // 判断文件目录是否存在
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, apkName);
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        double count = 0;
                        // 缓存
                        byte buf[] = new byte[1024];
                        // 写入到文件中
                        do {
                            int numread = is.read(buf);
                            count += numread;
                            // 计算进度条位置
                            progress = (int) ((count / length) * 100);
                            // 更新进度
                            Log.e("ddd", progress + "%");
                            publishProgress(progress);
                            // 写入文件
                            fos.write(buf, 0, numread);
                        } while (count < length && !cancelUpdate);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
                return cancelUpdate;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                // 取消下载对话框显示
                mDownloadDialog.dismiss();
/*
                if (mustUpdate == ConstantKey.MUST_UPDATE) {
                    showNoticeDialog();
                }*/
                if (!aVoid) {
                    installApk();
                } /*else {
                    MyToastUtils.showToast("APP没有储存权限，将调用外部浏览器下载安装包");
                    IntentUtil.startOutSideWebBrowser(mContext, apkUrl);
                }*/
            }
        }.execute();
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}
