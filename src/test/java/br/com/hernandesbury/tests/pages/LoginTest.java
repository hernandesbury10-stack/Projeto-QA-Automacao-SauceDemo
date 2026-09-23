package br.com.hernandesbury.tests.pages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.hernandesbury.tests.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    void deveRealizarLoginComCredenciaisValidas() {
        loginPage.preencherUsername("standard_user");
        loginPage.preencherPassword("secret_sauce");
        loginPage.clicarLogin();
        assertEquals(
                "Products",
                productsPage.obterTitulo()
        );
    }

        @Test
        void deveAcessarDetalhesDoProduto() {

            loginPage.preencherUsername("standard_user");
            loginPage.preencherPassword("secret_sauce");
            loginPage.clicarLogin();

            productsPage.acessarBackpack();
        }


    @Test
    void deveImpedirLoginComCredenciaisInvalidas() {
        loginPage.preencherUsername("usuario_invalido");
        loginPage.preencherPassword("senha_invalida");
        loginPage.clicarLogin();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSemPreencherOsCampos() {
        loginPage.clicarLogin();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Epic sadface: Username is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSomenteComUsername() {
        loginPage.preencherUsername("standard_user");
        loginPage.clicarLogin();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Epic sadface: Password is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirLoginSomenteComPassword() {
        loginPage.preencherPassword("secret_sauce");
        loginPage.clicarLogin();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Epic sadface: Username is required",
                mensagemErro
        );
    }

    @Test
    void deveExibirProdutosDisponiveis() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        int quantidadeProdutos = driver.findElements(By.className("inventory_item")).size();

        assertEquals(6, quantidadeProdutos);
    }

    @Test
    void deveOrdenarProdutosPorNome() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.className("product_sort_container"))
                .click();

        driver.findElement(By.cssSelector("option[value='az']"))
                .click();

        String primeiroProduto = driver.findElements(By.className("inventory_item_name"))
                .get(0).getText();

        assertEquals("Sauce Labs Backpack", primeiroProduto);
    }

    @Test
    void deveOrdenarProdutosPorPreco() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.className("product_sort_container")).click();

        driver.findElement(By.cssSelector("option[value='lohi']")).click();

        String primeiroPreco = driver.findElements(By.className("inventory_item_price"))
                .get(0).getText();

        assertEquals("$7.99", primeiroPreco);
    }

    @Test
    void deveExibirDetalhesDoProduto() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("item_4_title_link")).click();

        String nomeProduto = driver.findElement(By.className("inventory_details_name")).getText();
        String descricao = driver.findElement(By.className("inventory_details_desc")).getText();
        String preco = driver.findElement(By.className("inventory_details_price")).getText();

        assertEquals("Sauce Labs Backpack", nomeProduto);
        assertEquals("$29.99", preco);

        assertEquals(false, descricao.isEmpty());
    }

    @Test
    void deveAdicionarProdutoAoCarrinho() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        String quantidade = driver.findElement(By.className("shopping_cart_badge")).getText();

        assertEquals("1", quantidade);
    }

    @Test
    void deveAdicionarMultiplosProdutosAoCarrinho() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt")).click();

        driver.findElement(By.className("shopping_cart_link")).click();

        int quantidadeProdutos = driver.findElements(By.className("cart_item")).size();

        assertEquals(2, quantidadeProdutos);
    }

    @Test
    void deveRemoverProdutoDoCarrinho() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        int quantidadeProdutos = driver.findElements(By.className("cart_item")).size();

        assertEquals(0, quantidadeProdutos);
    }

    @Test
    void deveContinuarComprandoAposAcessarCarrinho() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("continue-shopping")).click();

        assertEquals(
                "https://www.saucedemo.com/inventory.html",
                driver.getCurrentUrl()
        );
    }

    @Test
    void deveImpedirCheckoutSemPreenchimento() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("continue")).click();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Error: First Name is required",
                mensagemErro
        );
    }

    @Test
    void deveImpedirCheckoutSomenteComFirstName() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Hernandes");
        driver.findElement(By.id("continue")).click();

        String mensagemErro = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        assertEquals(
                "Error: Last Name is required",
                mensagemErro
        );
    }

    @Test
    void deveAceitarPreenchimentoDoLastName() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Hernandes");
        driver.findElement(By.id("last-name")).sendKeys("Bury");

        String firstName = driver.findElement(By.id("first-name")).getAttribute("value");

        assertEquals("Hernandes", firstName);
    }

    @Test
    void deveAvancarParaRevisaoComDadosValidos() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Hernandes");
        driver.findElement(By.id("last-name")).sendKeys("Bury");
        driver.findElement(By.id("postal-code")).sendKeys("44380-000");
        driver.findElement(By.id("continue")).click();

        assertEquals(
                "https://www.saucedemo.com/checkout-step-two.html",
                driver.getCurrentUrl()
        );

        String nomeProduto = driver.findElement(By.className("inventory_item_name")).getText();

        assertEquals("Sauce Labs Backpack", nomeProduto);
    }

    @Test
    void deveFinalizarCompraComSucesso() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Hernandes");
        driver.findElement(By.id("last-name")).sendKeys("Bury");
        driver.findElement(By.id("postal-code")).sendKeys("44380-000");
        driver.findElement(By.id("continue")).click();

        driver.findElement(By.id("finish")).click();

        String mensagem = driver.findElement(By.className("complete-header")).getText();

        assertEquals("Thank you for your order!", mensagem);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
