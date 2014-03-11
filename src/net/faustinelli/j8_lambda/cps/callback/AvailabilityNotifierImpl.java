package net.faustinelli.j8_lambda.cps.callback;

import net.faustinelli.j8_lambda.cps.mvc.CpsController;
import net.faustinelli.j8_lambda.cps.mvc.CpsView;
import net.faustinelli.j8_lambda.cps.mvc.RssFeed;

public class AvailabilityNotifierImpl implements AvailabilityNotifier {

	private CpsView view;
	private CpsController flowController;

	public AvailabilityNotifierImpl(CpsView view, CpsController controller) {
		this.view = view;
		this.flowController = controller;
	}

	@Override
	public void notifyFeedAvailable(RssFeed newsFeed) {
        view.populate(newsFeed);
        view.unlock();
	}

	@Override
	public void notifyCachedFeedAvailable(RssFeed newsFeed) {
        view.unlock();
        view.populate(newsFeed);
        view.notifyFeedIsCached(new Notification().withText("these news are coming from cache"));
	}

	@Override
	public void notifyFeedUnavailable() {
        view.unlock();
        view.notifyFeedUnavailable(new Notification()
                .withText("fresh news are unavailable. Click OK to keep reading these ones")
                .withCaption("WARNING")
                .withFeedback(new Feedback(flowController::finish)
                ));
	}

	@Override
	public void notifyFeedCouldBeDownloaded( // guanto di sfida
			DownloadConfirmation confirmation) {

		view.confirmToDownloadNews(new Notification()
				.withText("please confirm you want fresh news to be downloaded")
				.withCaption("GIMME SOME FEEDBACK")
				.withFeedback(new Feedback(
						confirmation::confirmDownload,
						() -> { view.unlock(); flowController.finish(); })
				));
	}

}
