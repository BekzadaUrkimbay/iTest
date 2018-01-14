package autoTests.TestSiute;

import autoTests.CustomMethods;
import autoTests.pages.main.TemplatePage;
import org.junit.Test;

/**
 * Created by Privat24 on 09.09.2016.
 */
public class dms_0176_Zagran_iGov extends CustomMethods {

    //<editor-fold desc="Тестовый пример заполнение полей">
    @Test
    public void default_test() throws Exception {
        /**
         * ***************************************объявляем элементы страниц******************************************
         */
        TemplatePage o = new TemplatePage();
        //  Вносим в переменные название услуги начиная с точки ._test_fields_bankid_--_ и до начала названия поля
        String sBP = "dms_0176_Zagran_iGov";
        String email = "autotestbeta@gmail.com";

        openURLservice(getBaseUrl() + "/service/176/general");

        o.selectRegion("Дніпропетровська");

        o.mokAuthorization();

        setFieldValue("phone", "+380623155533");
        setFieldValue("email", email);
        setFieldAutocomplete("sID_Public_SubjectOrganJoin", "м. Кам'янське (Дніпровський) пр. Перемоги, 63");
        setFieldSelectSlotDate();
        setFieldSelectSlotTime();

        click(o.buttonSendingForm);

        o.checkMessageSuccess("Шановний(-а) MockUser MockUser!\n"
                + "Ваше звернення х-хххххххх успішно зареєстровано\n"
                + "(номер також відправлено Вам електронною поштою на Ваш e-mail " + email + ") Результати будуть спрямовані також на email.\n"
                + "Звертаємо увагу, що Іноді листи потрапляють у спам або у розділ \"Реклама\" (для Gmail).");

        click(o.buttonLogOut);

        openURLdashboard(sBP);

        AuthorizationBySetLoginPassword("tester", "tester");
        clickButton("Увійти");
        // Опрацювання в табі "В необроблені"    
        setRegionTask();
        clickButton("Взяти в роботу");
        clickButton("Почати опрацювання задачі");

        // Опрацювання [Етап I] 
        SetRegionFieldInputTypeEnum("Громадянин з'явився у призначений час");
        SetRegionFieldInputTypeString("sCancelInfo", "Заявка актуальна");

        clickButton("Опрацювати");
        clickButton("Опрацювати");
        clickButton("Підтвердити");
        clickButton("Ok");

    }
    //</editor-fold>
}
