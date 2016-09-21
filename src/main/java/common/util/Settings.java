package common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import common.Const;

public class Settings {

	private Settings() {
		readProperties();
	}

	private void readProperties() {
		Logger.info("Reading settings...");
		FileInputStream in = null;
		Properties property = new Properties();

		try {
			in = new FileInputStream(Const.SETTINGS_FILE_NAME);
			property.load(in);

			dir = property.getProperty("main.dir");
			Logger.info("Scanned directory is set to <" + dir + ">");

			mask = property.getProperty("main.mask");
			Logger.info("File mask is set to <" + mask + ">");

			sheet = property.getProperty("main.sheet");
			Logger.info("Sheet is set to <" + sheet + ">");

			encoding = property.getProperty("main.encoding");
			Logger.info("Encoding is set to <" + encoding + ">");

			timeout = Integer.valueOf(property.getProperty("main.timeout"));
			Logger.info("Timeout is set to <" + timeout + "> seconds");

			emailTo = property.getProperty("email.to");
			emailFrom = property.getProperty("email.from");
			emailTitle = property.getProperty("email.title");
			emailText = property.getProperty("email.text");

			smtpAuth = property.getProperty("mail.smtp.auth");
			smtpStarttlsEnable = property.getProperty("mail.smtp.starttls.enable");
			smtpHost = property.getProperty("mail.smtp.host");
			smtpPort = property.getProperty("mail.smtp.port");
			smtpUsername = property.getProperty("mail.smtp.username");
			smtpPassword = property.getProperty("mail.smtp.password");

			Logger.info("Settings successfully extracted.\r\n");
		} catch (IOException ex) {
			Logger.error("Error while reading property file: " + Const.SETTINGS_FILE_NAME, ex);
		} finally {
			IOHelper.closeStream(in);
		}
	}

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public String getDir() {
		return dir;
	}

	public String getMask() {
		return mask;
	}

	public String getEncoding() {
		return encoding;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public String getEmailText() {
		return emailText;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public String getSmtpStarttlsEnable() {
		return smtpStarttlsEnable;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public String getSheet() {
		return sheet;
	}

	private static Settings instance = null;

	private String dir;
	private String mask;
	private String encoding;
	private int timeout;
	private String sheet;

	private String emailTo;
	private String emailFrom;
	private String emailTitle;
	private String emailText;

	private String smtpAuth;
	private String smtpStarttlsEnable;
	private String smtpHost;
	private String smtpPort;
	private String smtpUsername;
	private String smtpPassword;
}
