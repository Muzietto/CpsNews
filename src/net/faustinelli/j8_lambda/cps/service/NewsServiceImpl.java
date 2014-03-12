package net.faustinelli.j8_lambda.cps.service;

import net.faustinelli.j8_lambda.cps.callback.AvailabilityNotifier;
import net.faustinelli.j8_lambda.cps.provider.CacheService;
import net.faustinelli.j8_lambda.cps.provider.HtmlClient;
import net.faustinelli.j8_lambda.cps.util.Nonnull;
import net.faustinelli.j8_lambda.cps.util.NotFoundException;
import net.faustinelli.j8_lambda.cps.util.Preferences;


public class NewsServiceImpl implements NewsService{

	private CacheService cache;
	private Preferences preferences;
	private HtmlClient htmlClient;
	
	public NewsServiceImpl(
			CacheService cache, 
			Preferences preferences, 
			HtmlClient htmlClient) {
		this.cache = cache;
		this.htmlClient = htmlClient;
		this.preferences = preferences;
	}

	@Override
	public void getNewsFeed (final @Nonnull AvailabilityNotifier notifier)
	  {
	    try // not worth a single Optional
	      {
	        notifier.notifyFeedAvailable(cache.getRssFeed());
	      }
	    catch (NotFoundException e)
	      {
	        ensureCacheIsInitialized(notifier);
	        
	        if (!cache.getStatus().isDownloadNeeded())
	          {
	            readNewsFeedAndNotifyAvailability(notifier);                    
	          }
	        else if (preferences.isNewsDownloadAllowed())
	          {
	            downloadNewsFeedAndNotifyAvailability(notifier);
	          }
	        else // DownloadConfirmation is a @FunctionalInterface
	          {
	            notifier.notifyFeedCouldBeDownloaded(() -> downloadNewsFeedAndNotifyAvailability(notifier));
	          }
	      }
	  }

	@Override
	public void eventuallyCheckForUpdates() {
		// TODO Auto-generated method stub		
	}

	private void downloadNewsFeedAndNotifyAvailability(AvailabilityNotifier notifier) {
        try {
        	notifier.notifyFeedAvailable(htmlClient.get());
        } catch (Exception exc) {
            notifier.notifyFeedUnavailable();
        }
	}

	private void readNewsFeedAndNotifyAvailability(
			AvailabilityNotifier notifier) {
		// TODO Auto-generated method stub
	}

	private void ensureCacheIsInitialized(
			AvailabilityNotifier notifier) {
		// TODO Auto-generated method stub
	}
}
