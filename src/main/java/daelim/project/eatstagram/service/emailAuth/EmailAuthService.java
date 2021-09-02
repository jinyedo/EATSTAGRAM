package daelim.project.eatstagram.service.emailAuth;

import daelim.project.eatstagram.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService extends BaseService<String, EmailAuth, EmailAuthDTO, EmailAuthRepository> {

    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailTokenRepository;

    // JavaMailSender 객체를 사용하여 Async 방식으로 이메일을 보낸다.
    @Async public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    // 이메일 인증 코드 생성
    public EmailAuthDTO createEmailToken(String username, String receiverEmail){
        EmailAuthDTO emailAuthenticationDTO = EmailAuthDTO.builder()
                .certificationNumber(createNumber())
                .expirationDate(LocalDateTime.now().plusMinutes(5L)) // 만료시간 5분
                .username(username)
                .expired(false)
                .build();
        save(emailAuthenticationDTO);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("인증코드 : " + emailAuthenticationDTO.getCertificationNumber());
        sendEmail(mailMessage);
        return emailAuthenticationDTO;
    }

     // 유효한 인증코드 가져오기
    public EmailAuth findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailAuthId){
        Optional<EmailAuth> emailToken = emailTokenRepository.findByEmailAuthIdAndExpirationDateAfterAndExpired(emailAuthId, LocalDateTime.now(),false);
        return emailToken.orElseThrow();
    };

    //	인증코드 만들기
    public static String createNumber() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
}
