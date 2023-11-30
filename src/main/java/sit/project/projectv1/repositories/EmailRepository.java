package sit.project.projectv1.repositories;

import sit.project.projectv1.models.EmailDetails;

public interface EmailRepository {

    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
