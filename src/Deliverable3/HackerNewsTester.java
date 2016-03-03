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
	    String body=driver.findElement(By.tagName("body")).getText();
	    assertTrue(body.contains("Bad login.")); //this does not work yet
	  }
	  
	  @Test
	  public void testAddCommentNotLoggedIn() throws Exception {
	    driver.findElement(By.linkText("comments")).click();
	    driver.findElement(By.linkText("parent")).click();
	    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	    String error=driver.findElement(By.tagName("body")).getText();
	    assertTrue(error.contains("You have to be logged in to comment."));
	  }
	  @Test
	  public void testSubmitNotLoggedIn() throws Exception {
	    driver.findElement(By.linkText("submit")).click();
	    String error=driver.findElement(By.tagName("body")).getText();
	    assertTrue(error.contains("You have to be logged in to submit."));
	  }
	  
	  @Test
	  public void testSubmitLoggedIn() throws Exception {
		logIn();
	    driver.findElement(By.linkText("submit")).click();
	    Thread.sleep(4000);
	   boolean exists=!driver.findElements(By.name("text")).isEmpty() && !driver.findElements(By.name("url")).isEmpty() && !driver.findElements(By.name("title")).isEmpty();
	    assertTrue(exists);
	  }
	  
	  public void testCanYouUpvoteACommentWhileLoggedIn() throws Exception {
	    driver.get(baseUrl + "/news");
	    logIn();
	    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	    driver.findElement(By.linkText("comments")).click();
	    driver.findElement(By.cssSelector("div.votearrow")).click();
	    driver.findElement(By.linkText("logout")).click();
	    logOut();
	  }
	  
	  @Test
	  public void testLoginBadCredentials() throws Exception {
	    driver.get(baseUrl + "/login");
	    driver.findElement(By.name("acct")).clear();
	    driver.findElement(By.name("acct")).sendKeys("Monkey");
	    driver.findElement(By.name("pw")).clear();
	    driver.findElement(By.name("pw")).sendKeys("MOnkey");
	    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	  }
	  
	  @Test
	  public void testCreateAccBlankUN() throws Exception {
	    driver.get(baseUrl + "/login?goto=news");
	    driver.findElement(By.xpath("(//input[@name='pw'])[2]")).clear();
	    driver.findElement(By.xpath("(//input[@name='pw'])[2]")).sendKeys("Monkey");
	    driver.findElement(By.xpath("//input[@value='create account']")).click();
	  }
	  
	  @Test
	  public void testCreateAccBadPass() throws Exception {
	    driver.get(baseUrl + "/news");
	    driver.findElement(By.linkText("login")).click();
	    driver.findElement(By.xpath("(//input[@name='acct'])[2]")).clear();
	    driver.findElement(By.xpath("(//input[@name='acct'])[2]")).sendKeys("Monkey");
	    driver.findElement(By.xpath("//input[@value='create account']")).click();
	  }
	  
	  	  
	  @After
	  public void tearDown() throws Exception {
	    //driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
	  
	  private void logIn(){
		    driver.findElement(By.linkText("login")).click();
		    driver.findElement(By.name("acct")).clear();
		    driver.findElement(By.name("acct")).sendKeys("1632_test");
		    driver.findElement(By.name("pw")).clear();
		    driver.findElement(By.name("pw")).sendKeys("laboon123");
		    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    driver.findElement(By.linkText("submit")).click();
	  }
	  private void logOut() {
		    driver.findElement(By.linkText("logout")).click();
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
