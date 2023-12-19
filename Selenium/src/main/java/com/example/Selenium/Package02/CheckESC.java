package com.example.Selenium.Package02;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CheckESC implements Runnable {
    private WebDriver driver;
    private CountDownLatch countDownLatch;
    private List<WebElement> element_solve;
    private WebDriverWait wait;
    public CheckESC(WebDriver driver, CountDownLatch countDownLatch) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public CheckESC(WebDriver driver, CountDownLatch countDownLatch, WebDriverWait wait) {
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
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/small"))).click();
                System.out.println("click ESC");
            } catch (Exception exception) {
                System.out.println("ESC not display : " + exception);
            }
        } else {
            // logic for the first constructor
            System.out.println("logic for the first constructor of ESC");
            element_solve = driver.findElements(By.xpath("/html/body/div[1]/div[1]/small"));
            if (element_solve.size() > 0 && element_solve.get(0).isDisplayed()) {
                driver.findElement(By.xpath("/html/body/div[1]/div[1]/small")).click();
            }else{
            }
        }
        countDownLatch.countDown();
    }
}

// 3 phut 22 giay - co block
// 2 phut 40 giay - co block
// 3 phut 19 giay - co block
// 3 phut 30 giay - co block

// 3 phut 1 giay - ko co block
// 3 phut 1 giay - ko co block
// 3 phut 2 giay - ko co block
// 3 phut 5 giay - ko co block




