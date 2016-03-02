package Deliverable3;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * As a user,
 * I would like to see reddit links in all sorts of ways,
 * So that I can know what is happening in the world
 * @author wlaboon
 *
 */

public class HackerNewsTester {

	private boolean acceptNextAlert = true;
	private String baseUrl="https://news.ycombinator.com";
	private StringBuffer verificationErrors = new StringBuffer();
	static WebDriver driver = new FirefoxDriver();
	
	// Start at the home page for hackernews for each test
	@Before
	public void setUp() throws Exception {
		
	    driver.get(baseUrl + "/");
	}
	@Test
	public void testShowsCorrectTitle() {
		
		// Simply check that the title contains the word "reddit"
		String title = driver.getTitle();
		assertTrue(title.contains("Hacker News"));
		
	}
	
	//Given that I am on the login page
   	//When I dont have an account
   	//Then I can't create one with a blank username
   	@Test
   	public void testCreateAccountBlankUsername() {
   		try {
   			driver.findElement(By.name("username:")).sendKeys("");
   		} catch (NoSuchElementException nseex) {
			fail();
		}
		WebElement loginDiv = driver.findElement(By.id("Submit"));
   		
		WebElement submitButton = loginDiv.findElement(By.className("Submit"));
		submitButton.click();
   	}
   	
	  @Test
	  public void testLoginNoUsernameNoPassword() throws Exception {
	    driver.findElement(By.linkText("login")).click();
	    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	    Thread.sleep(4000);
	    WebElement body=driver.findElement(By.tagName("body"));
	    String bodyString=body.toString();
	    System.out.println(bodyString);
	    assertTrue(bodyString.contains("Bad login.")); //this does not work yet
	  }
	  
	  @After
	  public void tearDown() throws Exception {
	    //driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }

}
