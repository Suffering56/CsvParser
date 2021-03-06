package common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import common.util.DataEnity;
import common.util.EMailHandler;
import common.util.IOHelper;
import common.util.Logger;
import common.util.Settings;

public class FilesHandler {

	public FilesHandler() {
		readLastModifiedProperty();
		newLastModified = lastModified;
	}

	public void start() {
		Logger.info("\r\nStart scanning... ");
		File[] files = new File(Settings.getInstance().getDir()).listFiles(filter);
		// List<DataEnity> sendList = new ArrayList<>();
		List<File> newFilesList = new ArrayList<>();

		if (files != null) {
			MultiSheetOutput session = null;

			for (File file : files) {
				if (isNew(file)) {
					List<DataEnity> parsingResult = parseFile(file);
					if (!parsingResult.isEmpty()) {
						if (session == null) {
							session = new MultiSheetOutput();
						}
						send(session, parsingResult);
					}

					// sendList.addAll(parsingResult);
					newFilesList.add(file);
				}
			}
			Logger.info("Scanning is completed.\r\n");

			if (newLastModified > lastModified) {
				saveLastModifiedProperty();
			}

			// if (!sendList.isEmpty()) {
			// send(sendList);
			// }

			if (!newFilesList.isEmpty()) {
				if (Settings.getInstance().getEmailTo() != null && !Settings.getInstance().getEmailTo().isEmpty()) {
					Logger.info("Sending email letters... count: " + newFilesList.size());
					EMailHandler eMailHandler = new EMailHandler();
					for (File file : newFilesList) {
						eMailHandler.sendEmail(file);
					}
					Logger.info("Sending email letters is completed");
				} else {
					Logger.info("Empty param \"email.to\"! Mail will not be sent. \r\n");
				}
			}
		} else {
			Logger.error("Error while scanning directory: " + Settings.getInstance().getDir() + ". This directory is null");
		}
	}

	private void send(MultiSheetOutput session, List<DataEnity> sendList) {
		Logger.info("Sending data...");
		// session = new DServerOutput();
		int counter = 0;
		for (DataEnity entity : sendList) {
			if (counter >= Settings.getInstance().getPacketSize()) {
				counter = 0;
				try {
					Logger.info("Sent packet. Sleep: " + Settings.getInstance().getPacketDelay());
					Thread.sleep(Settings.getInstance().getPacketDelay());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			session.sendData(entity.getField(), entity.getName(), entity.getValue());
			counter++;
		}
		Logger.info("Data sending is completed");
	}

	private boolean isNew(File file) {
		if (lastModified < file.lastModified()) {
			if (newLastModified < file.lastModified()) {
				newLastModified = file.lastModified();
			}
			return true;
		}
		return false;
	}

	private static List<DataEnity> parseFile(File file) {
		Logger.info("Parsing new file: " + file.getName() + " ...");

		List<DataEnity> sendList = new ArrayList<>();
		List<String> lines = null;
		int instrumentColumnIndex = Settings.getInstance().getInstrumentColumnIndex();

		try {
			lines = Files.readAllLines(file.toPath(), Charset.forName(Settings.getInstance().getEncoding()));
		} catch (IOException e) {
			Logger.error("Error while reading file: " + file.getName(), e);
		}

		if (lines != null) {
			String[] fields = lines.get(0).split(";");

			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				String[] values = line.split(";");

				for (int j = 0; j < values.length; j++) {
					if (j != instrumentColumnIndex) {
						String value = values[j].replaceAll(",", ".");
						if (j < fields.length) {
							String name = values[instrumentColumnIndex];
							String field = fields[j];
							sendList.add(new DataEnity(name, field, value));
						}
					}
				}
			}
		}
		return sendList;
	}

	private void saveLastModifiedProperty() {
		lastModified = newLastModified;
		FileOutputStream out = null;

		Properties property = new Properties();
		property.put("lastModified", String.valueOf(lastModified));

		Logger.info("Variable \"lastModified\" successfully updated. new value = " + lastModified + "\r\n");

		try {
			out = new FileOutputStream(Const.TEMP_FILE_NAME);
			property.store(out, null);
		} catch (IOException ex) {
			Logger.error("Error while saving property file: " + Const.TEMP_FILE_NAME, ex);
		} finally {
			IOHelper.closeStream(out);
		}
	}

	private void readLastModifiedProperty() {
		FileInputStream in = null;
		Properties property = new Properties();
		try {
			in = new FileInputStream(Const.TEMP_FILE_NAME);
			property.load(in);
			lastModified = Long.valueOf(property.getProperty("lastModified"));
			Logger.info("Variable \"lastModified\" successfully extracted. value = " + lastModified + "\r\n");
		} catch (IOException ex) {
			Logger.error("Error while reading property file: " + Const.TEMP_FILE_NAME, ex);
		} finally {
			IOHelper.closeStream(in);
		}
	}

	private long lastModified;
	private long newLastModified;
	// private DServerOutput session;

	private FileFilter filter = new FileFilter() {
		public boolean accept(File file) {
			return (file.isFile() && file.getName().endsWith(".csv") && file.getName().contains(Settings.getInstance().getMask()));
		}
	};
}
