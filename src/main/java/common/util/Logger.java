package common.util;

import dproxy.log.BasicLoging;
import dproxy.log.LogsDispatcher;

public class Logger {

	public static void info(String msg) {
		System.out.println(msg);
		logger.logMessage(msg, BasicLoging.LOG_LEVEL_INFO);
	}

	public static void error(String msg, Exception e) {
		logger.logMessage(msg, e, BasicLoging.LOG_LEVEL_ERROR);
		System.out.println(msg);
		e.printStackTrace();
	}

	public static void error(String msg) {
		logger.logMessage(msg, BasicLoging.LOG_LEVEL_ERROR);
		System.out.println(msg);
	}

	public static final BasicLoging logger = LogsDispatcher.getInstatnce().getLoger("TestDevision");
}
