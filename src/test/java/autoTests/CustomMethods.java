package autoTests;



import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import ru.stqa.selenium.factory.WebDriverFactory;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


public class CustomMethods extends SetupAndTeardown
{

	ConfigurationVariables configVariables = ConfigurationVariables.getInstance();








	//Открыть новую вкладку
	public void openNewTab(WebDriver driver)
	{
		WebElement body = driver.findElement(By.tagName("body"));
		body.sendKeys(Keys.CONTROL + "t");
	}

	//Закрыть вкладку
	public void closeTab(WebDriver driver)
	{
		WebElement body = driver.findElement(By.tagName("body"));
		body.sendKeys(Keys.CONTROL + "w");

		ArrayList <String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));

	}

	public int getRandomNumber(int n)
	{
		Random random = new Random();
		int RandomNumber = random.nextInt(n);
		return RandomNumber;
	}

	public void waitForElementRemoved(WebDriver driver, By locator, int timeoutInSeconds, int pollingInterval)
			throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds, pollingInterval);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForElementRemoved(WebDriver driver, WebElement webElement, int timeoutInSeconds, int pollingInterval)
			throws Exception
	{
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		boolean flag = true;
		int counter = 0;
		while(flag)
		{
			if (counter> (int)(timeoutInSeconds*1000/pollingInterval))
			{
				flag = false;
				throw new Exception ("Ошибка во время выполнения теста. " +
						"В метод waitForElementRemoved передан WebElement " +
						webElement +
						" который не удаляется"
				);
			}
			try
			{
				Thread.sleep(pollingInterval);
				counter++;
				if (!webElement.isDisplayed()) flag = false;
			}
			catch (Exception e)
			{
				flag = false;
			}
		}
		driver.manage().timeouts().implicitlyWait(configVariables.implicitTimeWait, TimeUnit.SECONDS);
	}

	public void waitForElementPresent(WebDriver driver, By locator, int timeoutInSeconds, int pollingInterval)
			throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds, pollingInterval);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
	}

	public void waitForElementPresent(WebDriver driver, WebElement webElement, int timeoutInSeconds, int pollingInterval)
			throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds, pollingInterval);
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}

	public Calendar getCurrentCalendar()
	{
		// http://docs.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html
		// get the supported ids for GMT+02:00 ("Среднеевропейское (Центральноевропейское) летнее время")

		String[] ids = TimeZone.getAvailableIDs(+2 * 60 * 60 * 1000);
		// if no ids were returned, something is wrong. get out.
		if (ids.length == 0) System.exit(0);
		// create a (CEST - Central Europe Summer Time Zone) UTC/GMT+2 time zone
		SimpleTimeZone GMT = new SimpleTimeZone(+2 * 60 * 60 * 1000, ids[0]);
		// create a GregorianCalendar with the current date and time
		Calendar calendar = new GregorianCalendar(GMT);
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		return calendar;

	}

	public static void delete(File file)  throws IOException
	{
		if(file.isDirectory())
		{
			//directory is empty, then delete it
			if(file.list().length==0)
			{
				file.delete();
				//System.out.println("Directory is deleted : " + file.getAbsolutePath());
			}
			else
			{
				//list all the directory contents
				String files[] = file.list();
				for (String temp : files)
				{
					//construct the file structure
					File fileDelete = new File(file, temp);
					//recursive delete
					delete(fileDelete);
				}
				//check the directory again, if empty then delete it
				if(file.list().length==0)
				{
					file.delete();
					//System.out.println("Directory is deleted : " + file.getAbsolutePath());
				}
			}

		}
		else
		{
			//if file, then delete it
			file.delete();
		}
	}

	public static void deleteFileOrDirectory(File directory)
	{
		//make sure directory exists
		if(!directory.exists())
		{
			//System.out.println("Directory "+directory+" does not exist.");
		}
		else
		{
			try
			{
				delete(directory);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	//convert from UTF-8 -> internal Java String format
	public String convertFromUTF8(String s)
	{
		String out = null;
		try
		{
			out = new String(s.getBytes("Windows-1251"), "UTF-8");
		}
		catch (java.io.UnsupportedEncodingException e)
		{
			return null;
		}
		return out;
	}

	//convert from internal Java String format -> UTF-8
	public String convertToUTF8(String s)
	{
		String out = null;
		try
		{
			out = new String(s.getBytes("UTF-8"), "Windows-1251");
		}
		catch (java.io.UnsupportedEncodingException e)
		{
			return null;
		}
		return out;
	}

	//Проверяем что элемент присутствует и видем
	public void CheckElementPresent(WebElement element) throws InterruptedException
	{
		Assert.assertEquals(true, element.isDisplayed());
		Assert.assertEquals(true, element.isEnabled());
	}

	public void _step(String stepName) throws Exception
	{
		Reporter.log("<b>" + stepName + "</b><br>");
	}

	public static void addTestNameToTheReport(String testName, String methodPath) throws Exception
	{
		methodPath = methodPath.substring(0, methodPath.indexOf("("));

		//получим id теста
		String testId = methodPath.substring(methodPath.lastIndexOf(".") + 1, methodPath.length());

		//отделим имя теста от имени класса символом '#'
		StringBuilder tempPath = new StringBuilder(methodPath);
		methodPath =
				tempPath.substring(0, methodPath.lastIndexOf(".")) +
				URLEncoder.encode("#", "UTF8") +
				tempPath.substring(methodPath.lastIndexOf(".") + 1, methodPath.length());

		Reporter.log(
			"<form id = \"" + testId + "form\" action= \"\" method=\"post\">\n" +
					"<font color=\"blue\" size=\"3\">" + testName + "</font>\n" +
					"<input type=\"Submit\" value=\"Выполнить\">\n" +
					"</form> \n" +
					"<script type=\"text/javascript\">\n" +
					"\tvar currentURL = document.URL;\n" +
					"\tcurrentURL = currentURL.substring(0,currentURL.indexOf(\"/HTML_Report/\"));\n" +
					"var jobNameStartIndex = currentURL.indexOf(\"AT.SELENIUM.\");\n" +
					"while(currentURL.lastIndexOf(\"/\") > jobNameStartIndex)\n" +
					"  currentURL = currentURL.substring(0,currentURL.lastIndexOf(\"/\")); "+
					"\tdocument.getElementById('" + testId +
					"form').action = currentURL + \"/buildWithParameters?suiteFile=testng.xml&test=" + methodPath + "\";\n" +
					"</script>"
		);
	}

	public static void addErrorToTheReport(String testName) throws Exception
	{
		Reporter.log("<b><font color=\"red\" size=\"3\">" + testName + "</font></b><br>");
	}

	// Method for file attachment
	public void attachDocument(WebElement locator, String document, WebDriver driver) {
		String script = "var element = arguments[0];" + "element.style.display='inline';";
		((JavascriptExecutor) driver).executeScript(script, locator);

		File file = new File(document);
		locator.sendKeys(file.getAbsolutePath());

		// Wait attach upload
		//TODO: add counter condition to avoid infinite loop
		while (!locator.isEnabled()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void pause(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void click(WebDriver driver, WebElement webElement){
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
		webElement.click();
	}

	public void click(WebDriver driver, String serviceName,String cssSelector){
		WebElement webElement = driver.findElement(By.cssSelector(serviceName + cssSelector));
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
		webElement.click();
	}

    public void clickXpath(WebDriver driver, String xpath){
        WebElement webElement = driver.findElement(By.xpath(xpath));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.click();
    }


	public void openURLservice(WebDriver driver, String url){
		driver.get(url);
	}
        
        public void openURLdashboard(WebDriver driver, String url){
		driver.get(url);
        }

	public void assertThis(WebDriver driver, WebElement webElement, String textAssert){
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(webElement));
		Assert.assertEquals(webElement.getText(), textAssert);
	}

    public void setFieldValue(WebDriver driver,String serviceName, String cssSelector, String value){
        WebElement webElement = driver.findElement(By.cssSelector("."+serviceName+"_--_"+cssSelector));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.click();
        webElement.clear();
        webElement.sendKeys(value);
    }
    public void setFieldFile(WebDriver driver,String serviceName, String cssSelector, String sPathFile){
        WebElement oWebElement = driver.findElement(By.cssSelector("."+serviceName+"_--_"+cssSelector+" input"));
        String sScript = "var element = arguments[0];" + "element.style.display='inline';";
        ((JavascriptExecutor) driver).executeScript(sScript, oWebElement);
        
        File oFile = new File(sPathFile);
        oWebElement.sendKeys(oFile.getAbsolutePath());

        // Wait attach upload
        //TODO: add counter condition to avoid infinite loop
        while (!oWebElement.isEnabled()) {
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }        
    }
    //public void attachDocument(WebElement locator, String document, WebDriver driver) {
    //}    

    public String getText(WebDriver driver, WebElement webElement) throws Exception {
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(webElement));
        String answer = null;
        try {
            answer = webElement.getText();
        }catch (Exception e){
            throw new Exception(e);
        }
        return answer;
    }

    public void setFieldSelectByText(WebDriver driver,String serviceName, String cssSelector, String text) {
        WebElement webElement = driver.findElement(By.cssSelector("."+serviceName+"_--_"+cssSelector));
        Select select = new Select(webElement);
        select.selectByVisibleText(text);
    }

    public void setFieldSelectByValue(WebDriver driver,String serviceName, String cssSelector, String value) {
        WebElement webElement = driver.findElement(By.cssSelector("."+serviceName+"_--_"+cssSelector));
        Select select = new Select(webElement);
        select.selectByValue(value);
    }

    public void setFieldAutocomplete(WebDriver driver,String name, String value) {
        driver.findElement(By.name(name)).click();
        driver.findElement(By.name(name)).sendKeys(value);
        driver.findElement(By.linkText(value)).click();
    }

    public void setFieldCalendar (WebDriver driver,String serviceName, String cssSelector, String data) {

        WebElement webElement = driver.findElement(By.cssSelector("."+serviceName+"_--_"+cssSelector));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
        ((JavascriptExecutor) driver).executeScript("angular.element(document.getElementsByName('"+cssSelector+"')[0]).removeAttr('readonly');");
        webElement.click();
        webElement.clear();
        webElement.sendKeys(data);

    }
    
    public void setFieldCheckBox(WebDriver driver, String serviceName, String cssSelector) {
        WebElement webElement = driver.findElement(By.cssSelector("#" + cssSelector)); // //*[@id="bFavorite11"] //*[@id="field-bWrite"]/div
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.click();
    }
     
     // TODO Как будет нормальный локатор для выбора времени  Пример: ._test_all_case_--_visitDate переделать метод и перенести в кастом
    public void setFieldSelectSlotDate(WebDriver driver, String serviceName, String cssSelector) {
        Boolean status;
        try {
            WebElement webElement = driver.findElement(By.xpath("//select[@ng-model='selected.date']")); //By.xpath("//select[@ng-model='selected.date']")
            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
            Select select = new Select(webElement);
            select.selectByValue("0");
            status= true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            status= false;
        }
        Assert.assertTrue(status, "NO AVAILABLE SLOTS DATE!, NO AVAILABLE SLOTS DATE =!, NO AVAILABLE SLOTS DATE!");

    }

    // TODO Как будет нормальный локатор для выбора времени  Пример: ._test_all_case_--_visitTime переделать метод и перенести в кастом
    public void setFieldSelectSlotTime(WebDriver driver, String serviceName, String cssSelector) {
        Boolean status;
        try {
            WebElement webElement = driver.findElement(By.xpath("//select[@ng-model='selected.slot']")); //By.xpath("//select[@ng-model='selected.slot']")
            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
            Select select = new Select(webElement);
            select.selectByValue("0");
            status= true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            status= false;
        }
        Assert.assertTrue(status, "NO AVAILABLE SLOTS TIME!, NO AVAILABLE SLOTS TIME!, NO AVAILABLE SLOTS TIME!");

    }
     
    // Methods for filling the table
    
    public void setTableCellsInputTypeString(WebDriver driver,String serviceName, String tableName, String cellName, String NameRow, String text){
        WebElement td = driver.findElement(By.cssSelector("#field-" + tableName + " input[name=" + cellName + NameRow + "]"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
        td.click();
        td.clear();
       td.sendKeys(text);
    }
    
 public void setTableCellsInputTypeSelect(WebDriver driver, String serviceName, String tableName, String cellName, String NameRow, String row) {
        WebElement td = driver.findElement(By.cssSelector("#field-" + tableName + " div[name=" + cellName + NameRow + "]"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
        td.click();
        driver.findElement(By.cssSelector("#ui-select-choices-row" + row + " > a")).click(); //#ui-select-choices-row-1-1 > a #ui-select-choices-row-7-1 > a

    }
    public void setFieldTypeSelect(WebDriver driver, String serviceName, String cssSelector, String row) {
        WebElement webElement = driver.findElement(By.cssSelector("#field-" + cssSelector));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.click();
        driver.findElement(By.cssSelector("#ui-select-choices-row" + row + " > a")).click(); //#ui-select-choices-row-1-1 > a #ui-select-choices-row-7-1 > a

    }
    
    public void setTableCellsInputTypeEnum(WebDriver driver, String serviceName, String tableName, String cellName, String NameRow, String text) {
//        WebElement td = driver.findElement(By.cssSelector("#field-"+tableName+" option[value=\""+value+"\"]"));
        WebElement td = driver.findElement(By.cssSelector("#field-" + tableName + " select[name=" + cellName + NameRow + "]"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
        td.click();
        Select select = new Select(td);
        select.selectByVisibleText(text);
    }
    public void setTableCellsSelectUp(WebDriver driver, String serviceName, String cssSelector) {
        WebElement td = driver.findElement(By.cssSelector(cssSelector));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
//        td.click();
        Select select = new Select(td);
        
    }
    public void setTableCellsTypeCalendar(WebDriver driver, String serviceName, String tableName, String cellName, String NameRow, String date) {

        WebElement td = driver.findElement(By.cssSelector("#field-" + tableName + " input[name=" + cellName + NameRow + "]"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
        ((JavascriptExecutor) driver).executeScript("angular.element(document.getElementsByName('" + cellName + NameRow + "')[0]).removeAttr('readonly');");
        td.click();
        td.clear();
        td.sendKeys(date);

    }
    
    public void setTableCellsInputTypeFile(WebDriver driver, String serviceName, String tableName, String cellName, String nameRow, String sPathFile) {
        if (nameRow.equals("0")) {
            WebElement fileInput = driver.findElement(By.cssSelector("." + serviceName + "_--_" + tableName + "_--_COL_" + cellName + "_--_ROW_" + nameRow + " p[name="+cellName+"] input"));
            String sScript = "var element = arguments[0];" + "element.style.display='inline';";
        ((JavascriptExecutor) driver).executeScript(sScript, fileInput);
        
        File oFile = new File(sPathFile);
        fileInput.sendKeys(oFile.getAbsolutePath());
        } else {
            WebElement fileInput = driver.findElement(By.cssSelector("." + serviceName + "_--_" + tableName + "_--_COL_" + cellName + "_--_ROW_" + nameRow + " p[name="+cellName+"_"+nameRow+"] input"));
            String sScript = "var element = arguments[0];" + "element.style.display='inline';";
        ((JavascriptExecutor) driver).executeScript(sScript, fileInput); 
        
        File oFile = new File(sPathFile);
        fileInput.sendKeys(oFile.getAbsolutePath());
        }
      
    }
        
    public void addTableRow(WebDriver driver, String serviceName, String tableName){
    WebElement td = driver.findElement(By.cssSelector("#field-" + tableName + " a"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(td));
        td.click();
   }
   
    public void AuthorizationBySetLoginPassword(WebDriver driver, String serviceName, String loginName, String passwordName) {
        String windowHandler = driver.getWindowHandle();
        WebElement elementLogin = driver.findElement(By.name("login"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(elementLogin));
        elementLogin.click();
        elementLogin.clear();
        elementLogin.sendKeys(loginName);
        WebElement elementPassword = driver.findElement(By.name("password"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(elementPassword));
        elementPassword.click();
        elementPassword.clear();
        elementPassword.sendKeys(passwordName);
    }

    public void setRegionFindOrder(WebDriver driver, String serviceName, String sID_Order) {
        WebElement element = driver.findElement(By.cssSelector("table .find-field-tooltip input"));
        element.click();
        element.clear();
        element.sendKeys(sID_Order);
    }

    public void clickButton(WebDriver driver, String serviceName, String nameButton) {
        WebElement button = driver.findElement(By.xpath(".//button[contains(text(),'" + nameButton + "')]"));//findElement(By.cssSelector("input[type=\"submit\"]")).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public void findOrderByNumber(WebDriver driver, String serviceName, String sID_Order) {
        WebElement element = driver.findElement(By.xpath(".//*[contains(text(),'" + sID_Order + "')]"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void setRegionTab(WebDriver driver, String serviceName, String enumRegionTab) {
        WebElement element = driver.findElement(By.xpath(".//a[@id='"+enumRegionTab+" ']"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    
    public void getNumbersIdOrder(WebDriver driver, String serviceName, String enumRegionTab) {
        WebElement element = driver.findElement(By.cssSelector("a .ng-binding"));
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    
}
