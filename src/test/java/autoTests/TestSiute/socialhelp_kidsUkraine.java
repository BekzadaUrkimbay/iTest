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
import org.apache.tools.ant.taskdefs.Sleep;

/**
 * Created by Privat24 on 09.09.2016.
 */
public class socialhelp_kidsUkraine extends CustomMethods {


    //<editor-fold desc="Тестовый пример заполнение полей">
    @Test(enabled = true, groups = {"Main", "Критический функционал"}, priority = 1)
    public void default_test() throws Exception {
        /*****************************************объявляем элементы страниц*******************************************/
        TemplatePage o = new TemplatePage(driver);
        //  Вносим в переменные название услуги начиная с точки ._test_fields_bankid_--_ и до начала названия поля
        String sBP = "socialhelp_kidsUkraine";
        String email = "autotestbeta@gmail.com";

        _step("1. Вход по прямому URL на услугу");
        openURLservice(driver, CV.baseUrl + "/service/752/general");

        _step("3. Выбор области/города");
        o.selectRegion("Дніпропетровська");

        _step("4. Авторизация Off AuthMock/BankID");
        o.mokAuthorization();

        _step("5. Заполняем форму услуги");
        setFieldAutocomplete(driver,"sID_Public_SubjectOrganJoin","Бабушкінській район, м.Дніпропетровськ");
        setFieldValue(driver, sBP, "phone", "+380623155533");
        setFieldValue(driver, sBP, "email", email);
        setFieldValue(driver, sBP, "adress1", "Ваша адреса реєстрації");
        setFieldValue(driver, sBP, "adress2", "Ваша адреса проживання");
        setFieldFile(driver, sBP, "nNoHelp", "src/test/resources/files/test.jpg");
        setFieldFile(driver, sBP, "inn_scan", "src/test/resources/files/test.jpg");
        setFieldFile(driver, sBP, "birthdocument_scan", "src/test/resources/files/test.jpg");
        setFieldSelectByText(driver,sBP,"transfer_type","через національного оператора поштового зв'язку");
        setFieldSelectByText(driver,sBP,"ECP","без ЕЦП (погоджуєтесь на особистий візит після перевірки правильності даних)");
        setFieldValue(driver, sBP, "post_office", "12000");

        _step("6. Отправка формы");
        click(driver, o.buttonSendingForm);

        _step("7. Проверка сообщения о успешной отправке");
        o.checkMessageSuccess("Шановний(-а) MockUser MockUser!\n" +
                "Ваше звернення х-хххххххх успішно зареєстровано\n" +
                "(номер також відправлено Вам електронною поштою на Ваш e-mail "+email+") Результати будуть спрямовані також на email.\n" +
                "Звертаємо увагу, що Іноді листи потрапляють у спам або у розділ \"Реклама\" (для Gmail).");

        _step("7-1. Нажать кнопку Выйти");
        click(driver, o.buttonLogOut);

        _step("8. Вход по прямому URL на дашборд");
        openURLdashboard(driver, sBP);

        _step("9. Авторизация login/BankID на дашборде. login/pass: (tester/tester)");
        AuthorizationBySetLoginPassword(driver, sBP, "tester", "tester");
        clickButton(driver, sBP, "Увійти");

        // Опрацювання в табі "В необроблені"    

        setRegionTask(driver, sBP);
        clickButton(driver, sBP, "Взяти в роботу");
        clickButton(driver, sBP, "Почати опрацювання задачі");
        
        // Опрацювання [Етап I] 

        SetRegionFieldInputTypeEnum(driver, sBP, "decision_step1", "Заповнені дані вірні, інформація направлена на розгляд, очікується рішення");
        SetRegionFieldInputTypeTextArea(driver, sBP, "comment1", "Коментар до рішення");


        clickButton(driver, sBP, "Опрацювати");
        clickButton(driver, sBP, "Підтвердити");
        clickButton(driver, sBP, "Ok");
        
        // Опрацювання [Етап II]

//        setRegionTask(driver, sBP);
        clickButton(driver, sBP, "Опрацювати");
        clickButton(driver, sBP, "Почати опрацювання задачі");

        SetRegionFieldInputTypeTextArea(driver, sBP, "comment2", "Коментар до заяви на етапі 2");
        SetRegionFieldInputTypeEnum(driver, sBP, "decision_step2", "Рішення отримане, допомогу призначено");
        SetRegionFieldInputTypeFile(driver, sBP, "document", "src/test/resources/files/test.jpg");
//
        clickButton(driver, sBP, "Опрацювати");
        clickButton(driver, sBP, "Підтвердити");
        clickButton(driver, sBP, "Ok");
      
     }
    //</editor-fold>
}
