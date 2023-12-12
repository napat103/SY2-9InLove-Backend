package sit.project.projectv1.services;

import sit.project.projectv1.models.EmailDetails;

public interface EmailService {

    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
