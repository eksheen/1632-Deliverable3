package Deliverable3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HackerNewsTester {

	private boolean acceptNextAlert = true;
	private String baseUrl = "https://news.ycombinator.com";
	private StringBuffer verificationErrors = new StringBuffer();
	static WebDriver driver = new FirefoxDriver();

	// Start at the home page for hackernews for each test
	@Before
	public void setUp() throws Exception {

		driver.get(baseUrl + "/");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	// Verify we are on the right site
	@Test
	public void testVerifyCorrectSite() {

		// Simply check that the title contains the word "reddit"
		String title = driver.getTitle();
		assertTrue(title.contains("Hacker News"));

	}

	/****************************************
	 * USER STORY 1*****************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************/

	@Test
	public void testLoginNoUsernameNoPassword() throws Exception {
		logIn();
		logOut();
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains("Bad login.")); // this does not work yet
	}

	@Test
	public void testLoginBadCredentials() throws Exception {
		driver.get(baseUrl + "/login");
		driver.findElement(By.name("acct")).clear();
		driver.findElement(By.name("acct")).sendKeys("Monkey");
		driver.findElement(By.name("pw")).clear();
		driver.findElement(By.name("pw")).sendKeys("MOnkey");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains("Bad login.")); // this does not work yet
	}

	@Test
	public void testCreateAccBlankUN() throws Exception {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.get(baseUrl + "/login?goto=news");
		driver.findElement(By.xpath("(//input[@name='pw'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='pw'])[2]")).sendKeys("Monkey");
		driver.findElement(By.xpath("//input[@value='create account']")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains(
				"Usernames can only contain letters, digits, dashes and underscores, and should be between 2 and 15 characters long. Please choose another."));
	}

	@Test
	public void testCreateAccBlankPass() throws Exception {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.get(baseUrl + "/login?goto=news");
		driver.findElement(By.xpath("(//input[@name='acct'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='acct'])[2]")).sendKeys("kfgnjdfngjdfg");
		driver.findElement(By.xpath("//input[@value='create account']")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains("Passwords should be at least 8 characters. Please choose another."));
	}

	@Test
	public void testCreateAccConflictingUser() throws Exception {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.get(baseUrl + "/news");
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.xpath("(//input[@name='acct'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='acct'])[2]")).sendKeys("Monkey");
		driver.findElement(By.xpath("//input[@value='create account']")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains(
				"That username conflicts with an existing one. Names are case-insensitive. Please choose another."));
	}


	/****************************************
	 * USER STORY 2*****************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************/
	@Test
	public void testNumberOfUpvotes() throws Exception {
		logIn();
		driver.get(baseUrl + "/");
		//Verify all posts have a score field
		List<WebElement> scores= driver.findElements(By.className("score")); //this is the score of a post
		List<WebElement> ranks= driver.findElements(By.className("rank")); //this is the current rank on the homepage
		assertTrue(ranks.size() == scores.size()); //verify the number of ranks is equal to the number of scores
	}

	@Test
	public void testUpvoteASubmissionWhileLoggedIn() throws Exception {
		logIn();
		List<WebElement> upvotes_old = driver.findElements(By.className("votearrow"));
		driver.findElement(By.cssSelector("div.votearrow")).click();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		driver.get(baseUrl + "/");
		List<WebElement> upvotes_new = driver.findElements(By.className("votearrow"));
		System.out.println(upvotes_old.size());
		System.out.println(upvotes_new.size());
		logOut();
		assertTrue(upvotes_old.size() == upvotes_new.size()+1);
	}

	@Test
	public void testUpvoteSubmissionWhileNotLoggedIn() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.cssSelector("div.votearrow")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains("You have to be logged in to vote."));
	}

	@Test
	public void testUpvoteACommentWhileLoggedIn() throws Exception {
		logIn();
		driver.findElement(By.linkText("comments")).click();
		driver.findElement(By.linkText("parent")).click();
		List<WebElement> upvotes_old= driver.findElements(By.className("votearrow"));
		driver.findElement(By.cssSelector("div.votearrow")).click();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		driver.navigate().refresh();
		List<WebElement> upvotes_new = driver.findElements(By.className("votearrow"));
		System.out.println(upvotes_old.size());
		System.out.println(upvotes_new.size());
		logOut();
		assertTrue(upvotes_old.size() == upvotes_new.size()+1);
	}

	@Test
	public void testUpvoteACommentWhileNotLoggedIn() throws Exception {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.findElement(By.linkText("comments")).click();
		driver.findElement(By.linkText("parent")).click();
		driver.findElement(By.cssSelector("div.votearrow")).click();
		String body = driver.findElement(By.tagName("body")).getText();
		assertTrue(body.contains("You have to be logged in to vote."));
	}

	/****************************************
	 * USER STORY 3*****************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************/

	@Test
	public void testAddCommentNotLoggedIn() throws Exception {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.findElement(By.linkText("comments")).click();
		driver.findElement(By.linkText("parent")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String error = driver.findElement(By.tagName("body")).getText();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		assertTrue(error.contains("You have to be logged in to comment."));
	}

	@Test
	public void testSubmitNotLoggedIn() throws Exception {
		driver.findElement(By.linkText("submit")).click();
		String error = driver.findElement(By.tagName("body")).getText();
		assertTrue(error.contains("You have to be logged in to submit."));
	}

	@Test
	public void testSubmitLoggedIn() throws Exception {
		logIn();
		WebElement submitlink = driver.findElement(By.linkText("submit"));
		submitlink.click();
		boolean exists = !driver.findElements(By.name("text")).isEmpty()
				&& !driver.findElements(By.name("url")).isEmpty() && !driver.findElements(By.name("title")).isEmpty();
		logOut();
		assertTrue(exists);
	}

	@Test
	public void testAddCommentLoggedIn() throws Exception {
		logIn();
		driver.findElement(By.linkText("comments")).click();
		driver.findElement(By.linkText("parent")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String error = driver.findElement(By.cssSelector("td[style=\"line-height:12pt; height:10px;\"]")).getText();
		logOut();
		assertTrue(error.contains("Add Comment")); // Verify you reached the add
													// comment page
	}

	@Test
	public void testSubmitThenLogIn() throws Exception {
		// Get in the logged out state by logging in, then logging out
		logIn();
		logOut();
		driver.findElement(By.linkText("submit")).click();
		String error = driver.findElement(By.tagName("body")).getText();
		assertTrue(error.contains("You have to be logged in to submit."));
		driver.findElement(By.name("acct")).clear();
		driver.findElement(By.name("acct")).sendKeys("1632_test");
		driver.findElement(By.name("pw")).clear();
		driver.findElement(By.name("pw")).sendKeys("laboon123");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		boolean exists = !driver.findElements(By.name("text")).isEmpty()
				&& !driver.findElements(By.name("url")).isEmpty() && !driver.findElements(By.name("title")).isEmpty();
		logOut();
		assertTrue(exists);

	}
	@Test
	public void testCommentThenLogIn() throws Exception {
		// Get in the logged out state by logging in, then logging out
		logIn();
		logOut();
		driver.findElement(By.linkText("comments")).click();
		driver.findElement(By.linkText("parent")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String error = driver.findElement(By.tagName("body")).getText();
		assertTrue(error.contains("You have to be logged in to comment."));
		driver.findElement(By.name("acct")).clear();
		driver.findElement(By.name("acct")).sendKeys("1632_test");
		driver.findElement(By.name("pw")).clear();
		driver.findElement(By.name("pw")).sendKeys("laboon123");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		String pageTitle = driver.findElement(By.cssSelector("td[style=\"line-height:12pt; height:10px;\"]")).getText();
		logOut();
		assertTrue(pageTitle.contains("Add Comment")); // Verify you reached the add
													// comment page
	}
	/****************************************
	 * USER STORY 4*****************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************/
	// The Canadian test
	@Test
	public void testFilterByNew() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("new")).click();
		List<WebElement> ages = driver.findElements(By.className("age"));
		int oldtime;
		int time2;
		// convert time to seconds
		// Im sorry :(
		if (ages.get(0).getText().contains("minutes"))
			oldtime = Integer.parseInt(ages.get(0).getText().substring(0, 1)) * 60; // I'm
																					// Sorry
		else if (ages.get(0).getText().contains("hours"))
			oldtime = Integer.parseInt(ages.get(0).getText().substring(0, 1)) * 3600; // I'm
																					// Sorry
		else
			oldtime = Integer.parseInt(ages.get(0).getText().substring(0, 1)); // I'm Sorry
		
		for (int i=1;i<ages.size();i++)
		{
		// I'm Sorry
		if (ages.get(i).getText().contains("minutes"))
			time2 = Integer.parseInt(ages.get(i).getText().substring(0, 1)) * 60; // I'm
																					// Sorry
		else if (ages.get(i).getText().contains("hours"))
			time2 = Integer.parseInt(ages.get(i).getText().substring(0, 1)) * 3600; // I'm
																					// Sorry
		else
			time2 = Integer.parseInt(ages.get(i).getText().substring(0, 1));// I'm
																			// Sorry
		// I'm so sorry for violating the law of demeter
			System.out.println(oldtime);
			System.out.println(time2);
			assertTrue(oldtime <= time2);
			oldtime=time2;
		}
	}

	@Test
	public void testFilterByShow() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("show")).click();
		List<WebElement> posts = driver.findElements(By.cssSelector("td[class=title]"));
		List<WebElement> num_posts = driver.findElements(By.className("rank"));
		int showPosts = 0;
		for (int i = 0; i < posts.size(); i++) {
			if (posts.get(i).getText().contains("Show"))
				showPosts++;

		}
		String title = driver.getTitle();
		assertTrue(title.contains("Show"));
		assertTrue(showPosts == num_posts.size());

	}

	@Test
	public void testSearch() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.name("q")).clear();
		driver.findElement(By.name("q")).sendKeys("Google" + Keys.RETURN);
		assertTrue(driver.findElements(By.tagName("ng-pluralize")).size() != 0);
	}

	@Test
	public void testFilterByAsk() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.linkText("ask")).click();
		List<WebElement> posts = driver.findElements(By.cssSelector("td[class=title]"));
		List<WebElement> num_posts = driver.findElements(By.className("rank"));
		int askPosts = 0;
		for (int i = 0; i < posts.size(); i++) {
			if (posts.get(i).getText().contains("Ask") || posts.get(i).getText().contains("Tell")
					|| posts.get(i).getText().contains("HN Office Hours") || posts.get(i).getText().contains("?"))
				askPosts++;

		}
		String title = driver.getTitle();
		assertTrue(title.contains("Ask"));
		System.out.println(askPosts);
		assertTrue(askPosts >= num_posts.size() - 2); // have a margin of error
														// of two posts

	}

	@Test
	public void testDefaultPage() throws Exception {
		driver.get(baseUrl);
		List<WebElement> posts = driver.findElements(By.className("score"));
		List<WebElement> num_posts = driver.findElements(By.className("rank"));
		assertTrue(posts.size() == num_posts.size()); 
		}

	@After
	public void tearDown() throws Exception {
		// driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	/****************************************
	 * Helper Functions***************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************/
	private void logIn() {
		try {
			logOut();
		} catch (NoSuchElementException e) {
			System.out.println("Already logged out \n");
		}
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("login")).click();
		driver.findElement(By.name("acct")).clear();
		driver.findElement(By.name("acct")).sendKeys("1632_test");
		driver.findElement(By.name("pw")).clear();
		driver.findElement(By.name("pw")).sendKeys("laboon123");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.get(baseUrl + "/");
	}

	private void logOut() {
		driver.get(baseUrl + "/");
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
