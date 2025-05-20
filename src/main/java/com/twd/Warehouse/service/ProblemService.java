package com.twd.Warehouse.service;

import com.twd.Warehouse.entity.Problem;
import com.twd.Warehouse.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private EmailService emailService;

    public Problem saveProblem(Problem problem) throws MessagingException {
        // Save the problem to the database
        Problem savedProblem = problemRepository.save(problem);

        // Send email notification to admin
        emailService.sendProblemNotification(
                problem.getTitle(),
                problem.getUserId(),
                problem.getProblemType(),
                problem.getDescription()
        );

        return savedProblem;
    }
}