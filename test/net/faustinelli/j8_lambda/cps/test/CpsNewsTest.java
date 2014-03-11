package net.faustinelli.j8_lambda.cps.test;

import junit.framework.TestSuite;
import net.faustinelli.j8_lambda.cps.callback.DownloadConfirmation;
import net.faustinelli.j8_lambda.cps.callback.Feedback;

import org.junit.Test;

public class CpsNewsTest extends TestSuite {
	
	@Test
	public void testFeedback1() throws Exception {
		
		Runnable onRefuse = () -> System.out.println("refused");
		Feedback fb = new Feedback(onRefuse);
		
		fb.onRefuse();
	}

	@Test
	public void testFeedback2() throws Exception {
		
		Runnable onRefuse = () -> System.out.println("refused");
		DownloadConfirmation dc = () -> System.out.println("confirmed");
		
		Feedback fb = new Feedback(dc,onRefuse);
		
		fb.onConfirm();
	}
}
