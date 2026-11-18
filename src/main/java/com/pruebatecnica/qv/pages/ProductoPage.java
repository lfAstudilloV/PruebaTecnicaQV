package com.pruebatecnica.qv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductoPage {
    private static final By AGREGAR_AL_CARRITO = By.cssSelector(".single_add_to_cart_button");
    private static final By CANTIDAD_CARRITO = By.cssSelector(".cart-contents-count");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void seleccionarTalla(String talla) {
        By botonTalla = By.cssSelector("button.variation-button[data-value='" + talla + "']");
        wait.until(ExpectedConditions.elementToBeClickable(botonTalla)).click();

        wait.until(currentDriver -> {
            Object valor = ((JavascriptExecutor) currentDriver)
                    .executeScript("return document.querySelector('.variation_id').value;");
            return valor != null && !"0".equals(valor.toString());
        });
    }

    public void agregarAlCarrito() {
        wait.until(ExpectedConditions.elementToBeClickable(AGREGAR_AL_CARRITO)).click();
        wait.until(currentDriver -> currentDriver.findElements(CANTIDAD_CARRITO).stream()
                .anyMatch(ProductoPage::cantidadActualizada));
    }

    private static boolean cantidadActualizada(org.openqa.selenium.WebElement elemento) {
        String texto = elemento.getText().trim();
        return !texto.isEmpty() && !"0".equals(texto);
    }
}
