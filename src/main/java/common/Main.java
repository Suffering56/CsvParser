package common;

import java.io.IOException;
import java.net.URISyntaxException;

import common.util.Settings;

public class Main {
	public static void main(String[] args) throws IOException, URISyntaxException {
		Settings.getInstance();

		FilesHandler handler = new FilesHandler();
		while (true) {
			handler.start();
			sleep();
		}
	}

	private static void sleep() {
		try {
			Thread.sleep(Settings.getInstance().getTimeout() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
