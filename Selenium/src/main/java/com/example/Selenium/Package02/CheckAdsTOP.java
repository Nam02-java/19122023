package com.example.Selenium.Package02;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CheckAdsTOP implements Runnable {
    private WebDriver driver;
    private CountDownLatch countDownLatch;
    private List<WebElement> element_solve;
    private WebDriverWait wait;

    public CheckAdsTOP(WebDriver driver, CountDownLatch countDownLatch) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CheckAdsTOP(WebDriver driver, CountDownLatch countDownLatch, WebDriverWait wait) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = wait;
    }

    @Override
    public void run() {
        if (wait != null) {
            //  logic for the second constructor
            System.out.println("logic for the second constructor of ESC");
            try {
                WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Đóng quảng cáo']"))).click();
            } catch (Exception exception) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='grippy-host'])[1]"))).click();
            }
        } else {
            // logic for the first constructor
            System.out.println("logic for the first constructor of ESC");
            element_solve = driver.findElements(By.xpath("(//div[@class='grippy-host'])[1]"));
            if (element_solve.size() > 0 && element_solve.get(0).isDisplayed()) {
                driver.findElement(By.xpath("(//div[@class='grippy-host'])[1]")).click();
            } else {
            }
        }
        countDownLatch.countDown();
    }
}