package net.faustinelli.j8_lambda.cps.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestSuite;
import net.faustinelli.j8_lambda.cps.callback.AvailabilityNotifier;
import net.faustinelli.j8_lambda.cps.callback.AvailabilityNotifierImpl;
import net.faustinelli.j8_lambda.cps.callback.DownloadConfirmation;
import net.faustinelli.j8_lambda.cps.callback.Feedback;
import net.faustinelli.j8_lambda.cps.callback.Notification;
import net.faustinelli.j8_lambda.cps.mvc.CpsView;
import net.faustinelli.j8_lambda.cps.mvc.RssFeed;
import net.faustinelli.j8_lambda.cps.provider.CacheService;
import net.faustinelli.j8_lambda.cps.provider.CacheStatus;
import net.faustinelli.j8_lambda.cps.provider.HtmlClient;
import net.faustinelli.j8_lambda.cps.service.NewsService;
import net.faustinelli.j8_lambda.cps.service.NewsServiceImpl;
import net.faustinelli.j8_lambda.cps.util.NotFoundException;
import net.faustinelli.j8_lambda.cps.util.Preferences;

import org.junit.Assert;
import org.junit.Test;

public class CpsNewsTest extends TestSuite {

	@Test
	public void testFeedback1() throws Exception {
		
		StringBuffer result = new StringBuffer();

		Runnable onRefuse = () -> result.append("refused");
		Feedback fb = new Feedback(onRefuse);

		fb.onRefuse();
		
		Assert.assertEquals("refused", result.toString());
	}

	@Test
	public void testFeedback2() throws Exception {

		StringBuffer result = new StringBuffer();

		Runnable onRefuse = () -> result.append("refused");
		DownloadConfirmation dc = () -> result.append("confirmed");

		Feedback fb = new Feedback(dc, onRefuse);

		fb.onConfirm();
		
		Assert.assertEquals("confirmed", result.toString());
	}

	/**
	 * We need: 
	 * - cache.getRssFeed() throws NotFoundException 
	 * - cache.getStatus().isDownloadNeeded() == false 
	 * - preferences.isNewsDownloadAllowed() == true
	 * - htmlClient.get() throws 
	 */
	@Test
	public void testFailedNewsFeedDownloadAndNotification() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream temp = System.out;
		System.setOut(new PrintStream(baos));
		
		HtmlClient throwingClient = new HtmlClient() {
			
			@Override
			public RssFeed get() {
				throw new RuntimeException();
			}
		};
		
		NewsService serviceWithNoFeedAvailable = testNewsService(true,throwingClient);
		
		AvailabilityNotifier an = new AvailabilityNotifierImpl(testView(),null);
		
		serviceWithNoFeedAvailable.getNewsFeed(an);
		
		String result = baos.toString();

		System.setOut(temp);
		
		Assert.assertTrue(result.contains("view unlocked"));
		Assert.assertTrue(result.contains("fresh news are unavailable"));
	}

	/**
	 * We need: 
	 * - cache.getRssFeed() throws NotFoundException 
	 * - cache.getStatus().isDownloadNeeded() == false 
	 * - preferences.isNewsDownloadAllowed() == true
	 * - htmlClient.get() succeeds
	 */
	@Test
	public void testSuccessfulNewsFeedDownloadAndNotification() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream temp = System.out;
		System.setOut(new PrintStream(baos));
		
		HtmlClient goodClient = new HtmlClient() {
			
			@Override
			public RssFeed get() {
				return new RssFeed();
			}
		};

		AvailabilityNotifier an = new AvailabilityNotifierImpl(testView(),null);
		
		NewsService serviceWithFeedAvailable = testNewsService(true,goodClient);

		serviceWithFeedAvailable.getNewsFeed(an);
		
		String result = baos.toString();

		System.setOut(temp);
		
		Assert.assertTrue(result.contains("view populated"));
		Assert.assertTrue(result.contains("view unlocked"));
	}

	/**
	 * We need: 
	 * - cache.getRssFeed() throws NotFoundException 
	 * - cache.getStatus().isDownloadNeeded() == false 
	 * - preferences.isNewsDownloadAllowed() == false
	 * - htmlClient.get() either throws or succeeds 
	 */
	@Test
	public void testNotifyFeedCouldBeDownloadedAndDoIt() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream temp = System.out;
		System.setOut(new PrintStream(baos));
		
		HtmlClient goodClient = new HtmlClient() {
			
			@Override
			public RssFeed get() {
				return new RssFeed();
			}
		};

		NewsService serviceAskingAndThenFailing = testNewsService(false,goodClient);
		
		AvailabilityNotifier an = new AvailabilityNotifierImpl(testView(),null);
		
		serviceAskingAndThenFailing.getNewsFeed(an);

		String result = baos.toString();

		System.setOut(temp);
		
		Assert.assertTrue(result.contains("view confirmToDownloadNews"));
	}

	/**
	 * We need: 
	 * - cache.getRssFeed() throws NotFoundException 
	 * - cache.getStatus().isDownloadNeeded() == false 
	 * - preferences.isNewsDownloadAllowed() == false
	 * - htmlClient.get() either throws or succeeds 
	 */
	@Test
	public void testNotifyFeedCouldBeDownloadedButFail() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream temp = System.out;
		System.setOut(new PrintStream(baos));
		
		HtmlClient throwingClient = new HtmlClient() {

			@Override
			public RssFeed get() {
				throw new RuntimeException();
			}
		};
		
		NewsService serviceAskingAndThenFailing = testNewsService(false,throwingClient);
		
		AvailabilityNotifier an = new AvailabilityNotifierImpl(testView(),null);
		
		serviceAskingAndThenFailing.getNewsFeed(an);

		String result = baos.toString();

		System.setOut(temp);
		
		Assert.assertTrue(result.contains("view confirmToDownloadNews"));
	}

	private NewsService testNewsService(Boolean isNewsDownloadAllowed, HtmlClient htmlClient) {
		CacheStatus yesDownload = new CacheStatus() {

			@Override
			public Boolean isDownloadNeeded() {
				return Boolean.TRUE;
			}
		};

		CacheService cache = new CacheService() {

			@Override
			public CacheStatus getStatus() {
				return yesDownload;
			}

			@Override
			public RssFeed getRssFeed() {
				throw new NotFoundException();
			}
		};

		Preferences prefs = new Preferences() {
			
			@Override
			public Boolean isNewsDownloadAllowed() {
				return isNewsDownloadAllowed;
			}
		};
		
		return new NewsServiceImpl(cache, prefs, htmlClient);
	}
	
	private CpsView testView() {
		return new CpsView() {
			
			@Override
			public void unlockAndThen(Runnable continuation) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void unlock() {
				System.out.println("view unlocked");
			}
			
			@Override
			public void terminate(Notification notification) {
				System.out.println("view terminated");
			}
			
			@Override
			public void populate(RssFeed rssFeed) {
				System.out.println("view populated by " + rssFeed.toString());
			}
			
			@Override
			public void notifyFeedUnavailable(Notification notification) {
				System.out.println("view notifies feed unavailable; " + notification.toString());
			}
			
			@Override
			public void notifyFeedIsCached(Notification notification) {
				System.out.println("view notifies feed is cached; " + notification.toString());
			}
			
			@Override
			public void lock(Notification notification) {
				System.out.println("view locked; " + notification.toString());

			}
			
			@Override
			public void empty() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void confirmToDownloadNews(Notification notification) {
				System.out.println("view confirmToDownloadNews; " + notification.toString());
			}
		};
	}
	
	
}
