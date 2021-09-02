package daelim.project.eatstagram.security.dto;

import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ValidationMemberDTO {

    @NotBlank
    @Pattern(regexp = "[A-za-z0-9]{6,18}", message = "올바른 형식의 아이디를 입력해주세요.")
    private String username; // 아이디

    @NotBlank(message = "비밀번호는 필수 입력 정보입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "올바른 형식의 비밀번호를 입력해주세요.")
    private String password; // 비밀번호

    @NotBlank(message = "비밀번호 확인은 필수 입력정보 입니다.")
    private String confirmPassword; // 비밀번호 확인

//    @Builder.Default
//    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
//    private boolean checkPassword = false;

    @NotBlank(message = "이름은 필수 입력정보 입니다.")
    @Pattern(regexp = "[가-힣]{2,6}", message = "올바른 형식의 이름을 입력해주세요.")
    private String name; // 이름

    @Pattern(regexp = "([\\w-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$", message = "올바른 형식의 이메일 주소를 입력해주세요.")
    private String email;

    private String nickname;

    private String msg;

    @Builder.Default
    private boolean joinSuccessYn = false;
}