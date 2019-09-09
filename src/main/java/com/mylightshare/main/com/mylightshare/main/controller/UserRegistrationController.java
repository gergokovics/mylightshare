package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.dao.AuthorityRepository;
import com.mylightshare.main.com.mylightshare.main.dao.ConfirmationTokenRepository;
import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.Authority;
import com.mylightshare.main.com.mylightshare.main.entity.ConfirmationToken;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.service.EmailSenderService;
import com.mylightshare.main.com.mylightshare.main.service.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

@Controller
public class UserRegistrationController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MimeMessage confirmMessage;

    @GetMapping("/register")
    public String register(Model model) {

        User user = new User();

        model.addAttribute("user", user);

        System.out.println("Register page! GET");

        return "register";

    }

    @PostMapping("/register")
    public String register(@Valid User newUser, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        validateCredentials(newUser, bindingResult);

        // Final check for errors
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.out.println(error.toString());
            }
            return "register";

        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        newUser.setEmail(newUser.getEmail().toLowerCase());

        newUser.setStorageSpace(FileSystemStorageService.DEFAULT_USER_STORAGE_SPACE);
        userRepository.save(newUser);

        authorityRepository.save(new Authority(newUser.getUsername(), "ROLE_USER"));

        ConfirmationToken confirmationToken = new ConfirmationToken(newUser);

        confirmationTokenRepository.save(confirmationToken);


        try {
            createConfirmMessage(newUser, confirmationToken);

            emailSenderService.sendEmail(confirmMessage);
            
        } catch (Exception e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("message",
                    "Registration failed! Please contact the site administrator for help.");

            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("message",
                "Success! Please confirm your email address to finish the registration.");


        return "redirect:/login";
    }

    private void createConfirmMessage(@Valid User user, ConfirmationToken confirmationToken) throws MessagingException {
        confirmMessage = emailSenderService.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(confirmMessage, true);

        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject("Email Verification");
        messageHelper.setText("To confirm your account, please click here: "
                + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());
    }


    @GetMapping("/confirm-account")
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken, RedirectAttributes redirectAttributes){

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            User user = userRepository.findByUsername(token.getUser().getUsername());
            user.setEnabled(true);
            userRepository.save(user);
            model.addAttribute("confirmMessage", "Your email has been verified!");
        } else {
            model.addAttribute("confirmMessage", "Invalid token!");
        }

        redirectAttributes.addFlashAttribute("message",
                "Account confirmed! You can log in now with your username and password.");

        return "redirect:/login";
    }

    private void validateCredentials(@Valid User user, BindingResult bindingResult) {

        // Check password length
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            ObjectError error = new FieldError("user", "password", "Password length must be in between 6 and 20 characters.");
            bindingResult.addError(error);
        }

        // Check if email exists
        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        if (existingUser != null) {
            ObjectError error = new FieldError("user", "email", "Email is taken. Please try another one.");
            bindingResult.addError(error);
        }

        // Check if username exists
        existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            ObjectError error = new FieldError("user", "username", "Username is taken. Please try another one.");
            bindingResult.addError(error);
        }
    }
}
