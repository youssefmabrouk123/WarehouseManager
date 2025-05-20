package com.twd.Warehouse.controller;

import com.twd.Warehouse.entity.Problem;
import com.twd.Warehouse.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/public/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping
    public ResponseEntity<Problem> submitProblem(@RequestBody Problem problem) throws MessagingException {
        Problem savedProblem = problemService.saveProblem(problem);
        return ResponseEntity.ok(savedProblem);
    }
}