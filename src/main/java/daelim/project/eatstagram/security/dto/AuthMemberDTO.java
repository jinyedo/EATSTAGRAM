package daelim.project.eatstagram.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter @Setter
public class AuthMemberDTO extends User implements OAuth2User {

    private String username;
    private String email;
    private String name;
    private String password;
    private String nickname;
    private String socialType;
    private boolean formSocial;
    private boolean emailVerified;
    private Map<String, Object> attr;

    public AuthMemberDTO(
            String username,
            String password,
            String name,
            String email,
            String nickname,
            boolean formSocial,
            boolean emailVerified,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.formSocial = formSocial;
        this.emailVerified = emailVerified;
    }

    public AuthMemberDTO(
            String username,
            String password,
            String name,
            String email,
            String nickname,
            boolean formSocial,
            boolean emailVerified,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr) {

        this(username, password, name, email, nickname, formSocial, emailVerified, authorities);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
