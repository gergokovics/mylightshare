package com.mylightshare.main.com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.dao.AuthorityRepository;
import com.mylightshare.main.com.mylightshare.main.dao.UserRepository;
import com.mylightshare.main.com.mylightshare.main.entity.User;
import com.mylightshare.main.com.mylightshare.main.formview.AccountUpdateForm;
import com.mylightshare.main.com.mylightshare.main.modelattribute.UserModelAttribute;
import com.mylightshare.main.com.mylightshare.main.service.EmailSenderService;
import com.mylightshare.main.com.mylightshare.main.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

@Controller
public class UserAccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping("/login")
    public String login(Model model, Authentication auth) {

        if (auth != null) {
            return "redirect:/";
        }

        User user = new User();

        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate() {

        return "home";
    }

    @GetMapping("/logout")
    public String logout() {

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/login";
    }

    @GetMapping("/account-settings")
    public String accountSettings(Model model, Authentication auth) {

        User user = userRepository.findByUsername(auth.getName());

        AccountUpdateForm accountUpdateForm = new AccountUpdateForm();

        accountUpdateForm.setEmail(user.getEmail());
        accountUpdateForm.setUsername(user.getUsername());

        model.addAttribute("accountUpdateForm", accountUpdateForm);

        UserModelAttribute userModelAttribute = new UserModelAttribute(user);
        userModelAttribute.setPage("account-settings");
        model.addAttribute("user", userModelAttribute);

        return "account-settings";
    }

    @PostMapping("/account-settings")
    public String accountUpdate(Model model, @Valid @ModelAttribute AccountUpdateForm accountUpdateForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmailIgnoreCase(accountUpdateForm.getEmail());
        
        validateUserUpdate(user, accountUpdateForm, bindingResult);

        if (bindingResult.hasErrors()) {

            UserModelAttribute userModelAttribute = new UserModelAttribute(user);
            userModelAttribute.setPage("account-settings");
            model.addAttribute("user", userModelAttribute);

            return "account-settings";
        } else {
            
            // Apply account updates

            String updatedPassword = accountUpdateForm.getNewPassword();

            if (updatedPassword.length() > 0) {
                updatedPassword = passwordEncoder.encode(updatedPassword);
                user.setPassword(updatedPassword);

                userRepository.save(user);

                redirectAttributes.addFlashAttribute("message", "You have successfully updated your password.");
            }

            return "redirect:/account-settings";

        }

    }

    @GetMapping("/password-recovery")
    public String recoverPassword(Model model, Authentication auth) {

        if (auth != null) {
            return "redirect:/";
        }

        User user = new User();

        model.addAttribute("user", user);

        return "password-recovery";
    }

    @PostMapping("/password-recovery")
    public String recoverPassword(@ModelAttribute("user") User userModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmailIgnoreCase(userModel.getEmail());

        if (user == null) {
            ObjectError error = new FieldError("user", "email", "Invalid e-mail address!");
            bindingResult.addError(error);

            return "password-recovery";
        } else {

            String tmpPassword = Generator.getUniqueID(12);

            try {
                user.setPassword(passwordEncoder.encode(tmpPassword));

                System.out.println(user.getPassword());

                User userSaved = userRepository.save(user);

                System.out.println(userSaved.getPassword());

                MimeMessage message = createResetMessage(user, tmpPassword);

                emailSenderService.sendEmail(message);
            } catch (MessagingException me) {
                throw new RuntimeException("Error while sending password reset message to user: "+user.getEmail());
            }
        }

        redirectAttributes.addFlashAttribute("message",
                "Your password has been reset successfully. Please check your e-mail account for details.");

        return "redirect:/login";
    }

    private MimeMessage createResetMessage(User user, String newPassword) throws MessagingException {

        MimeMessage message = emailSenderService.createMimeMessage();
        message.setContent(message, "text/html; charset=utf-8");

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        String messageContent = "<p>Your password has been reset to: " + newPassword
                + "</p><p>Please log in and change your temporary password to secure your account.<br>"
                + "If you did not issue this password reset please contact us.</p>";

        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject("Password Reset");
        messageHelper.setText(messageContent, true);

        return message;
    }

    private void validateUserUpdate(User user, AccountUpdateForm accountUpdateForm, BindingResult bindingResult) {

        // Check if current password is correct
        String inputPassword = accountUpdateForm.getCurrentPassword();

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            ObjectError error = new FieldError("accountUpdateForm", "currentPassword", "Invalid password!");
            bindingResult.addError(error);
        }

        // Check if password change is needed
        String newPassword = accountUpdateForm.getNewPassword();

        if (newPassword.length() > 0) {

            // Check new password length
            if (newPassword.length() < 6 || newPassword.length() > 20) {
                ObjectError error = new FieldError("accountUpdateForm", "newPassword",
                        "Password length must be in between 6 and 20 characters.");
                bindingResult.addError(error);
            }
        }

        // Check if username change is needed
        String username =  user.getUsername().toLowerCase();
        String updatedUsername = accountUpdateForm.getUsername().toLowerCase();

        if (!username.equals(updatedUsername)) {
            User existingUser = userRepository.findByUsername(accountUpdateForm.getUsername());
            // Check if new username is taken
            if (existingUser != null) {
                ObjectError error = new FieldError("accountUpdateForm", "username",
                        "Username is taken. Please try a different one.");
                bindingResult.addError(error);
            }
        }

    }

}
