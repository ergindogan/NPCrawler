import java.io.File;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class OutputController {
	
	public static void configureLogger(File path, boolean console) {
		if (path == null) {
			throw new RuntimeException("Log path is not defined!");
		}

		if (!path.getParentFile().exists()) {
			throw new RuntimeException("Log path is configured for an invalid path! Path :" + path.getAbsolutePath());
		}

		Level lLevel = Level.ALL;

		LogManager.resetConfiguration();

//		%d %-5p %t [%c{1}] %m%n
		PatternLayout layout = new PatternLayout("%m%n");
		// Add File Appender to root logger
		DailyRollingFileAppender fa = new DailyRollingFileAppender();
		fa.setName("file");
		fa.setName("FileLogger");
		fa.setFile(path.getAbsolutePath());
		fa.setLayout(layout);
		fa.setAppend(true);
		fa.setDatePattern("'.'yyyy-MM-dd");
		fa.setEncoding("UTF-8");
		fa.activateOptions();
		

		Logger mainLogger = Logger.getLogger("Main");
		mainLogger.setAdditivity(false);
		mainLogger.setLevel(lLevel);
		mainLogger.addAppender(fa);

		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setAdditivity(false);
		rootLogger.setLevel(lLevel);
		rootLogger.addAppender(fa);

		// Add Console appender to root logger
//		if (console) {
//			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
//			consoleAppender.setName("console");
//			consoleAppender.setThreshold(Level.OFF);
//			consoleAppender.activateOptions();
//			
//			rootLogger.addAppender(consoleAppender);
//			mainLogger.addAppender(consoleAppender);
//		}
	}

}
