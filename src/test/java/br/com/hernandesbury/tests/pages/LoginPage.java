package br.com.hernandesbury.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    private By username = By.id("user-name");
    private By password = By.id("password");
    private By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void preencherUsername(String usuario) {
        driver.findElement(username).sendKeys(usuario);
    }

    public void preencherPassword(String senha) {
        driver.findElement(password).sendKeys(senha);
    }

    public void clicarLogin() {
        driver.findElement(loginButton).click();
    }
}
