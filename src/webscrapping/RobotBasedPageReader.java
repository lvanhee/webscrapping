package webscrapping;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class RobotBasedPageReader {
	private static Robot r;
	static {
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	public static void clickOnChrome() {
		clickOn(50, 50);
	}
	public static void clickOnChromeInsidePage() {
		clickOn(50, 150);
	}
	
	
	public static void clickOn(int x, int y) {
		r.mouseMove(x,y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}	
	
	
	public static String getFullPageAsText(Robot r)
	{
		clickOnChromeInsidePage();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_A);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		r.keyPress(KeyEvent.VK_C);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_C);
		r.keyRelease(KeyEvent.VK_A);
		
		
		

		try {
			return (String) Toolkit.getDefaultToolkit()
					.getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch(ExceptionInInitializerError | IllegalStateException e)
		{
			return getFullPageAsText(r);
		}
		catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		
		throw new Error();
	}

	public static void writeAddressAndLoadPage(String pageToAsk) {
		
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_L);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_L);
		
		
		pasteText(pageToAsk);
		
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		//System.out.println("Loading the page");
		try {
			Thread.sleep(1000+(int)(Math.random()*500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void pasteText(String pageToAsk) {
		String text = pageToAsk;
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);

		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_V);
		r.keyRelease(KeyEvent.VK_V);		
		r.keyRelease(KeyEvent.VK_CONTROL);
	}

	public static synchronized String getFullPageAsHtml(String page, double processingSpeedFactor) {
		try {
			
			clickOnChrome();
			writeAddressAndLoadPage(page);
			Thread.sleep((int)(1000*processingSpeedFactor));
			
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_U);

			Thread.sleep((int)(300*processingSpeedFactor));

			r.keyRelease(KeyEvent.VK_U);
			r.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep((int)(1000*processingSpeedFactor));

			String res = getFullPageAsText(r);
			Thread.sleep((int)(300*processingSpeedFactor));

			closePage();

			
			return res;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		throw new Error();	
	}

	public static void closePage() {
		
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_W);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		r.keyRelease(KeyEvent.VK_W);
		r.keyRelease(KeyEvent.VK_CONTROL);
		
		try {
			//System.out.println("Closing the page");
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized static void savePage(String parent, String name) {
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_S);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		r.keyRelease(KeyEvent.VK_S);
		r.keyRelease(KeyEvent.VK_CONTROL);
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pasteText(name);
		
		/*r.mouseMove(500,170);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		r.keyPress(KeyEvent.VK_ENTER);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyRelease(KeyEvent.VK_ENTER);
		
		r.keyPress(KeyEvent.VK_ENTER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyRelease(KeyEvent.VK_ENTER);
		
		r.keyPress(KeyEvent.VK_ENTER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyRelease(KeyEvent.VK_ENTER);
		
		
		pasteText(parent);*/
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyPress(KeyEvent.VK_ENTER);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyRelease(KeyEvent.VK_ENTER);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyPress(KeyEvent.VK_ESCAPE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.keyRelease(KeyEvent.VK_ESCAPE);
		
		
		
		System.out.println("Pasted");
	}
	
	public static void clearAndTypeString(Robot r, String string) throws InterruptedException {
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_A);
		
		Thread.sleep(100);
		r.keyRelease(KeyEvent.VK_A);
		r.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(100);
		r.keyPress(KeyEvent.VK_DELETE);
		r.keyRelease(KeyEvent.VK_DELETE);
		Thread.sleep(100);
		
		typeString(r,string);
		
	}
	public static void typeString(Robot r2, String string) {
		while(!string.isEmpty())
		{
			char c = string.charAt(0);
			int code = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
			
			boolean isShiftActive = Character.isUpperCase(c)||c=='('||c==')'|| c==':';
			if(isShiftActive)r.keyPress(KeyEvent.VK_SHIFT);
			
			if(c=='(') code = KeyEvent.VK_9;
			
			if(c==')')
				code = KeyEvent.VK_0;
			
			if(c=='"') code = KeyEvent.VK_SEMICOLON;
			
			r.keyPress(code);
			r.keyRelease(code);
			
			if(isShiftActive) r.keyRelease(KeyEvent.VK_SHIFT);
			string = string.substring(1);
	
		}
	}


}
