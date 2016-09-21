package common;

import common.util.Settings;
import pt.dserver.api.MultiSheetSessionMaintaner;

public class MultiSheetOutput {

	public MultiSheetOutput() {
		multiSheetSession = new MultiSheetSessionMaintaner();
	}

	public void sendData(String field, String ticker, String value) {
		//		Logger.info("Sending to onlinx: " + ticker + " " + field + " " + value);
		//		System.out.println("Sending to onlinx: " + Settings.getInstance().getSheet() + " " + ticker + " " + field + " " + value);
		//		session.sendData(field, ticker, value);
		multiSheetSession.sendData(Settings.getInstance().getSheet().trim(), field.trim(), ticker.trim(), value.trim());
	}

	private MultiSheetSessionMaintaner multiSheetSession;
}
