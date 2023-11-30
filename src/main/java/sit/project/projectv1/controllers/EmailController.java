package sit.project.projectv1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.models.EmailDetails;
import sit.project.projectv1.repositories.EmailRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/sendmail")
public class EmailController {

    @Autowired
    private EmailRepository emailRepository;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details) {
        String status = emailRepository.sendSimpleMail(details);
        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        String status = emailRepository.sendMailWithAttachment(details);
        return status;
    }
}
