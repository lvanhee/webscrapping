module webscrapping {
	requires htmlunit;
	requires java.logging;
	requires java.datatransfer;
	requires java.desktop;
	requires cachingutils;
	
	exports webscrapping;
	exports webscrapping.robot;
}