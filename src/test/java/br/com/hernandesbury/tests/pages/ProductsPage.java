package br.com.hernandesbury.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage {

    private WebDriver driver;

    private By productsTitle = By.className("title");
    private By backpack = By.id("item_4_title_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String obterTitulo() {
        return driver.findElement(productsTitle).getText();
    }

    public void acessarBackpack() {
        driver.findElement(backpack).click();
    }
}

