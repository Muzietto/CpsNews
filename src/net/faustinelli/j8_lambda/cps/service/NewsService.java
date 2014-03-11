package net.faustinelli.j8_lambda.cps.service;

import net.faustinelli.j8_lambda.cps.callback.AvailabilityNotifier;
import net.faustinelli.j8_lambda.cps.util.Nonnull;

public interface NewsService {
	public void eventuallyCheckForUpdates();

	public void getNewsFeed(@Nonnull AvailabilityNotifier notifier);
}
