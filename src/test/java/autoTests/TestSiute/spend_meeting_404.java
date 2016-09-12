package autoTests.TestSiute;

import autoTests.ConfigurationVariables;
import autoTests.CustomMethods;
import autoTests.pages.main.TemplatePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.stqa.selenium.factory.WebDriverFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Privat24 on 09.09.2016.
 */
public class spend_meeting_404 extends CustomMethods {

    ConfigurationVariables CV = ConfigurationVariables.getInstance();
    public WebDriver driver;

    DesiredCapabilities capabilities;

    /**************************************************************/
    @BeforeTest(alwaysRun = true)
    public void SetUp() throws IOException {
        /********* Закоментить для  для запуска на своем профиле и откоментить для запуска на дефолтном ***********/
        FirefoxProfile profile = new FirefoxProfile();
        profile.setEnableNativeEvents(false);
        profile.setAcceptUntrustedCertificates(true);

        /********* Раскомментить для запуска на своем профиле и закоментить для дефолтного ***********/
//        ProfilesIni allProfiles = new ProfilesIni();
//        FirefoxProfile profile = allProfiles.getProfile("default");

        profile.setEnableNativeEvents(false);
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("javascript.enabled", true);
        profile.setPreference("geo.enabled", false);

        capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        capabilities.setCapability("unexpectedAlertBehaviour", "ignore");

        System.out.println("Tests will be run (or rerun) in Firefox with custom profile...");


        driver = WebDriverFactory.getDriver(capabilities);
        this.driver.manage().timeouts().implicitlyWait(CV.implicitTimeWait, TimeUnit.SECONDS);
        this.driver.manage().window().maximize();
        this.driver.manage().deleteAllCookies();

    }

    @BeforeMethod(alwaysRun = true)
    public void doLogin() throws Exception {
        driver = WebDriverFactory.getDriver(capabilities);
    }

    //<editor-fold desc="Тестовый пример заполнение полей">
    @Test(enabled = true, groups = {"Main", "Критический функционал"}, priority = 1)
    public void default_test() throws Exception {
        /*****************************************объявляем элементы страниц*******************************************/
        TemplatePage o = new TemplatePage(driver);
        //  Вносим в переменные название услуги начиная с точки ._test_fields_bankid_--_ и до начала названия поля
        String sBP = "spend_meeting_404";
        String email = "smoktii.igov@gmail.com";

        _step("1. Вход по прямому URL на услугу");
        openURLservice(driver, CV.baseUrl + "/service/40/general");

        _step("2. Проверить, что открылась нужная услуга");
        assertThis(driver, o.usluga, "Повідомлення про проведення зборів, мітингів, маніфестацій і демонстрацій, спортивних, видовищних та інших масових заходів");

        _step("3. Выбор области/города");
        o.selectRegion("Київська");
        o.selectCity("Колонщина");

        _step("4. Авторизация Off AuthMock/BankID");
        o.mokAuthorization();

        _step("5. Заполняем форму услуги");
        selectAutocomplete(driver,"sID_Public_SubjectOrganJoin","ЦНАП Колонщинської сільради");
        setFieldValue(driver, sBP, "phone", "+380623155533");
        setFieldValue(driver, sBP, "email", email);
        setFieldValue(driver, sBP, "sMetaZahodu", "Мета заходу");
        setFieldSelectByText(driver,sBP,"eFormaZahodu","мітинг");
        setFieldValue(driver, sBP, "sMistoZahodu", "Місце проведення заходу або маршрути руху");
        setFieldFile(driver, sBP, "fMapViewMarshrut", "src/test/resources/files/test.jpg");
        setFieldValue(driver, sBP, "dDate_Beg", "2016-01-01");
        setFieldSelectByText(driver,sBP,"eTime_Beg","12:00");
        setFieldValue(driver, sBP, "dDate_End", "01.01.2016");
        setFieldSelectByText(driver,sBP,"eTime_End","19:00");
        setFieldValue(driver, sBP, "lCountPeople", "100");
        setFieldFile(driver, sBP, "fPovidomlennyaProZahid", "src/test/resources/files/test.jpg");
        setFieldSelectByText(driver,sBP,"eCountOrganizatorov","1");
        setFieldValue(driver, sBP, "lastName_UA1", "Прізвище");
        setFieldValue(driver, sBP, "firstName_UA1", "Ім'я");
        setFieldValue(driver, sBP, "middleName_UA1", "По батькові");
        setFieldValue(driver, sBP, "sAdress_1", "Адреса реєстрації відповідальної особи 1");
        setFieldValue(driver, sBP, "sphone_1", "Контактний (особистий) номер");
        setFieldValue(driver, sBP, "sPlace_of_Work_1", "Місце роботи (навчання) відповідальної особи 1");
        setFieldSelectByText(driver,sBP,"isRepresentCompany_1","Ні");

        _step("6. Отправка формы");
        click(driver, o.buttonSendingForm);

        _step("7. Проверка сообщения о успешной отправке");
        o.checkMessageSuccess("Шановний(-а) MockUser MockUser!\n" +
                "Ваше звернення х-хххххххх успішно зареєстровано\n" +
                "(номер також відправлено Вам електронною поштою на Ваш e-mail "+email+") Результати будуть спрямовані також на email.\n" +
                "Звертаємо увагу, що Іноді листи потрапляють у спам або у розділ \"Реклама\" (для Gmail).");

        _step("8. Нажать кнопку Выйти");
        click(driver, o.buttonLogOut);
    }
    /*
    public void my_test2() throws Exception {
        TemplatePage o = new TemplatePage(driver);
        String sBP = "spend_meeting_404";
        String email = "smoktii.igov@gmail.com";

        _step("1. Вход по прямому URL на услугу");
        openURLservice(driver, CV.baseUrl + "/service/40/general");

        _step("2. Проверить, что открылась нужная услуга");
        assertThis(driver, o.usluga, "Повідомлення про проведення зборів, мітингів, маніфестацій і демонстрацій, спортивних, видовищних та інших масових заходів");

        _step("3. Выбор области/города");
        o.selectRegion("Київська");
        o.selectCity("Колонщина");

        _step("4. Авторизация Off AuthMock/BankID");
        o.mokAuthorization();

        _step("5. Заполняем форму услуги");
        selectAutocomplete(driver,"sID_Public_SubjectOrganJoin","ЦНАП Колонщинської сільради");
        setFieldValue(driver, sBP, "phone", "+380623155533");
        setFieldValue(driver, sBP, "email", email);
        setFieldValue(driver, sBP, "sMetaZahodu", "Мета заходу");
        setFieldSelectByText(driver,sBP,"eFormaZahodu","мітинг");
        setFieldValue(driver, sBP, "sMistoZahodu", "Місце проведення заходу або маршрути руху");
        setFieldFile(driver, sBP, "fMapViewMarshrut", "src/test/resources/files/test.jpg");
        setFieldValue(driver, sBP, "dDate_Beg", "2016-01-01");
        setFieldSelectByText(driver,sBP,"eTime_Beg","12:00");
        setFieldValue(driver, sBP, "dDate_End", "01.01.2016");
        setFieldSelectByText(driver,sBP,"eTime_End","19:00");
        setFieldValue(driver, sBP, "lCountPeople", "100");
        setFieldFile(driver, sBP, "fPovidomlennyaProZahid", "src/test/resources/files/test.jpg");
        setFieldSelectByText(driver,sBP,"eCountOrganizatorov","2");
        setFieldValue(driver, sBP, "lastName_UA1", "Прізвище");
        setFieldValue(driver, sBP, "firstName_UA1", "Ім'я");
        setFieldValue(driver, sBP, "middleName_UA1", "По батькові");
        setFieldValue(driver, sBP, "sAdress_1", "Адреса реєстрації відповідальної особи 1");
        setFieldValue(driver, sBP, "sphone_1", "Контактний (особистий) номер");
        setFieldValue(driver, sBP, "sPlace_of_Work_1", "Місце роботи (навчання) відповідальної особи 1");
        setFieldSelectByText(driver,sBP,"isRepresentCompany_1","Так");
        setFieldValue(driver, sBP, "sNameCompany_1", "Назва організації 1");
        setFieldValue(driver, sBP, "sedrpou_1", "ЄДРПОУ організації 1");
        setFieldValue(driver, sBP, "lastName_UA2", "Прізвище");
        setFieldValue(driver, sBP, "firstName_UA2", "Ім'я");
        setFieldValue(driver, sBP, "middleName_UA2", "По батькові");
        setFieldValue(driver, sBP, "sAdress_2", "Адреса реєстрації відповідальної особи 2");
        setFieldValue(driver, sBP, "sphone_2", "Контактний (особистий) номер 2");
        setFieldValue(driver, sBP, "sPlace_of_Work_2", "Місце роботи (навчання) відповідальної особи 2");
        setFieldSelectByText(driver,sBP,"isRepresentCompany_2","Ні");

        _step("6. Отправка формы");
        click(driver, o.buttonSendingForm);

        _step("7. Проверка сообщения о успешной отправке");
        o.checkMessageSuccess("Шановний(-а) MockUser MockUser!\n" +
                "Ваше звернення х-хххххххх успішно зареєстровано\n" +
                "(номер також відправлено Вам електронною поштою на Ваш e-mail "+email+") Результати будуть спрямовані також на email.\n" +
                "Звертаємо увагу, що Іноді листи потрапляють у спам або у розділ \"Реклама\" (для Gmail).");

        _step("8. Нажать кнопку Выйти");
        click(driver, o.buttonLogOut);
    } */

    //</editor-fold>
}
