package com.example.Selenium.Package02;

import com.example.Selenium.Package03.CaptchaSolove_bot;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.example.Selenium.Package02.CheckFileName.flag_checkFileName;
import static com.example.Selenium.Package02.CheckText.flag_checkText;
import static com.example.Selenium.Package02.CheckText.notification;

@RestController
@RequestMapping("/api/web")
public class Selenium extends TelegramLongPollingBot {

    protected static String text = null;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        text = message.getText();
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return "CaptchaSlove_bot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotToken() {
        return "6928830332:AAGmv3fN_k8YdITzJeOyjqtsDQfWuviF308";
    }

    @RequestMapping("/photo")
    public String SendPhoto() throws TelegramApiException, InterruptedException {
        Thread.sleep(2000);
        String save_image = "E:\\CongViecHocTap\\Captcha\\captcha.png";
        SendPhoto photo = new SendPhoto();
        photo.setChatId("1159534870");
        photo.setPhoto(new InputFile(new File(String.valueOf(save_image))));
        try {
            this.execute(photo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return "Send photo 200ok";
    }

    @GetMapping("/LastAPI")
    public ResponseEntity<?> LastAPI(@RequestParam Map<String, String> params) throws InterruptedException, IOException, TelegramApiException {
        List<WebElement> element_solve;
        String user_name = "nam03test"; // mô phỏng tên user
        String user_password = "MK123nam"; // mô phỏng password user
        JavascriptExecutor js;
        WebElement webElement;
        WebDriverWait wait;

        System.setProperty("webdriver.http.factory", "jdk-http-client");
        System.setProperty("webdriver.chrome.driver", "E:\\CongViecHocTap\\ChromeDriver\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("useAutomationExtension", false); // disable chrome running as automation
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation")); // disable chrome running as automation

        WebDriver driver = new ChromeDriver(chromeOptions);

        CountDownLatch latch = new CountDownLatch(2);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2)); // số giây mà 1 driver chờ để load 1 phần tử nếu không có thiết lập của wait
        driver.manage().window().maximize();

        System.out.println("-----------------------------\n" + params.get("Text") + " " + params.get("Voice") + " " + params.get("FileName"));

        Thread checkFileName = new Thread(new CheckFileName(params.get("FileName"), latch));
        Thread checkText = new Thread(new CheckText(params.get("Text"), latch));
        checkFileName.start();
        checkText.start();
        latch.await();

        if (flag_checkFileName == false) {
            flag_checkFileName = true;
            driver.close();
            return ResponseEntity.ok(new String("Tên File bị trùng trong dữ liệu của bạn , hãy đổi tên khác hoặc xóa file cũ của bạn"));
        }
        if (flag_checkText == false) {
            flag_checkText = true;
            driver.close();
            return ResponseEntity.ok(new String(notification));
        }

        driver.get("https://ttsfree.com/login");

        ((JavascriptExecutor) driver).executeScript("var images = document.getElementsByTagName('img'); for(var i = 0; i < images.length; i++) { images[i].setAttribute('hidden', 'true'); }");

        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(user_name);
        driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys(user_password);

        latch = new CountDownLatch(2); // thiết lập 2 Thread ( trường hợp sau khi send key password sẽ có 1 trong 2 hiển thị nên thiết lập 2 thread kiểm tra cùng 1 lúc )
        Thread threadCheckESC = new Thread(new CheckESC(driver, latch, null));
        Thread threadCheckHandAD = new Thread(new CheckHandAD(driver, latch, null));
        threadCheckESC.start();
        threadCheckHandAD.start();
        latch.await();

        driver.findElement(By.xpath("//ins[@class='iCheck-helper']")).click();
        driver.findElement(By.xpath("//input[@id='btnLogin']")).click();

        driver.get("https://ttsfree.com/vn");

        ((JavascriptExecutor) driver).executeScript("var images = document.getElementsByTagName('img'); for(var i = 0; i < images.length; i++) { images[i].setAttribute('hidden', 'true'); }");
        ((JavascriptExecutor) driver).executeScript("var images = document.querySelectorAll('img[id=captcha_image]'); for(var i = 0; i < images.length; i++) { if(images[i].src.startsWith('https://ttsfree.com/voice/captcha.php?sign=?')) { images[i].removeAttribute('hidden'); } }");

        js = (JavascriptExecutor) driver;
        webElement = driver.findElement(By.xpath("//*[@id=\"input_text\"]"));
        js.executeScript("arguments[0].scrollIntoView();", webElement);


        latch = new CountDownLatch(1);
        Thread threadCheckAdsTOP = new Thread(new CheckAdsTOP(driver, latch));
        threadCheckAdsTOP.start();
        latch.await();

        latch = new CountDownLatch(1);
        threadCheckESC = new Thread(new CheckESC(driver, latch));
        threadCheckESC.start();
        latch.await();

        latch = new CountDownLatch(1);
        threadCheckHandAD = new Thread(new CheckHandAD(driver, latch));
        threadCheckHandAD.start();
        latch.await();

        driver.findElement(By.xpath("//textarea[@id='input_text']")).sendKeys(params.get("Text"));

        if (params.get("Voice").equals("Female")) {
            driver.findElement(By.xpath("(//label[@for='radioPrimaryvi-VN'])[1]")).click();
        } else if (params.get("Voice").equals("Male")) {
            driver.findElement(By.xpath("(//label[@for='radioPrimaryvi-VN2'])[1]")).click();
        }

        driver.findElement(By.xpath("//a[@class='btn mb-2 lg action-1 text-white convert-now']")).click();

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert alert-danger alert-dismissable']"))).isDisplayed();
            System.out.println("displayed captcha");

            js = (JavascriptExecutor) driver;
            webElement = driver.findElement(By.xpath("(//a[normalize-space()='Confirm'])[1]"));
            js.executeScript("arguments[0].scrollIntoView();", webElement);

            SaveCaptcha_Image saveCaptchaImage = new SaveCaptcha_Image(driver, webElement, "E:\\CongViecHocTap\\Captcha\\", "captcha.png");
            saveCaptchaImage.getCaptcha();
            SendPhoto();

            System.out.println("done image");

            int countdownDuration = 30;
            SendMessage message = new SendMessage();
            message.setChatId("1159534870"); // update.getMessage().getChatId().toString()
            int zero = countdownDuration;
            for (int second = 0; second <= countdownDuration; second++) {
                zero = countdownDuration - second;
                System.out.println(zero);

                Thread.sleep(1000);

                if (text != null) {
                    System.out.println("text : " + text);
                    for (int i = 0; i < text.length(); i++) {
                        if (!Character.isDigit(text.charAt(i))) {
                            message.setText("Value of text has char");
                            execute(message);
                            text = null;
                            break;
                        }
                    }
                    if (text == null || text.isEmpty() || text.length() <= 3) {
                        if (text == null) {
                        } else {
                            message.setText("Text length must be 4 numbers or more");
                            execute(message);
                        }
                        text = null;
                        continue;
                    }

                    driver.findElement(By.xpath("(//input[@id='captcha_input'])[1]")).sendKeys(text);
                    element_solve = driver.findElements(By.xpath("(//img[@title='Ad.Plus Advertising'])[1]"));
                    if (element_solve.size() > 0 && element_solve.get(0).isDisplayed()) {
                        driver.findElement(By.xpath("(//img[@title='Ad.Plus Advertising'])[1]")).click();
                    }
                    driver.findElement(By.xpath("(//a[normalize-space()='Confirm'])[1]")).click();
                    try {
                        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//h4[normalize-space()='Error!'])[1]")));
                        message.setText("Wrong number of captcha image , type again !");
                        execute(message);
                        text = null;
                        saveCaptchaImage.getCaptcha();
                        SendPhoto();
                        continue;

                    } catch (Exception exception) {
                        message.setText("Valid captcha code");
                        execute(message);

                        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Tạo Voice')]"))).click();

                        break;
                    }
                }
                System.out.println("---------------------");
            }if (zero == 0) {
                message.setText("End of time to solove captcha");
                execute(message);
                driver.close();
                return ResponseEntity.ok(new String("End of time to solove captcha"));
            }

        } catch (Exception e) {
            System.out.println("Non display captcha");
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"progessResults\"]/div[2]/center[1]/div/a"))).click();
        System.out.println("clicked download");
        latch = new CountDownLatch(1);
        Thread threadCheckHostAD = new Thread(new CheckHostAD(driver, latch));
        threadCheckHostAD.start();
        latch.await();


        /**
         * đổi tên file theo yêu cầu user ( đơn luồng thì hoạt động oke , đa luồng thì lỗi -> đang nghiên cứu login 1 lúc có request cùng đổi để đảm bảo không có lỗi xảy ra
         * đang nghiên cứu để update
         */
//        File folder = new File("E:\\New folder");
//        File[] files = folder.listFiles();
//        if (files != null && files.length > 0) {
//            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
//            File latestFile = files[0];
//            System.out.println(latestFile.getName());
//            String newFileName = params.get("FileName") + ".mp3";
//            File newFile = new File(folder, newFileName);
//            latestFile.renameTo(newFile);
//        }
        driver.close();
        return ResponseEntity.ok(new String("Downloaded successfully"));
    }
}


/**
 * cd C:\Program Files\Google\Chrome\Application
 * chrome.exe --remote-debugging-port=9222 --user-data-dir="E:\CongViecHocTap\ChromeData"
 * chrome.exe --remote-debugging-port=9222 --user-data-dir="D:\New folder\ChromeDriver\chromedriver-win64"
 * <p>
 * chrome://settings/content/popups
 */


