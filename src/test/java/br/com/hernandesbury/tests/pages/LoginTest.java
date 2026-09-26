package br.com.hernandesbury.tests.pages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);

        driver.get("https://www.saucedemo.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("user-name")
        ));
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private void realizarLoginComSucesso() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("user-name")
        )).sendKeys("standard_user");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("password")
        )).sendKeys("secret_sauce");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("login-button")
        )).click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("inventory_list")
        ));
    }

    private void adicionarBackpackAoCarrinho() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-sauce-labs-backpack")
        )).click();
    }

    private void acessarCarrinho() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.className("shopping_cart_link")
        )).click();

        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    private void acessarCheckout() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("checkout")
        )).click();

        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("first-name")
        ));
    }

    private void preencherCheckout() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("first-name")
        )).sendKeys("Hernandes");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("last-name")
        )).sendKeys("Bury");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("postal-code")
        )).sendKeys("44380-000");
    }

    // =========================
    // LOGIN
    // =========================

    @Test
    void deveRealizarLoginComCredenciaisValidas() {

        loginPage.preencherUsername("standard_user");
        loginPage.preencherPassword("secret_sauce");
        loginPage.clicarLogin();

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("inventory_list")
        ));

        assertEquals(
                "Products",
                productsPage.obterTitulo()
        );
    }

    @Test
    void deveAcessarDetalhesDoProduto() {

        realizarLoginComSucesso();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("item_4_title_link")
        )).click();

        wait.until(ExpectedConditions.urlContains("inventory-item.html"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("inventory_details_name")
        ));
    }

    @Test
    void deveImpedirLoginComCredenciaisInvalidas() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("user-name")
        )).sendKeys("usuario_invalido");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("password")
        )).sendKeys("senha_invalida");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("login-button")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSemPreencherOsCampos() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("login-button")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Epic sadface: Username is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSomenteComUsername() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("user-name")
        )).sendKeys("standard_user");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("login-button")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Epic sadface: Password is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSomenteComPassword() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("password")
        )).sendKeys("secret_sauce");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("login-button")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Epic sadface: Username is required",
                mensagemErro
        );
    }

    // =========================
    // PRODUTOS
    // =========================

    @Test
    void deveExibirProdutosDisponiveis() {

        realizarLoginComSucesso();

        int quantidadeProdutos = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.className("inventory_item")
                )
        ).size();

        assertEquals(6, quantidadeProdutos);
    }

    @Test
    void deveOrdenarProdutosPorNome() {

        realizarLoginComSucesso();

        Select ordenacao = new Select(
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.className("product_sort_container")
                ))
        );

        ordenacao.selectByValue("az");

        String primeiroProduto = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.className("inventory_item_name")
                )
        ).get(0).getText();

        assertEquals(
                "Sauce Labs Backpack",
                primeiroProduto
        );
    }

    @Test
    void deveOrdenarProdutosPorPreco() {

        realizarLoginComSucesso();

        Select ordenacao = new Select(
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.className("product_sort_container")
                ))
        );

        ordenacao.selectByValue("lohi");

        String primeiroPreco = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.className("inventory_item_price")
                )
        ).get(0).getText();

        assertEquals(
                "$7.99",
                primeiroPreco
        );
    }

    @Test
    void deveExibirDetalhesDoProduto() {

        realizarLoginComSucesso();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("item_4_title_link")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("inventory_details_name")
        ));

        String nomeProduto = driver.findElement(
                By.className("inventory_details_name")
        ).getText();

        String descricao = driver.findElement(
                By.className("inventory_details_desc")
        ).getText();

        String preco = driver.findElement(
                By.className("inventory_details_price")
        ).getText();

        assertEquals(
                "Sauce Labs Backpack",
                nomeProduto
        );

        assertEquals(
                "$29.99",
                preco
        );

        assertEquals(
                false,
                descricao.isEmpty()
        );
    }

    // =========================
    // CARRINHO
    // =========================

    @Test
    void deveAdicionarProdutoAoCarrinho() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        String quantidade = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("shopping_cart_badge")
                )
        ).getText();

        assertEquals(
                "1",
                quantidade
        );
    }

    @Test
    void deveAdicionarMultiplosProdutosAoCarrinho() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-sauce-labs-bolt-t-shirt")
        )).click();

        acessarCarrinho();

        int quantidadeProdutos = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.className("cart_item")
                )
        ).size();

        assertEquals(
                2,
                quantidadeProdutos
        );
    }

    @Test
    void deveRemoverProdutoDoCarrinho() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("remove-sauce-labs-backpack")
        )).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("cart_item")
        ));

        int quantidadeProdutos = driver.findElements(
                By.className("cart_item")
        ).size();

        assertEquals(
                0,
                quantidadeProdutos
        );
    }

    @Test
    void deveContinuarComprandoAposAcessarCarrinho() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("continue-shopping")
        )).click();

        wait.until(ExpectedConditions.urlContains(
                "inventory.html"
        ));

        assertEquals(
                "https://www.saucedemo.com/inventory.html",
                driver.getCurrentUrl()
        );
    }

    // =========================
    // CHECKOUT
    // =========================

    @Test
    void deveImpedirCheckoutSemPreenchimento() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        acessarCheckout();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("continue")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Error: First Name is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirCheckoutSomenteComFirstName() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        acessarCheckout();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("first-name")
        )).sendKeys("Hernandes");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("continue")
        )).click();

        String mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='error']")
                )
        ).getText();

        assertEquals(
                "Error: Last Name is required",
                mensagemErro
        );
    }

    @Test
    void deveAceitarPreenchimentoDoLastName() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        acessarCheckout();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("first-name")
        )).sendKeys("Hernandes");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("last-name")
        )).sendKeys("Bury");

        String firstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first-name")
                )
        ).getAttribute("value");

        assertEquals(
                "Hernandes",
                firstName
        );
    }

    @Test
    void deveAvancarParaRevisaoComDadosValidos() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        acessarCheckout();

        preencherCheckout();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("continue")
        )).click();

        wait.until(ExpectedConditions.urlContains(
                "checkout-step-two.html"
        ));

        assertEquals(
                "https://www.saucedemo.com/checkout-step-two.html",
                driver.getCurrentUrl()
        );

        String nomeProduto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("inventory_item_name")
                )
        ).getText();

        assertEquals(
                "Sauce Labs Backpack",
                nomeProduto
        );
    }

    @Test
    void deveFinalizarCompraComSucesso() {

        realizarLoginComSucesso();

        adicionarBackpackAoCarrinho();

        acessarCarrinho();

        acessarCheckout();

        preencherCheckout();

        WebElement botaoContinue = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("continue"))
        );

        botaoContinue.click();

        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));

        wait.until(ExpectedConditions.urlContains(
                "checkout-step-two.html"
        ));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("finish")
        )).click();

        wait.until(ExpectedConditions.urlContains(
                "checkout-complete.html"
        ));

        String mensagem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("complete-header")
                )
        ).getText();

        assertEquals(
                "Thank you for your order!",
                mensagem
        );
    }

    // =========================
    // ENCERRAMENTO
    // =========================

    @AfterEach
    void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}