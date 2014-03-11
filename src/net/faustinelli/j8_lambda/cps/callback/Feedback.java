package net.faustinelli.j8_lambda.cps.callback;


/**
 * Altra classe che piu' classe non si puo'
 * 
 * @see http://netbeans.dzone.com/articles/cleaner-mvc-by-continuation-passing
 * 
 * @author Marco Faustinelli <contatti@faustinelli.net>
 * 
 * TODO - implement it as kind of FutureCallback
 */
public class Feedback {

	private DownloadConfirmation onConfirm;
	private Runnable onRefuse;

	public Feedback(DownloadConfirmation onConfirm, Runnable onRefuse) {
		this.onConfirm = onConfirm;
		this.onRefuse = onRefuse;
	}

	public Feedback(Runnable onRefuse) {
		this(() -> {}, onRefuse);
	}
	
	public void onConfirm() {
		this.onConfirm.confirmDownload();
	}
	
	public void onRefuse() {
		this.onRefuse.run();
	}
}
