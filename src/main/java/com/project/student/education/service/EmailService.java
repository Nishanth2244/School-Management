package com.project.student.education.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;

	public void sendAdminInviteEmail(String email, String fullName, String inviteLink) {




		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Principal Onboarding Form Invitation");

		String emailBody = String.format(
				"Dear %s,%n%n" + "Greetings!%n%n" + "You have been invited to complete the Principal Onboarding Form. "
						+ "Please use the link below to provide the required information:%n%n" + "%s%n%n"
						+ "Kindly complete the form at your earliest convenience.%n%n"
						+ "If you have any questions or require assistance, please feel free to contact us.%n%n"
						+ "Thank you.%n%n" + "Best Regards,%n" + "Administration Team",
				fullName, inviteLink);

		message.setText(emailBody);

		javaMailSender.send(message);
	}

	public void sendDriverInviteEmail(String email, String fullName, String inviteLink) {




		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Principal Onboarding Form Invitation");

		String emailBody = String.format(
				"Dear %s,%n%n" + "Greetings!%n%n" + "You have been invited to complete the Driver Onboarding Form. "
						+ "Please use the link below to provide the required information:%n%n" + "%s%n%n"
						+ "Kindly complete the form at your earliest convenience.%n%n"
						+ "If you have any questions or require assistance, please feel free to contact us.%n%n"
						+ "Thank you.%n%n" + "Best Regards,%n" + "Administration Team",
				fullName, inviteLink);

		message.setText(emailBody);

		javaMailSender.send(message);

	}

	
	public void sendAdminConfirmation(String email, String fullName, String username, String password) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Congratulations! Your Admin Account Has Been Created");

		String emailBody = String.format("Dear %s,%n%n" + "Congratulations!%n%n"
				+ "We are pleased to inform you that you have been successfully appointed as an Administrator in our system.%n%n"
				+ "Your login credentials are as follows:%n%n" + "Username: %s%n" + "Password: %s%n%n"
				+ "Please log in using the above credentials and change your password after your first login for security purposes.%n%n"
				+ "If you experience any issues accessing your account or require assistance, please contact the support team.%n%n"
				+ "We wish you success in your new role and look forward to your contributions.%n%n" + "Best Regards,%n"
				+ "Administration Team", fullName, username, password);

		message.setText(emailBody);
		javaMailSender.send(message);
	}

}
