package net.faustinelli.j8_lambda.cps.mvc;

import net.faustinelli.j8_lambda.cps.callback.Notification;

public interface CpsView {
	
	public void lock(Notification notification);

	public void unlock();

	public void empty();
	
	public void populate(RssFeed rssFeed);

	public void notifyFeedIsCached(Notification notification);

	public void notifyFeedUnavailable(Notification notification);
	
	/**
	 * Sample Usage: () -> view.unlockAndThen(flowController::finish)
	 */
	public void unlockAndThen(Runnable continuation);
	
	public void confirmToDownloadNews(Notification notification);

	public void terminate(Notification notification);

}
