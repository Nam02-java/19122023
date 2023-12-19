package com.example.Selenium.Package02;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class CheckFileName implements Runnable {
    private String fileName;
    private CountDownLatch countDownLatch;

    public static Boolean flag_checkFileName = true;

    public CheckFileName(String fileName, CountDownLatch countDownLatch) {
        this.fileName = fileName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        File directory = new File("E:\\New folder\\");
        File[] files = directory.listFiles(File::isFile);
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            String target = name.copyValueOf(".mp3".toCharArray());
            name = name.replace(target, "");
            if (name.equals(fileName)) {
                System.out.println("Tên File bị trùng trong dữ liệu của bạn , hãy đổi tên khác hoặc xóa file cũ của bạn");
                flag_checkFileName = false;
                break;
            }
        }
        countDownLatch.countDown();
    }
}