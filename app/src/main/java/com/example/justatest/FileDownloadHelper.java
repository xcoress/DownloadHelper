package com.example.justatest;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.content.BroadcastReceiver;
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

public class FileDownloadHelper {

    private Context context;
    private String folderPath = "/sdcard/";

    public FileDownloadHelper(Context context) {
        this.context = context;
    }

    @NonNull
    private static File getHomeDir() {
        return new File(Environment.getExternalStorageDirectory(), BuildConfig.home + "/1_04_patch.gro").getAbsoluteFile();
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

    private void downloadFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Файл на скачивании")
                .setMessage("Статус скачивания...")
                .setPositiveButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();

        String url = "https://github.com/Skyrimus/Serious-Sam-Android/releases/download/v1.05.2/SeriousSamAndroid-v1.05.2-TSE-release.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Загрузка файла...");
        request.setTitle("Файл загружается");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SeriousSamTFE.zip");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadID = manager.enqueue(request);

        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show();

                    // Путь к загруженному файлу в папке "Downloads"
                    String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";
                    String fileName = "SeriousSamTFE.zip";
                    unpackZip(downloadPath, fileName);
                }
            }
        };

        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    // Добавление метода для распаковки архива
    private void unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
