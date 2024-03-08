package com.example.justatest;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.content.IntentFilter;
//uncompress
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
//fk no
import android.os.AsyncTask;
import android.app.ProgressDialog;
//Using filedownloader from git
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.BaseDownloadTask;


public class FileDownloadHelper {

    private Context context;
    private String folderPath = "/sdcard/";

    public FileDownloadHelper(Context context) {
        this.context = context;
    }

    @NonNull
    private static File getHomeDir() {
        return new File(Environment.getExternalStorageDirectory(), "/1_04_patch.gro").getAbsoluteFile();
    }
    // Проверка папки, если папки нет вызов функции showDownloadDialog
    public void checkFolderAndDownloadFile() {
        File folder = getHomeDir();
        if (!folder.exists()) {
            showDownloadDialog();
        }
    }
    // Диалоговое окно спрашивающее качаем или нет, если да - вызов функции downloadFile
    private void showDownloadDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Folder not found")
                .setMessage("Would you like to download the content?")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadFile();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void downloadFile(){

        FileDownloader.setup(context); // Это можно сделать в вашем Application классе

        FileDownloader.getImpl().create("https://github.com/slowpoke1337one/zadacha44/releases/download/v1.0.0/hgfjgfjfjtse.zip")
                .setPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/SE/downloaded.apk")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        // Вызывается, когда загрузка в ожидании начала
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        // Вызывается для обновления прогресса загрузки
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        // Вызывается, когда загрузка успешно завершена
                        Log.d("message","check");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        // Вызывается, когда загрузка приостановлена
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        // Вызывается, когда во время загрузки произошла ошибка
                        e.printStackTrace();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        // Вызывается, если загрузка уже находится в очереди
                    }
                }).start();


    }


    }

    // Добавление метода для распаковки архива


