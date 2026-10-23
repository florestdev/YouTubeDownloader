package ru.florestdev;

import com.github.felipeucelli.javatube.Youtube;

public class Downloader {
    public static void download(String videoUrl, String directory) throws Exception {
        // Создаем запрос
        Youtube yt = new Youtube(videoUrl);
        yt.streams().getHighestResolution().download(directory);
    }
}