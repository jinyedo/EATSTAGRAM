package daelim.project.eatstagram.service.member;

import daelim.project.eatstagram.security.MemberRole;
import daelim.project.eatstagram.service.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Member extends BaseEntity {

    @Id
    private String username; // 아이디

    private String password;

    private String name;

    private String email;

    private String nickname;

    private boolean formSocial; // 소셜 로그인 유무

    private String socialType; // 소셜 로그인 타입

    private boolean emailVerified;

    @Builder.Default
    @Enumerated(EnumType.STRING) @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

    public void emailVerifiedSuccess(){
        emailVerified = true;
    }
}
