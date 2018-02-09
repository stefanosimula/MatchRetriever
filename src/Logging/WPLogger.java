package Logging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class WPLogger {
		private Logger log;
		private static WPLogger instance = null;
			
		protected WPLogger() {
			PropertyConfigurator.configure("log4j.properties");
		    log = Logger.getLogger(WPLogger.class);
		}
	   
		public static WPLogger getInstance() {
	      if(instance == null) {
	         instance = new WPLogger();
	      }
	      return instance;
		}
		
		public void Log(String strClass, String strMethod, String str, LogLevel logLevel) {
			switch(logLevel) {
			case DEBUG:
				log.debug("["+strClass+"]"+"["+strMethod+"] " + str);
				break;
			case INFO:
				log.info("["+strClass+"]"+"["+strMethod+"] " + str);
				break;
			case WARN:
				log.warn("["+strClass+"]"+"["+strMethod+"] " + str);
				break;
			case ERROR:
				log.error("["+strClass+"]"+"["+strMethod+"] " + str);
				break;
			case FATAL:
				log.fatal("["+strClass+"]"+"["+strMethod+"] " + str);
				break;
			}
		}
		
		public String GetMethodName() {
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		    StackTraceElement e = stacktrace[2];//coz 0th will be getStackTrace so 1st
		    return e.getMethodName();
		}
}
