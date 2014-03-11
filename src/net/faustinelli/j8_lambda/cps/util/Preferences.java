package net.faustinelli.j8_lambda.cps.util;

import java.util.HashMap;

public class Preferences {
	
	private HashMap<String, Boolean> prefs;

	public Boolean isNewsDownloadAllowed() {
		return prefs.get("isNewsDownloadAllowed");
	}

}
