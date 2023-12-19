package com.example.Selenium.Package02;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SaveCaptcha_Image {
    private WebDriver driver;

    private WebElement webElement;
    private String path;

    private String fileName;

    private WebDriverWait wait;



    public SaveCaptcha_Image(WebDriver driver, WebElement webElement, String path, String fileName) {
        this.driver = driver;
        this.webElement = webElement;
        this.path = path;
        this.fileName = fileName;
    }

    public void getCaptcha() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"captcha_image\"]")));

        webElement = driver.findElement(By.xpath("//*[@id=\"captcha_image\"]"));
        File imageFile = webElement.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        saveImage(imageFile, path, fileName);
    }
    // (//img[@id='captcha_image'])[1]
    private void saveImage(File imageFile, String folderPath, String fileName) {
        try {
            File destinationFolder = new File(folderPath);

            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            File destinationFile = new File(destinationFolder, fileName);

            FileUtils.copyFile(imageFile, destinationFile);

            System.out.println("Đã lưu trữ ảnh thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lưu trữ ảnh.");
        }
    }
}
