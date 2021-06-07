import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class HomeWork3Tests {

    private Logger logger = LogManager.getLogger(HomeWork3Tests.class);
    protected static WebDriver driver;
    ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @Before
    public void startTests() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Browser opened");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @After
    public void endTests() {
        if (driver != null)
            driver.quit();
        logger.info("Browser closed");
    }

    @Test
    public void testYandexMarket() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();

        //Открываем в Chrome сайт Яндекс.Маркет - "Электроника" -> "Смартфоны"
        openPage(cfg.yandexMarket());
        //Локаторы писались под регион Москва, если регион отличается, будет выбран нужный
        if (!driver.findElement(By.xpath("//*[@data-tid-prop='99fc66c']//*[@class='_6RmNBByo8N']")).getText().equals("Москва")) {
            driver.findElement(By.xpath("//*[@data-tid-prop='99fc66c']//*[@class='_6RmNBByo8N']")).click();
            driver.findElement(By.xpath("//*[@placeholder='Укажите другой регион']")).sendKeys("Москва");
            driver.findElement(By.xpath("//*[@data-tid='95d685a1'][1]")).click();
            driver.findElement(By.xpath("//button[@data-tid='42de86b']//*[text()='Продолжить с новым регионом']")).click();
        }


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@href='/catalog--elektronika/54440']"))).click();
        //driver.findElement(By.xpath("//*[@href='/catalog--elektronika/54440']")).click();
        logger.info("Went to the 'Elektronika'");
        driver.findElement(By.cssSelector("._2qvOOvezty._2x2zBaVN-3._9qbcyI_fyS")).click();
        logger.info("Went to the 'Smartfoni'");
        //Отфильтровать список товаров: Samsung и Xiaomi
        driver.findElement(By.xpath("//*[@id='7893318_153061']/..")).click();
        logger.info("Samsung selected");
        driver.findElement(By.xpath("//*[@id='7893318_7701962']/..")).click();
        logger.info("Xiaomi selected");
        //Отсортировать список товаров по цене (от меньшей к большей)
        driver.findElement(By.xpath("//*[@data-autotest-id='dprice']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@data-tid='8bc8e36b']")));
        logger.info("List sorted");
        //Добавить первый в списке Samsung
        var firstSamsung = "(//*[@data-zone-name='snippetList']//article[@data-autotest-id='product-snippet']//span[text()[contains(.,'Samsung')]])[1]";
        var samsungCompareButton = firstSamsung + "/ancestor::*[@data-autotest-id='product-snippet']//*[@class='_1CXljk9rtd' and @data-tid='64a067c1']";
        driver.findElement(By.xpath(samsungCompareButton)).click();
        logger.info("Samsung added");
        //Проверить, что отобразилась плашка "Товар {имя товара} добавлен к сравнению"
        var compareSamsung = "//*[@data-tid='11882e1c']//*[text()[contains(.,'добавлен к сравнению')] and text()[contains(.,'Samsung')]]";
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(compareSamsung))).isDisplayed());
        logger.info("Comparison is visible");
        //Добавить первый в списке Xiaomi
        var firstXiaomi = "(//*[@data-zone-name='snippetList']//article[@data-autotest-id='product-snippet']//span[text()[contains(.,'Xiaomi')]])[1]";
        var xiaomiCompareButton = firstXiaomi + "/ancestor::*[@data-autotest-id='product-snippet']//*[@class='_1CXljk9rtd' and @data-tid='64a067c1']";
        driver.findElement(By.xpath(xiaomiCompareButton)).click();
        logger.info("Xiaomi added");
        //Проверить, что отобразилась плашка "Товар {имя товара} добавлен к сравнению"
        var compareXiaomi = "//*[@data-tid='11882e1c']//*[text()[contains(.,'добавлен к сравнению')] and text()[contains(.,'Xiaomi')]]";
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(compareXiaomi))).isDisplayed());
        logger.info("Comparison is visible");
        //Перейти в раздел Сравнение
        var compareButton = "//*[@data-tid='11882e1c']//*[@href='/my/compare-lists']";
        var buttonVisible = true;
        while (buttonVisible) {
            try {
                driver.findElement(By.xpath(compareButton)).click();
            } catch (ElementClickInterceptedException e) {
                logger.info(e);
            }
            try {
                driver.findElement(By.xpath(compareButton)).isDisplayed();
            } catch (NoSuchElementException e) {
                buttonVisible = false;
            }
        }
        logger.info("Moved to comparison");
        //Проверить, что в списке товаров 2 позиции
        var compareList = driver.findElements(By.xpath("//*[@data-tid='412661c']"));
        Assert.assertEquals(2, compareList.size());
    }

    @Test
    public void test220Volt() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        Actions action = new Actions(driver);
        driver.manage().window().maximize();

        //Открываем сайт www.220-volt.ru
        openPage(cfg.store220Volt());
        //Переходим в Электроэнструменты -> Перфораторы
        action.moveToElement(driver.findElement(By.xpath("//a[@title='Электроинструменты']"))).build().perform();
        driver.findElement(By.xpath("//a[@title='Перфораторы' and text()='Перфораторы']")).click();
        //Выбрать марки Makita и Зубр
        driver.findElement(By.xpath("//input[@title='MAKITA']")).click();
        driver.findElement(By.xpath("//input[@title='ЗУБР']")).click();
        driver.findElement(By.xpath("//*[@value='Подобрать модель']")).click();
        //Отсортировать по цене (min->max)
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='select2-listing']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='select2-results__option']"))).click();
        //Добавить в сравнение первый перфоратор "Зубр" и первый перфоратор "Makita"
        var firstZubrXp = "(//*[@id='product-list']//*[text()[contains(.,('ЗУБР'))]])[1]";
        var firstMakitaXp = "(//*[@id='product-list']//*[text()[contains(.,('MAKITA'))]])[1]";
        var zubrName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(firstZubrXp)))
                .getText().replaceAll("Перфоратор ", "");
        var makitaName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(firstMakitaXp)))
                .getText().replaceAll("Перфоратор ", "");
        driver.findElement(By.xpath(firstZubrXp + "/parent::*/parent::*//*[@title='Добавить к сравнению']")).click();
        driver.findElement(By.xpath("//*[text()[contains(.,('Продолжить просмотр'))]]")).click();
        try {
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException e) {
            driver.findElement(By.xpath(firstMakitaXp + "/parent::*/parent::*//*[@title='Добавить к сравнению']")).click();
        }
        //Перейти на страницу сравнения. Убедиться, что в сравнении корректные перфораторы.
        driver.findElement(By.xpath("//*[text()='Перейти к сравнению' and @href='/compare/']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()[contains(.,'" + zubrName + "')]]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()[contains(.,'" + makitaName + "')]]")).isDisplayed());
    }

    private void openPage(String address) {
        driver.get(address);
        logger.info("Page '" + address + "' opened");
    }
}
