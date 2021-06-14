package webscrapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebpageReader {

	private static WebClient webClient;
	public static String downloadWebPage(String webpage, String uniqueID)
	{
		String res = null;

		/*if(webpage.contains("wordreference.com"))
			res = traditionalWebPageContents(webpage);
		else 
		{*/
			res = getPageContentsWebClient(webpage);
		//}


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
			throw new Error();
		}
	}

	private static String traditionalWebPageContents(String webpage) {
		webpage = webpage.replaceAll(" ", "%20");
		try {
			// Create URL object
			URL url = new URL(webpage);
			URLConnection conn = url.openConnection();
			String codeType = StandardCharsets.ISO_8859_1.displayName();
			InputStreamReader isr = 
					new InputStreamReader(url.openStream(),
							codeType
							);

			BufferedReader readr = 
					new BufferedReader(isr);

			String line = null;
			String res = "";
			while ((line = readr.readLine()) != null) 
			{
				//res+=TextInputUtils.Utf8ToIso(line)+"\n";
				res+=line+"\n";
			}


			readr.close();
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
}
