package webscrapping.robot;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import webscrapping.RobotBasedPageReader;

public class RobotUtils {

	public static void pasteText(String pageToAsk) {
		String text = pageToAsk;
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
	
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_CONTROL);
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_V);
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_V);		
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_CONTROL);
	}

	public static void setBold() {
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_CONTROL);
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_B);
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_B);		
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_CONTROL);
		
	}

	private static void delete(int in) {
		for(int i = 0; i < in ; i++)
		{
			RobotBasedPageReader.r.keyPress(KeyEvent.VK_BACK_SPACE);
			RobotBasedPageReader.r.keyRelease(KeyEvent.VK_BACK_SPACE);
		}
	}

	public static void setItalics() {
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_CONTROL);
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_I);
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_I);		
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_CONTROL);
		
		
	}

	public static void setShiftOn() {
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_SHIFT);
	}

	public static void pressLeft(int nb) {
		for(int i = 0; i < nb; i++)
		{
			RobotBasedPageReader.r.keyPress(KeyEvent.VK_LEFT);
			RobotBasedPageReader.r.keyRelease(KeyEvent.VK_LEFT);
		}
	}

	public static void setShiftOff() {
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_SHIFT);
	}

	public static void control(String string) {
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_CONTROL);
		typeString(string);
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_CONTROL);
		
	}

	public static void typeString(String string) {
		
		while(!string.isEmpty())
		{
			char c = string.charAt(0);
			if(c=='“'||c=='”')c='\"';
			if(c=='’')c='\'';
			int code = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
			
			boolean isShiftActive = Character.isUpperCase(c)||c=='('||c==')'|| c==':'||c=='!'||c=='\"';
			if(isShiftActive)RobotBasedPageReader.r.keyPress(KeyEvent.VK_SHIFT);
			
			if(c=='(') code = KeyEvent.VK_9;
			if(c=='!') code = KeyEvent.VK_1;
			
			if(c==')')code = KeyEvent.VK_0;
			if(c=='*')code = KeyEvent.VK_MULTIPLY;
			
			if(c==':') code = KeyEvent.VK_SEMICOLON;
			if(c=='"') code = KeyEvent.VK_QUOTE;
			
			if(c=='é') {
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_ALT);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD1);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD1);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD3);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD3);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD0);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD0);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_ALT);
			}
			else if(c=='è') {
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_ALT);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD0);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD0);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD2);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD2);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD3);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD3);
				RobotBasedPageReader.r.keyPress(KeyEvent.VK_NUMPAD2);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_NUMPAD2);
				RobotBasedPageReader.r.keyRelease(KeyEvent.VK_ALT);
			}
			else {
			RobotBasedPageReader.r.keyPress(code);
			RobotBasedPageReader.r.keyRelease(code);
			}
			
			if(isShiftActive) RobotBasedPageReader.r.keyRelease(KeyEvent.VK_SHIFT);
			string = string.substring(1);
	
		}
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
		
		typeString(string);
		
	}

	public static void enter() {
		RobotBasedPageReader.r.keyPress(KeyEvent.VK_ENTER);
		RobotBasedPageReader.r.keyRelease(KeyEvent.VK_ENTER);
	}

}
