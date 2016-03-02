package Deliverable3;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class HackerNewsTests {
	static WebDriver driver =  new FirefoxDriver();
	
	@Before
	public void setUp() throws Exception {
		driver.get("https://news.ycombinator.com/");
	}
	
   	@Test
   	public void testShowsCorrectTitle() {
   		// Simply check that the title contains the word "Hacker News"
   		String title = driver.getTitle();
   		assertTrue(title.contains("Hacker News"));
   	}-
   	
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
}