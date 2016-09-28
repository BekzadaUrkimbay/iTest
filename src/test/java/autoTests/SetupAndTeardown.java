package autoTests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import ru.stqa.selenium.factory.WebDriverFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SetupAndTeardown {

    public WebDriver driver;
    DesiredCapabilities capabilities;
    public ConfigurationVariables CV = ConfigurationVariables.getInstance();



    @BeforeMethod(alwaysRun = true)
    public void SetUp() throws IOException {
        if (null == driver) {
            /********* Закоментить для  для запуска на своем профиле и откоментить для запуска на дефолтном ***********/
          FirefoxProfile profile = new FirefoxProfile();
          profile.setEnableNativeEvents(false);
          profile.setAcceptUntrustedCertificates(true);

            /********* Раскомментить для запуска на своем профиле и закоментить для дефолтного ***********/
 //           ProfilesIni allProfiles = new ProfilesIni();
//            FirefoxProfile profile = allProfiles.getProfile("default");

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
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenshot(ITestResult result) throws Exception {
        //Для того чтобы передавать html теги и спец-символы в reporter.log
        //Или можно передать параметр в командную строку при выполнении TestNG:
        //-Dorg.uncommons.reportng.escape-output=false
        //System.setProperty("org.uncommons.reportng.escape-output", "false");

        Reporter.setCurrentTestResult(result);
        boolean success = (new File("TestReport/html/Screens/")).mkdirs();

        Calendar calendar = new CustomMethods().getCurrentCalendar();
        String SuccsessLogMessage =
                "The test - \"" +
                        result.getMethod().getMethodName().toString() +
                        "\" was successfully ended" +
                        "(" +
                        calendar.get(Calendar.DATE) +
                        "." +
                        (calendar.get(Calendar.MONTH) + 1) +
                        "." +
                        calendar.get(Calendar.YEAR) +
                        " " +
                        calendar.get(Calendar.HOUR_OF_DAY) +
                        ":" +
                        calendar.get(Calendar.MINUTE) +
                        ":" +
                        calendar.get(Calendar.SECOND) +
                        ")";

        String ErrorLogMessage =
                "The test - \"" +
                        result.getMethod().getMethodName().toString() +
                        "\" was failed!" +
                        "(" +
                        calendar.get(Calendar.DATE) +
                        "." +
                        (calendar.get(Calendar.MONTH) + 1) +
                        "." +
                        calendar.get(Calendar.YEAR) +
                        " " +
                        calendar.get(Calendar.HOUR_OF_DAY) +
                        ":" +
                        calendar.get(Calendar.MINUTE) +
                        ":" +
                        calendar.get(Calendar.SECOND) +
                        ")";

        try {
            if (!result.isSuccess()) {
                try {
                    FileOutputStream fileOuputStream = new FileOutputStream("TestReport/html/Screens/" + result.getMethod().getMethodName() + ".png");
                    fileOuputStream.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
                } catch (Exception e) {
                    System.out.println(e);
                }
                Reporter.log(
                        "<center>Скриншот снят при падении теста "
                                + result.getMethod().getMethodName() + ".png"
                                + ", URL = "
                                + driver.getCurrentUrl()
                                + "<br><div><a target=\"_blank\" href=\"Screens/"
                                + result.getMethod().getMethodName()
                                + ".png\"><img  style=\"height:400px; width: 600px;\"  src=\"" + "Screens/"
                                + result.getMethod().getMethodName()
                                + ".png"
                                + "\"></a></div><center><br><br>",
                        true
                );
                System.out.println(ErrorLogMessage);
                

            } else {
                System.out.println(SuccsessLogMessage);
                Reporter.log(SuccsessLogMessage);
            }
        } catch (Exception e) {
            CustomMethods.addErrorToTheReport("Connection with browser was lost.");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void deleteFiles() throws Exception {
        if (!WebDriverFactory.isEmpty()) WebDriverFactory.dismissAll();

        //Удаляем временные папки и файлы...
        File directory = new File("target");
        CustomMethods.deleteFileOrDirectory(directory);

        directory = new File("surefire");
        CustomMethods.deleteFileOrDirectory(directory);
        
         }
    }
