package com.pruebatecnica.qv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ZapatosPage {
    private static final String URL = "https://www.bon-bonite.com/categoria-producto/zapatos-mujer/";
    private static final By PRODUCTO = By.cssSelector("a[href$='/producto/zapato-en-gamuza-tabaco/']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ZapatosPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void abrir() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCTO));
    }

    public void abrirProducto() {
        wait.until(ExpectedConditions.elementToBeClickable(PRODUCTO)).click();
        wait.until(ExpectedConditions.urlContains("/producto/zapato-en-gamuza-tabaco"));
    }
}
