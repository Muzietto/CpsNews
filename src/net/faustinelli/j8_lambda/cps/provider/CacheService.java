package net.faustinelli.j8_lambda.cps.provider;

import net.faustinelli.j8_lambda.cps.mvc.RssFeed;

public interface CacheService {

	RssFeed getRssFeed();

	CacheStatus getStatus();

}
