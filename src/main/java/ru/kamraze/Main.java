package ru.kamraze;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {
        File sourceFile = new File("savegames/SaveGames.zip");
        File destDir = new File("savegames");
        if (sourceFile.exists()) {
            try {
                openZip(sourceFile, destDir);
                GameProgress gameProgress = openProgress(destDir + File.separator + "save2.dat");
                System.out.println("Предыдущее сохранение игры загружено:");
                System.out.println(gameProgress);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка загрузки сохранения: " + e.getMessage());
            }
        } else {
            System.out.println("Файл архива сохранений " + sourceFile.getName() + " не найден.");
        }
    }

    public static void openZip(File zipFilePath, File destDirectory) throws IOException {
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
// Начинаем считывать в read данные в размере bytesIn. Считываем из архива, указанного в zipIn. Но где указатель на то, какую именно запись (файл) в архиве считывать???
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[1024];
                int read;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();

            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    public static GameProgress openProgress(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        GameProgress gameProgress = (GameProgress) ois.readObject();
        ois.close();
        return gameProgress;
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}