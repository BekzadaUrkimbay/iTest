/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoTests.TestSiute.iDoc;

import autoTests.CustomMethods;

import static com.codeborne.selenide.Selenide.open;

import org.junit.Test;
/**
 *
 * @author User
 */
public class Test_doc_btsol_protocol_meeting  extends CustomMethods {
    @Test
    public void Test_doc_btsol_protocol_meeting() throws Exception {
        String sBP = "_doc_btsol_protocol_meeting";
        String email = "autotestbeta@gmail.com";

        openURLdashboard(getRegionUrl());
        
        //Авторизация
        AuthorizationBySetLoginPassword("IGOV_270907SVK", " ");
        clickButton("Увійти");
        navigateToggleMenu();
        createDocumentOrTask("Протокол оперативної наради");
        clickButton("Далi");
        pause(5000);
        
            //----Создание---- 
        setDocTitle("Автотест протокола"); 
        setDocContent("Автотест протокола");
        SetRegionFieldInputTypeDate("sDateMeeting", "19.03.2018");
        SetRegionFieldInputTypeLong("sAmtDecisions", "2");
        setSelect("_doc_btsol_protocol_meeting", "sName_Chairperson", "Смоктій Вікторія Кирилівна");
        setParticipant(sBP, "sTable_listener", "sName_isExecute", "0", "Туренко Ольга Володимирівна");
        addRegionsTableRow("sTable_listener");
        setParticipant(sBP, "sTable_listener", "sName_isExecute", "1", "Смоктій Вікторія Кирилівна");
        setTaskContent("Запустить автотест протокола");
        setRegionTableCellsInputTypeString("sNumber", "0", "1");
        setRegionTableCellsInputTypeString("sNameFile", "0", "Тестовий додаток");
        setRegionTableCellsInputTypeFile(sBP,"sTableFile", "sFile", "0", "src/test/resources/files/test.jpg");
        
        //Додати завдання
        addTask();
        setTaskName(generateText(10));
        setTaskTerm("Кiлькiсть днiв пiсля", "3");
        setTaskForm("Текстове повiдомлення");
        SetRegionFieldInputTypeLong("sAmtDecisions", "3");
        setController("Смоктій Вікторія Кирилівна");
        setExecutor("Столбова Анна Юріївна");
        addNewExecutor("Літовченко Інна Вадимівна");  
        pause(5000);
        setTaskContent("Перевірка завдання автотестом");
        
        /*Таблица Узгоджуючі*/
        setAcceptor(sBP, "sTableAccept", "sName_Acceptor", "0", "Туренко Ольга Володимирівна");
        /*addRegionsTableRow("sTableAccept");
        setAcceptor(sBP, "sTableAccept", "sName_Acceptor", "1", "Грек Одарка Олексіївна");
        addRegionsTableRow("sTableAccept");
        setAcceptor(sBP, "sTableAccept", "sName_Acceptor", "2", "Столбова Анна Юріївна");*/
        
        /*Таблица Затверджуючий*/
        setApprover(sBP, "sTableAgree", "sName_Approver", "0", "Герман Август Васильович");
       
        
        //Проверка наличия кнопок
        isExistButton("Зберегти",true);
        isExistButton("Роздрукувати",true);
        isExistButton("Додати завдання",true);
        isExistButton("Інші дії",true);
        clickButton("Інші дії");
        isExistButton("Додати на перегляд",true);
        isExistButton("Видалити",true);
        isExistButton("Зауваження",false);
        isExistButton("Ознайомити",false);
        isExistButton("Додати підписанта",false);
        isExistButton("Делегувати",false);
        isExistButton("Відмовити",false);
        isExistButton("Підпис не потрібен",false);
        isExistButton("Редагувати завдання",false);
        
        //Добавляем на просмотр
        //clickButton("Інші дії");
        addViewer("Літовченко Інна Вадимівна");
        
        pause(1000);
        
        getOrderFromUrlCurrentPage();
        clickButtonCreate();
        pause(5000);
        //clickButton("Ok");
        clickLink("Смоктій Вікторія Кирилівна");
        clickLink("Вийти");        

            //----Подписываем (Согласование)----
        //Первый подписант
        AuthorizationBySetLoginPassword("IGOV_200687TOV", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(5000);
        addComment("Тестове зауваження");
        //clickButton("Ok");       
        clickButton("Інші дії");
        clickButton("Делегувати");
        addDelegate("Гуков Юрій Олександрович");
        pause(2000);
        clickLink("Туренко Ольга Володимирівна");
        clickLink("Вийти");
         
        //Второй подписант(С делегирования)
        AuthorizationBySetLoginPassword("IGOV_151071GUO", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(5000);
        isExistButton("Пiдписати",true);
        isExistButton("Зберегти",true);
        isExistButton("Роздрукувати",true);
        isExistButton("Зауваження",true);
        isExistButton("Інші дії",true);
        clickButton("Інші дії");
        isExistButton("Додати на перегляд",true);
        isExistButton("Ознайомити",true);
        isExistButton("Додати підписанта",true);
        isExistButton("Делегувати",true);
        isExistButton("Відмовити",true);
        isExistButton("Підпис не потрібен",true);
        isExistButton("Додати завдання",false);
        isExistButton("Редагувати завдання",false);
        //clickButton("Інші дії");
        addAcceptor("Грек Одарка Олексіївна");

        clickButtonSign();
        pause(5000);
        clickLink("Гуков Юрій Олександрович");
        clickLink("Вийти");
        
        //подпись еще одним исполнителем (с добавить на согласования)
        AuthorizationBySetLoginPassword("IGOV_130384GOA", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(2000);
        clickButtonSignNotNeed(sBP, "Подпись не нужна");
        clickLink("Грек Одарка Олексіївна");
        clickLink("Вийти");
        
        //Снять подпись 
        AuthorizationBySetLoginPassword("IGOV_130384GOA", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        cancelSign("Перевірка зняття підпису");
        clickLink("Грек Одарка Олексіївна");
        clickLink("Вийти");
        
        //Удалить подписанта
        //AuthorizationBySetLoginPassword("IGOV_151071GUO", " ");
        //clickButton("Увійти");
        //setRegionFindOrderByNumberDocument();
        //pause(5000);
        //removeParticipant(1, true);
        //clickButtonSign();
        //pause(5000);
        //clickLink("Гуков Юрій Олександрович");
        //clickLink("Вийти");
        
        
                //-----Редактирование-----
        //Автору, ответить на замечание и редактировать
        //Проверка наличия кнопок
        AuthorizationBySetLoginPassword("IGOV_270907SVK", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        clickButton("Редагувати");
        pause(5000);
        //Проверка наличия кнопок
        //isExistButton("Пiдписати",true);
        //isExistButton("Зберегти",true);
        //isExistButton("Роздрукувати",true);
        //isExistButton("Додати завдання",false);
        //isExistButton("Інші дії",true);
        //clickButton("Інші дії");
        //isExistButton("Додати на перегляд",true);
        //isExistButton("Видалити",true);
        //isExistButton("Редагувати завдання",true);
        //isExistButton("Зауваження",false);
        //isExistButton("Ознайомити",false);
        //isExistButton("Додати підписанта",false);
        //isExistButton("Делегувати",false);
        //isExistButton("Відмовити",false);
  
        //answerComment("Відповідь на зауваження");
        //pause(1500);
        //clickButton("Ok");
        //Редактирование задачи
        clickButton("Інші дії");
        clickButton("Редагувати завдання");
        SetRegionFieldInputTypeLong("sAmtDecisions", "3");      
        
        clickButtonSign();
        pause(5000);
        clickLink("Смоктій Вікторія Кирилівна");
        clickLink("Вийти");
        
        //Подписи исполнителей задачи
        AuthorizationBySetLoginPassword("IGOV_260185SAU", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(2000);
        clickButtonRefuse(sBP, "Отказать");
        //clickButtonSign();
        pause(5000);
        clickLink("Столбова Анна Юріївна");
        clickLink("Вийти");
        
        AuthorizationBySetLoginPassword("IGOV_230878LIV", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(2000);
        clickButtonSignNotNeed(sBP, "Подпись не нужна");  
        clickLink("Літовченко Інна Вадимівна");
        clickLink("Вийти");
        
        
        
        
        
        //Подписываем (Утверждение)      
        AuthorizationBySetLoginPassword("IGOV_110771GAV", " ");
        clickButton("Увійти");
        setRegionFindOrderByNumberDocument();
        pause(2000);
        clickButton("Інші дії");
        addVisor("Павленко Юлія Юріївна");
        clickButtonSign();
        pause(5000);
        clickLink("Герман Август Васильович");
        clickLink("Вийти");
        
        //Ознакомление
        AuthorizationBySetLoginPassword("IGOV_220290PUU", " ");
        clickButton("Увійти");
        pause(5000);
        setRegionFindOrderByNumberDocument();
        pause(2000);
        clickButton("Ознайомлений");
        pause(5000);
        clickLink("Павленко Юлія Юріївна");
        clickLink("Вийти");
        
        //Ознакомление Автором
        AuthorizationBySetLoginPassword("IGOV_270907SVK", " ");
        clickButton("Увійти");
        pause(2000);
        setRegionFindOrderByNumberDocument();
        pause(5000);
        clickButton("Ознайомлений");
        pause(5000);
        clickLink("Смоктій Вікторія Кирилівна");
        clickLink("Вийти");
        
        //Работа с задачей
    }
}
        