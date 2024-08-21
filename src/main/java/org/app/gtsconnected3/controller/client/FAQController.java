package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.dto.QuestionForm;
import org.app.gtsconnected3.service.EmailServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class FAQController {

    private final EmailServiceInt emailService;

    @Autowired
    public FAQController(EmailServiceInt emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("questionForm", new QuestionForm());
        log.info("FAQ page requested");
        return "faq";
    }

    @PostMapping("/question")
    public String question(Model model, QuestionForm questionForm) {
        emailService.sendEmailToAdmin(questionForm);
        model.addAttribute("info", "La tua domanda è stata inviata correttamente. Grazie!");
        return "info";
    }
}
