package com.example.Selenium.Package02;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CheckHandAD implements Runnable {
    private WebDriver driver;
    private CountDownLatch countDownLatch;
    private List<WebElement> element_solve;
    private WebDriverWait wait;

    public CheckHandAD(WebDriver driver, CountDownLatch countDownLatch) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public CheckHandAD(WebDriver driver, CountDownLatch countDownLatch, WebDriverWait wait) {
        this.driver = driver;
        this.countDownLatch = countDownLatch;
        this.wait = wait;
    }

    @Override
    public void run() {
        if (wait != null) {
            //  logic for the second constructor
            System.out.println("logic for the second constructor of HAND_AD");
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='×']"))).click();
                System.out.println("click check hand ad");
            } catch (Exception exception) {
                System.out.println("HAND_AD not display : " + exception);
            }
        } else {
            // logic for the first constructor
            element_solve = driver.findElements(By.xpath("//button[normalize-space()='×']"));
            if (element_solve.size() > 0 && element_solve.get(0).isDisplayed()) {
                driver.findElement(By.xpath("//button[normalize-space()='×']")).click();
            }else{
            }
        }
        countDownLatch.countDown();
    }
}


