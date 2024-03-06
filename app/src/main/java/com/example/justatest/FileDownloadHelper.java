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
import java.io.File;


public class FileDownloadHelper {

    private Context context;
    private String folderPath = "/sdcard/";

    public FileDownloadHelper(Context context) {
        this.context = context;
    }

    @NonNull
    private static File getHomeDir() {
        return new File(Environment.getExternalStorageDirectory(), BuildConfig.home+"/1_04_patch.gro").getAbsoluteFile();
    }
    //чек папки, если папки нет вызов функции showDownloadDialog
    public void checkFolderAndDownloadFile() {
        File folder = getHomeDir();
        if (!folder.exists()) {
            showDownloadDialog();
        }
    }
    //Диалоговое окно спрашивающее качаем или нет, если да - вызов функции downloadFile
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
                        // Здесь вы можете прервать процесс скачивания
                        dialog.cancel();
                    }
                })
                .show();
        String url = "https://github.com/Skyrimus/Serious-Sam-Android/releases/download/v1.05.2/SeriousSamAndroid-v1.05.2-TSE-release.apk"; // ну типа путь ога?
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Загрузка файла...");
        request.setTitle("Файл загружается");

        // Если вы используете Wi-Fi, разрешите скачивание через Wi-Fi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SeriousSamTFE.zip"); //моё говно

        // Уведомление будет отображаться во время скачивания и после завершения загрузки
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadID = manager.enqueue(request);

        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // получение ID загрузки
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    Toast.makeText(context, "Zalupa", Toast.LENGTH_SHORT).show();

                }
            }
        };

        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }
}

