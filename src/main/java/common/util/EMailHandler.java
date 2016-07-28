package common.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EMailHandler {

	public EMailHandler() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", settings.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", settings.getSmtpStarttlsEnable());
		props.put("mail.smtp.host", settings.getSmtpHost());
		props.put("mail.smtp.port", settings.getSmtpPort());

		session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(settings.getSmtpUsername(), settings.getSmtpPassword());
			}
		});
	}

	public void sendEmail(File file) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(settings.getEmailFrom()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(settings.getEmailTo()));
			message.setSubject(settings.getEmailTitle());

			Multipart content = new MimeMultipart();

			attachText(content, settings.getEmailText());
			attachFile(content, file);

			message.setContent(content);
			message.saveChanges();

			Transport.send(message);
		} catch (MessagingException e) {
			Logger.error("Error while sending email.", e);
		}
	}

	private void attachText(Multipart content, String text) throws MessagingException {
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(text);

		content.addBodyPart(textPart);
	}

	public void attachFile(Multipart content, File file)
			throws MessagingException {

		MimeBodyPart filePart = new MimeBodyPart();
		FileDataSource fileDS = new FileDataSource(file);
		filePart.setDataHandler(new DataHandler(fileDS));
		filePart.setFileName(fileDS.getName());

		content.addBodyPart(filePart);
	}

	private final Session session;
	private final Settings settings = Settings.getInstance();
}
