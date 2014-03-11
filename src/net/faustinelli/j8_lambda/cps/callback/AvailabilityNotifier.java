package net.faustinelli.j8_lambda.cps.callback;

import net.faustinelli.j8_lambda.cps.mvc.RssFeed;
import net.faustinelli.j8_lambda.cps.util.Nonnull;

public interface AvailabilityNotifier
{
  public void notifyFeedAvailable (@Nonnull RssFeed newsFeed);

  public void notifyCachedFeedAvailable (@Nonnull RssFeed newsFeed);
  
  public void notifyFeedUnavailable();

  public void notifyFeedCouldBeDownloaded (@Nonnull DownloadConfirmation downloadConfirmation);
}
