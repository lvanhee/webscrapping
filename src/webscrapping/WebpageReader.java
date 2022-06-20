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
import java.util.function.Function;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import cachingutils.impl.SplittedFileBasedCache;

public class WebpageReader {

	private static final Object lockOverloadRequests = "lockOverload";
	private static WebClient webClient;

	private static Map<String, String> cache = new HashMap<>();
	
	private static SplittedFileBasedCache<String, String> harddriveCache = SplittedFileBasedCache.newInstance(
			x->urlToLocalFile(x), Function.identity(), Function.identity());
	
	public static class PageContentsResult{
		public final String res;
		public boolean fromCache;
		private boolean isFailedToBeLoaded;
		private PageContentsResult(String res, boolean fromCache) {
			this.isFailedToBeLoaded = res.equals("NO_PAGE_TO_BE_SERVED");
			this.res = res; this.fromCache = fromCache;}
		public String getString() {
			return res;
		}
		public boolean isFailed() {
			return isFailedToBeLoaded;
		}
	}

	public static PageContentsResult getWebclientWebPageContents(String webpage)
	{
		if(cache.containsKey(webpage))//return cache.get(webpage);
				return new PageContentsResult(cache.get(webpage), true);
		if(harddriveCache.has(webpage)) {
			cache.put(webpage, harddriveCache.get(webpage));
			return //harddriveCache.get(webpage);
					new PageContentsResult(harddriveCache.get(webpage), true);
		}

		synchronized(lockOverloadRequests) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("WebpageReader: loading from URL "+webpage);
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
		harddriveCache.add(webpage, res);
		return //res;
				new PageContentsResult(res,false);
	}

	private static File urlToLocalFile(String webpage) {
		File res =
		new File("../databases/discordforrad/caches/webscrapping/"+webpage.replaceAll(":", "").replaceAll("\\?", "").replaceAll("=", "")+".html");
		
		return res;
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
			e.printStackTrace();
			if(e.toString().contains("404"))return "NO_PAGE_TO_BE_SERVED";
			if(e.toString().contains("com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: 502 Bad Gateway for")||
					e.toString().contains("com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: 522")||
					e.toString().contains("com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException: 503")
					)
				return getPageContentsWebClient(webpage);
				
			if(
					e.toString().contains("Attempted to refresh a page using an ImmediateRefreshHandler")||
					e.toString().contains("504")||
					e.getCause().toString().equals("java.net.SocketException: Connection reset")||
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

	public static void downloadFileFrom(String uRL, File outputFile) {
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

	public static PageContentsResult getWebclientWebPageContents(String last, boolean purgeEntry) {
		if(purgeEntry)
			{harddriveCache.delete(last);
			cache.remove(last);
			}
		return getWebclientWebPageContents(last);
	}
}
