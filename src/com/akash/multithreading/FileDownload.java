package com.akash.multithreading;

import java.io.*;
import java.net.*;

public class FileDownload extends Thread {
    private String url;
    private String destinationFile;
    private volatile boolean stopDownload;

    public FileDownload(String url, String destinationFile) {
        this.url = url;
        this.destinationFile = destinationFile;
    }

    public void run() {
        try {
            URL downloadUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            int fileSize = connection.getContentLength();

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1 && !stopDownload) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                double percentCompleted = ((double) totalBytesRead / fileSize) * 100;
                System.out.printf("Download progress: %.2f%%\n", percentCompleted);
            }

            bufferedOutputStream.close();
            inputStream.close();
            connection.disconnect();

            if (stopDownload) {
                System.out.println("Download interrupted.");
                File file = new File(destinationFile);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                System.out.println("Download complete.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopDownload() {
        stopDownload = true;
    }

    public static void main(String[] args) {
    String url = "http://www.angelikalanger.com/Lambdas/Lambdas.pdf";
    String destinationFile = "example.txt";

    FileDownload downloader = new FileDownload(url, destinationFile);
    downloader.start();

    try {
        Thread.sleep(30000);
        downloader.stopDownload();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}
