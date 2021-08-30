package com.demo.scheduler;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
public class SeleniumScheduler {

	
	
	public void testGoogle() {
		System.setProperty("webdriver.gecko.driver", "/usr/local/Cellar/geckodriver/0.29.1/bin/geckodriver");
		
		WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            driver.get("https://google.com/ncr");
            driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
            WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("h3")));
            System.out.println(firstResult.getAttribute("textContent"));
        } finally {
            driver.quit();
        }
	}
	
	public static void main(String[] args) {
		new SeleniumScheduler().testGoogle();
	}
}
