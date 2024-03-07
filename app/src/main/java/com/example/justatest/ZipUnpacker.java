package com.example.justatest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUnpacker {

    public static void main(String[] args) {
        // Указывайте путь к вашему ZIP-файлу и путь, куда этот файл будет распакован.
        String zipFilePath = "path/to/your/file.zip";
        String destDirectory = "path/to/destination/directory";

        try {
            unzip(zipFilePath, destDirectory);
            System.out.println("ZIP file was successfully unpacked.");
        } catch (IOException e) {
            System.out.println("There was an error unpacking the ZIP file: " + e.getMessage());
        }
    }

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            // Итерация по всем элементам в архиве
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // Если элемент не директория, извлекаем файл
                    extractFile(zipIn, filePath);
                } else {
                    // Если элемент является директорией, создаем ее
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        // Создаем выходной поток для записи извлеченного файла
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
