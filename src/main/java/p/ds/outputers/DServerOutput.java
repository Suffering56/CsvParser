package p.ds.outputers;

import java.io.BufferedReader;
import java.io.FileReader;

import common.util.Logger;
import proxy.util.config.ConfigData;
import pt.dserver.api.IDServerSession;
import pt.dserver.api.SessionDispatcher;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 *
 * <p>
 * Company: Prime
 * </p>
 *
 * @author Timofey
 * @version 1.0
 */
public class DServerOutput {
	protected IDServerSession session;

	public DServerOutput() {
		/**
		 * Загрузка конфигурационного файла.
		 */
		String sCfgFileName = "DserverAPI.cfg";
		ConfigData cfg = null;
		try {
			BufferedReader bfr_config = new BufferedReader(new FileReader(sCfgFileName));
			cfg = new ConfigData(bfr_config);
			bfr_config.close();
		} catch (Exception e) {
			System.out.println("Exception, while looking for config file:" + sCfgFileName + "\r\n " + e.toString());
			System.exit(-1);
		}

		/**
		 * Далее следующие строки выбирают из конфигурационного файла
		 * необходимые элементы конфигурации.
		 */
		String DESTINATION_ADDRESS = cfg.getParameter("DESTINATION_ADDRESS");
		String DESTINATION_PORT = cfg.getParameter("DESTINATION_PORT");
		String DESTINATION_SESSION_NAME = cfg.getParameter("DESTINATION_SESSION_NAME");
		String DESTINATION_SHEET_NAME = cfg.getParameter("DESTINATION_SHEET_NAME");
		String DESTINATION_USER_NAME = cfg.getParameter("DESTINATION_USER_NAME");
		String DESTINATION_USER_PASS = cfg.getParameter("DESTINATION_USER_PASS");
		String DESTINATION_TIMEOUT = cfg.getParameter("DESTINATION_TIMEOUT");
		String DESTINATION_QUEUE_TIMEOUT = cfg.getParameter("DESTINATION_QUEUE_TIMEOUT");

		/**
		 * В случае если какойнибудь из элементов конфига - отсутствует
		 */
		if (DESTINATION_ADDRESS == null ||
				DESTINATION_PORT == null ||
				DESTINATION_SESSION_NAME == null ||
				DESTINATION_SHEET_NAME == null ||
				DESTINATION_USER_NAME == null ||
				DESTINATION_USER_PASS == null ||
				DESTINATION_TIMEOUT == null ||
				DESTINATION_QUEUE_TIMEOUT == null) {
			System.out.println("Config file - some parameters missing, can't continue.");
			System.exit(-1);
		}

		int destport = 6013;
		try {
			destport = Integer.parseInt(DESTINATION_PORT);
		} catch (Exception e) {
			System.out.println("Error parisng DESTINATION port number:" + DESTINATION_PORT + ", seting it to default:" + destport);
		}

		long destTimeout = 10000;
		try {
			destTimeout = Long.parseLong(DESTINATION_TIMEOUT);
		} catch (Exception e) {
			System.out.println("Error parsing DESTINATION_TIMEOUT value:" + DESTINATION_TIMEOUT + ", seting it to default:" + destTimeout);
		}

		long destQueueTimeout = 10000;
		try {
			destTimeout = Long.parseLong(DESTINATION_QUEUE_TIMEOUT);
		} catch (Exception e) {
			System.out.println(
					"Error parsing DESTINATION_QUEUE_TIMEOUT value:" + DESTINATION_QUEUE_TIMEOUT + ", seting it to default:" + destQueueTimeout);
		}

		/**
		 * Собственно оно
		 */
		SessionDispatcher sd = SessionDispatcher.getInstance();

		// Получение сессии
		session = sd.aquireSession(DESTINATION_SESSION_NAME, DESTINATION_SHEET_NAME, DESTINATION_ADDRESS, DESTINATION_USER_PASS,
				DESTINATION_USER_NAME, destport, destTimeout, destQueueTimeout);
	}

	public void sendData(String field, String ticker, String value) {
		//		Logger.info("Sending to onlinx: " + ticker + " " + field + " " + value);
		//		System.out.println("Sending to onlinx: " + ticker + " " + field + " " + value);
		session.sendData(field, ticker, value);
	}
}
