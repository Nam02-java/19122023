package com.example.Selenium.Package02;

import java.util.concurrent.CountDownLatch;

public class CheckText implements Runnable {
    private String text;
    private CountDownLatch countDownLatch;
    public static String notification;

    public static Boolean flag_checkText = true;

    public CheckText(String text, CountDownLatch countDownLatch) {
        this.text = text;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        text = text.trim();

        if (text.matches("[!@#$%^&*()_+=,]+")) {
            notification = "Chỉ chứa kí tự đặc biệt , trình phiên dịch không thể đọc , yêu cầu nhập lại Text";
            System.out.println(notification);
            flag_checkText = false;

        } else if (text.matches(".*[a-zA-Z].*") && text.matches(".*\\d.*")) {
        } else if (text.isEmpty()) {
            notification = "Không được để trống , yêu cầu nhập lại";
            System.out.println(notification);
            flag_checkText = false;
        } else {
        }
        countDownLatch.countDown();
    }
}

