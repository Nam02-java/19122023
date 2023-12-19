package com.example.Selenium.Package02;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CheckHostAD implements Runnable {
    private WebDriver driver;
    private WebDriverWait wait;
    private List<WebElement> element_solve;

    private CountDownLatch countDownLatch;

    public CheckHostAD(WebDriver driver, CountDownLatch countDownLatch) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public CheckHostAD(WebDriver driver, CountDownLatch countDownLatch, WebDriverWait wait) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = wait;
    }

    @Override
    public void run() {
        if (wait != null) {
            //  logic for the second constructor

            System.out.println("Logic for the second constructor of host ad ");
            try { // quảng cáo host 8 9
                System.out.println("Xem xét quảng cáo của host ");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[contains(@style,'width: 100vw')]"))); //iframe[contains(@style,'width: 100vw')]
                element_solve = driver.findElements(By.xpath("//iframe[contains(@style,'width: 100vw')]"));
                if (element_solve.size() > 0 && element_solve.get(0).isDisplayed()) {
                    System.out.println("Quảng của host có hiển thị ");
                    WebElement frame1 = driver.findElement(By.xpath("//iframe[contains(@style,'width: 100vw')]"));
                    driver.switchTo().frame(frame1);
                    driver.findElement(By.xpath("//div[@aria-label='Đóng quảng cáo']")).click();
                    System.out.println("Đã tắt host");
                    driver.switchTo().defaultContent(); // return default content
                }
                countDownLatch.countDown();
            } catch (Exception exception) {
                countDownLatch.countDown();
                System.out.println("Quảng cáo host 8 9 :  " + exception);
            }

        } else {
            // logic for the first constructor
        }
    }
}

