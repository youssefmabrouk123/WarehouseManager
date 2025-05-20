package com.twd.Warehouse.service;

import com.twd.Warehouse.entity.OurUsers;
import com.twd.Warehouse.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OurUserRepo ourUsersRepository;

    public void sendProblemNotification(String title, String userId, String problemType, String description) throws MessagingException {
        // Fetch all admins (users with role ADMIN)
        List<OurUsers> admins = ourUsersRepository.findByRole("ADMIN");

        for (OurUsers admin : admins) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(admin.getEmail());
            helper.setSubject("New Problem Reported: " + title);
            helper.setText(
                    "<h3>New Problem Submission</h3>" +
                            "<p><strong>Title:</strong> " + title + "</p>" +
                            "<p><strong>User ID:</strong> " + userId + "</p>" +
                            "<p><strong>Problem Type:</strong> " + problemType + "</p>" +
                            "<p><strong>Description:</strong> " + description + "</p>",
                    true
            );

            mailSender.send(message);
        }
    }
}