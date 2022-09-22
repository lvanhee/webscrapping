module webscrapping {
	requires htmlunit;
	requires java.logging;
	requires java.datatransfer;
	requires java.desktop;
	requires cachingutils;
	requires java.net.http;
	
	exports webscrapping;
	exports webscrapping.robot;
}