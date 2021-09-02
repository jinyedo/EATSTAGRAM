package daelim.project.eatstagram.service.emailAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {
    Optional<EmailAuth> findByEmailAuthIdAndExpirationDateAfterAndExpired(
            String emailAuthId,
            LocalDateTime now,
            boolean expired);
}
