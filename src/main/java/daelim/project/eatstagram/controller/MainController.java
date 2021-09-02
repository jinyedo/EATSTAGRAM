package daelim.project.eatstagram.controller;

import daelim.project.eatstagram.security.dto.AuthMemberDTO;
import daelim.project.eatstagram.security.dto.ValidationMemberDTO;
import daelim.project.eatstagram.service.emailAuth.EmailAuthDTO;
import daelim.project.eatstagram.service.emailAuth.EmailAuthService;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final EmailAuthService emailTokenService;

    @GetMapping("/")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/")
    public String postLogin() {
        return "login";
    }

    @GetMapping("/join")
    public String getJoin() {
        return "join";
    }

    @PostMapping("/join")
    public String postJoin(@Valid ValidationMemberDTO validationMemberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        ValidationMemberDTO result = memberService.join(validationMemberDTO, errors);
        if (!result.isJoinSuccessYn()) {
            model.addAttribute("validationMemberDTO", validationMemberDTO);
            model.addAttribute("msg", result.getMsg());
            return "join";
        } else {
            redirectAttributes.addFlashAttribute("msg", result.getMsg());
            return "redirect:/";
        }
    }

    @GetMapping("/join/mail")
    public String getJoinMail(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        if (authMemberDTO.isEmailVerified()) {
            return "redirect:/test";
        }
        EmailAuthDTO emailAuthenticationDTO = emailTokenService.createEmailToken(authMemberDTO.getUsername(), authMemberDTO.getEmail());
        model.addAttribute("certificationNumber", emailAuthenticationDTO.getCertificationNumber());
        model.addAttribute("emailAuthId", emailAuthenticationDTO.getEmailAuthId());
        return "join_mail";
    }

    @PostMapping("/join/mail")
    public String postJoinMail(String mailcode, String certificationNumber, String emailAuthId, Model model) {
        if (certificationNumber.equals(mailcode)) {
            memberService.confirmEmail(emailAuthId);
            return "redirect:/test";
        }
        model.addAttribute("msg", "인증번호가 일치하지 않습니다.");
        model.addAttribute("certificationNumber", certificationNumber);
        model.addAttribute("emailAuthId", emailAuthId);
        return "join_mail";
    }


    @GetMapping("/find/password")
    public String getFindPassword() {
        return "findpw";
    }

    @PostMapping("/find/password")
    public String postFindPassword() {
        return "findpw";
    }

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }
}
