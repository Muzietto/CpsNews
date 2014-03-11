package net.faustinelli.j8_lambda.cps.mvc;

import net.faustinelli.j8_lambda.cps.service.NewsService;
import net.faustinelli.j8_lambda.cps.callback.AvailabilityNotifierImpl;
import net.faustinelli.j8_lambda.cps.callback.Feedback;
import net.faustinelli.j8_lambda.cps.callback.Notification;

public class FlowController implements CpsController {

	private CpsView view;
	private NewsService newsService;

	public FlowController(CpsView view, NewsService newsService) {
		this.view = view;
		this.newsService = newsService;
	}

	@Override
	public void finish() {
	    view.terminate(new Notification().withText("close News Reader?")
	            .withFeedback(new Feedback(
	                    view::empty,
	                    this::showNewsFeed
	        )));
	}

	@Override
	public void showNewsFeed(){		
        view.lock(new Notification().withText("preparing news..."));
        newsService.getNewsFeed(
            new AvailabilityNotifierImpl(view,this));		
	}

}
