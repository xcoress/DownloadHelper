package com.example.justatest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

    /**
     * Распаковывает содержимое ZIP-файла в указанную директорию.
     * @param zipFilePath Путь к ZIP-файлу.
     * @param destDirectory Путь к директории, куда будут распакованы файлы.
     * @param listener Слушатель событий процесса и завершения распаковки.
     * @throws IOException В случае ошибок ввода/вывода.
     */
    public void unzip(String zipFilePath, String destDirectory, UnzipListener listener) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // Итерируем по элементам архива
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // Если это файл, извлекаем его
                extractFile(zipIn, filePath);
                // Вызываем коллбэк при обработке файла
                if (listener != null) {
                    listener.onFileProcessed(new File(filePath));
                }
            } else {
                // Если это директория, создаем ее
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        // Вызываем коллбэк после завершения распаковки
        if (listener != null) {
            listener.onUnzipCompleted();
        }
    }

    /**
     * Извлекает файл из потока ZIP в указанный путь.
     * @param zipIn Поток ZIP-архива.
     * @param filePath Путь к файлу для извлечения.
     * @throws IOException В случае ошибок ввода/вывода.
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public interface UnzipListener {
        void onFileProcessed(File file);
        void onUnzipCompleted();
    }

    public static void main(String[] args) {
        UnzipUtility unzipper = new UnzipUtility();
        try {
            unzipper.unzip("path/to/your/archive.zip", "destination/directory", new UnzipListener() {
                @Override
                public void onFileProcessed(File file) {
                    System.out.println("Processed: " + file.getName());
                }

                @Override
                public void onUnzipCompleted() {
                    System.out.println("Unzip completed!");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
