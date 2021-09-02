package daelim.project.eatstagram.security.service;

import daelim.project.eatstagram.security.MemberRole;
import daelim.project.eatstagram.security.dto.AuthMemberDTO;
import daelim.project.eatstagram.service.member.Member;
import daelim.project.eatstagram.service.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2MemberDetailsService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 소셜 로그인한 사용자의 정보 받아오기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("====================");
        log.info("userRequest : " + userRequest);

        // OAuth 로 연결한 클라이언트 이름
        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName : " + clientName);
        log.info(userRequest.getAdditionalParameters()); // 연결때 사용한 파라미터

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("--------------------");
        // 처리 결과로 나오는 OAuth2User 객체의 내부의 값 확인
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " : " + v);
        });
        log.info("--------------------");

        HashMap<String, String> propertiesHashMap = oAuth2User.getAttribute("properties");
        String id = clientName.equals("Google")
                ? oAuth2User.getAttribute("email")
                : Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
        String name = clientName.equals("Google")
                ? oAuth2User.getAttribute("name")
                : Objects.requireNonNull(propertiesHashMap).get("nickname");

        Member member = saveSocialMember(id, name, clientName);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getUsername(),
                member.getPassword(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                true,
                member.isEmailVerified(),
                member.getRoleSet().stream().map(memberRole ->
                        new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        authMemberDTO.setName(member.getName());

        log.info("====================");
        return authMemberDTO;
    }

    // 받아온 데이터로 회원가입 처리
    private Member saveSocialMember(String username, String name, String socialType) {

        Optional<Member> result = memberRepository.findByUsernameAndFormSocial(username, true);

        // 기존에 동일한 아이디로 가입한 회원이 있는 경우 조회만
        if (result.isPresent()) {
            return result.get();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode("1111"))
                .name(name)
                .formSocial(true)
                .emailVerified(true)
                .socialType(socialType)
                .build();

        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);

        return member;
    }
}
