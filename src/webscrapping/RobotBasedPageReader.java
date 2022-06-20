package webscrapping;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import webscrapping.robot.RobotUtils;

public class RobotBasedPageReader {
	public static Robot r;
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
	
	
	public static String getFullPageAsText()
	{
		clickOnChromeInsidePage();
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		r.keyPress(KeyEvent.VK_CONTROL);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			String res =(String) Toolkit.getDefaultToolkit()
					.getSystemClipboard().getData(DataFlavor.stringFlavor); 
			return res;
		} catch(ExceptionInInitializerError | IllegalStateException e)
		{
			return getFullPageAsText();
		}
		catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		
		throw new Error();
	}

	public static synchronized String getFullPageAsHtml(String page, double processingSpeedFactor) {
		try {
			
			clickOnChrome();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_T);
			
			Thread.sleep((int)(300*processingSpeedFactor));
			
			r.keyRelease(KeyEvent.VK_T);
			r.keyRelease(KeyEvent.VK_CONTROL);
			
			writeAddressAndLoadPage("view-source:"+page);
			Thread.sleep((int)(1000*processingSpeedFactor));

			String res = getFullPageAsText();
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
			Thread.sleep(750);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RobotUtils.pasteText(name);
		
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
		
	}
	
	public static synchronized void tryDownloadingPageByHand(URL link, File outputFile) {
		if(outputFile.exists())
			outputFile.delete();
	
		clickOnChrome();
		writeAddressAndLoadPage(link.toString());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		savePage(outputFile.getParentFile().getAbsolutePath(),outputFile.getName());
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
		
		
		RobotUtils.pasteText(pageToAsk);
		
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		//System.out.println("Loading the page");
		try {
			Thread.sleep(1000+(int)(Math.random()*500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
