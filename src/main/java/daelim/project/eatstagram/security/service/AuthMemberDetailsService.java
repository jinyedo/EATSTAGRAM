package daelim.project.eatstagram.security.service;

import daelim.project.eatstagram.security.dto.AuthMemberDTO;
import daelim.project.eatstagram.service.member.Member;
import daelim.project.eatstagram.service.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberUserDetailsService loadUserByUsername : " + username);

        Optional<Member> result = memberRepository.findByUsernameAndFormSocial(username,false);
        if (result.isPresent()) {
            Member member = result.get();
            log.info("Member : " + member);

            return new AuthMemberDTO(
                    member.getUsername(),
                    member.getPassword(),
                    member.getName(),
                    member.getEmail(),
                    member.getNickname(),
                    member.isFormSocial(),
                    member.isEmailVerified(),
                    member.getRoleSet().stream().map(role ->
                            new SimpleGrantedAuthority("ROLE_" + role.name())
                    ).collect(Collectors.toSet())
            );
        }
        throw new UsernameNotFoundException("Check ID");
    }
}
