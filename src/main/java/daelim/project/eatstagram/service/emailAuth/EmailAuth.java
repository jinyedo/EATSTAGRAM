package daelim.project.eatstagram.service.emailAuth;

import daelim.project.eatstagram.service.base.BaseEntity;
import daelim.project.eatstagram.service.base.DTOKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class EmailAuth extends BaseEntity {

    @Id @DTOKey("EA")
    private String emailAuthId;

    private String certificationNumber;

    private LocalDateTime expirationDate; // 만료 시간

    private boolean expired; // 토큰 만료 여부

    private String username;

    // 토큰 사용으로 인한 만료
    public void useToken(){
        expired = true;
    }
}
