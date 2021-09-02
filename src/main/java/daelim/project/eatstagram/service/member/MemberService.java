package daelim.project.eatstagram.service.member;

import daelim.project.eatstagram.security.MemberRole;
import daelim.project.eatstagram.security.dto.ValidationMemberDTO;
import daelim.project.eatstagram.service.base.BaseService;
import daelim.project.eatstagram.service.emailAuth.EmailAuth;
import daelim.project.eatstagram.service.emailAuth.EmailAuthRepository;
import daelim.project.eatstagram.service.emailAuth.EmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService extends BaseService<String, Member, MemberDTO, MemberRepository> {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthenticationRepository;
    private final EmailAuthService emailTokenService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ValidationMemberDTO join(ValidationMemberDTO dto, Errors errors) {
        if (validationCheck(errors)){
            dto.setMsg("알 수 없는 이유로 회원가입에 실패했습니다. 관리자에게 문의해 주세요.");
            return dto;
        }
        Optional<Member> result =  memberRepository.findByUsernameAndFormSocial(dto.getUsername(), false);
        if (result.isPresent()) {
            dto.setMsg(dto.getUsername() + "은 이미 존재하는 아이디입니다.");
            return dto;
        } else {
            Member member = Member.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .email(dto.getEmail())
                        .name(dto.getName())
                        .nickname(dto.getNickname())
                        .formSocial(false)
                        .emailVerified(false)
                        .build();
            member.addMemberRole(MemberRole.USER);
            memberRepository.save(member);
        }
        dto.setMsg("회원가입 성공");
        dto.setJoinSuccessYn(true);
        return dto;
    }

    public String checkUsername(ValidationMemberDTO dto) {
        Optional<Member> result = memberRepository.findByUsernameAndFormSocial(dto.getUsername(), false);
        if (result.isPresent()) return "fail";
        return "success";
    }

    private boolean validationCheck(Errors errors) {
        if (errors.hasErrors()) {
            log.info("-----유효성 검사 오류 종류-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------");
            return true;
        }
        return false;
    }

    public void confirmEmail(String token) {
        EmailAuth emailToken = emailTokenService.findByEmailTokenIdAndExpirationDateAfterAndExpired(token);
        log.info(emailToken.toString());
        Member member = memberRepository.findById(emailToken.getUsername()).orElseThrow();
        emailToken.useToken();	// 토큰 만료 로직을 구현해주면 된다. ex) expired 값을 true로 변경
        member.emailVerifiedSuccess();	// 유저의 이메일 인증 값 변경 로직을 구현해주면 된다. ex) emailVerified 값을 true로 변경
        memberRepository.save(member);
        emailAuthenticationRepository.save(emailToken);
    }
}
