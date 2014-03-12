package net.faustinelli.j8_lambda.cps.callback;

/**
 * Classe che piu' classe non si puo'
 * 
 * @see http://netbeans.dzone.com/articles/cleaner-mvc-by-continuation-passing
 * 
 * @author Marco Faustinelli <contatti@faustinelli.net>
 * 
 */
public class Notification {

	private String text;
	private String caption;
	private Feedback feedback;

	public Notification withText(String text) {
		this.text = text;
		return this;
	}

	public Notification withCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public Notification withFeedback(Feedback feedback) {
		this.feedback = feedback;
		return this;
	}

	@Override
	public String toString() {

		StringBuffer result = new StringBuffer("notification: ");
		result.append("caption=" + caption);
		result.append("; text=" + text);
		result.append("; feedback=" + feedback.toString());

		return result.toString();
	}

}
