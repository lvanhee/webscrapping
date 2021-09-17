package webscrapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebpageReader {

	private static final Object lockOverloadRequests = "lockOverload";
	private static WebClient webClient;
	
	private static Map<String, String> cache = new HashMap<>();
	
	public static String getWebclientWebPageContents(String webpage)
	{
		if(cache.containsKey(webpage))return cache.get(webpage);
		/*File cacheFileName = getCacheFileFor(webpage);
		if(cacheFileName.exists())
			try {
				return Files.readString(
						getCacheFileFor(webpage).toPath(),
						StandardCharsets.ISO_8859_1);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Error();
			}
		*/
		synchronized(lockOverloadRequests) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Loading:"+webpage);
		String res = null;
		res = getPageContentsWebClient(webpage);
		
		/*BufferedWriter writer;
		try {
		//	System.out.println(cacheFileName.getAbsolutePath());
			cacheFileName.createNewFile();
			writer = new BufferedWriter(new FileWriter(cacheFileName,StandardCharsets.ISO_8859_1));

			writer.write(res);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Trying to save at:"+cacheFileName);
			throw new Error();
		}*/

		if(cache.size()>500)cache.clear();
		cache.put(webpage, res);
		return res;
	}

	private static File getCacheFileFor(String webpage) {
		return new File("data/cache/webscrapping/"+webpage.replaceAll(":", "").replaceAll("\\?", "").replaceAll("=", "")+".html");
	}

	private synchronized static String getPageContentsWebClient(String webpage) {
		try {
			if(webClient==null)
			{
				webClient = new WebClient();

				webClient.getOptions().setJavaScriptEnabled(false);
				//webClient.getJavaScriptEngine().ini();
				java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
			}

			final HtmlPage page = webClient.getPage(webpage);
			final String pageAsXml = page.asXml();
			//  final String pageAsText = page.asText();
			return pageAsXml;
		}
		catch(Exception e)
		{
			if(e.getCause().toString().equals("java.net.SocketException: Connection reset")||
					e.getCause().toString().equals("java.net.SocketTimeoutException: Read timed out")
					||e.getCause().toString().endsWith("Connection timed out: no further information")
					||e.getCause().toString().equals("java.net.NoRouteToHostException: No route to host: no further information")
					||e.getCause().toString().endsWith("failed: Connection refused: no further information")
					||e.getCause().toString().equals("java.io.EOFException: SSL peer shut down incorrectly"))
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return getPageContentsWebClient(webpage);
			}
			e.printStackTrace(); 
			throw new Error();
		}
	}

	public static String traditionalWebPageContents(String webpage) {
		webpage = webpage.replaceAll(" ", "%20");
		try {
			// Create URL object
			URL url = new URL(webpage);
			//URLConnection conn = url.openConnection();
			InputStreamReader isr = 
					new InputStreamReader(url.openStream(),
							Charset.forName("ISO-8859-15")
							//StandardCharsets.ISO_8859_1
							);

			BufferedReader readr = 
					new BufferedReader(isr);

			String line = null;
			String res = "";
			while ((line = readr.readLine()) != null) 
			{
			//	res+=String.Utf8ToIso(line)+"\n";
				res+=line+"\n";
			}


			readr.close();
		//	System.out.println(res);
			return res;
		}

		// Exceptions
		catch (MalformedURLException mue) {
			// System.out.println("Malformed URL Exception raised");
		}
		catch (IOException ie) {
			//  System.out.println("IOException raised");
		}
		if(webpage.contains("bab.la"))return null;
		throw new Error();
	}

	public static void downloadFileFrom(String uRL, String string) {
		File outputFile = new File(string);
		URL u;
		try {
			u = URI.create(uRL).toURL();
			ReadableByteChannel rbc = Channels.newChannel(u.openStream());

			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
