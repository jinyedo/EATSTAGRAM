package daelim.project.eatstagram.security.handler;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        log.info("----------로그인 실패 처리----------");
        String msg = null;

        log.info("exception - " + exception);

        if (exception instanceof BadCredentialsException) {
            msg = "아이디나 비밀번호가 올바르지 않습니다.";
        }

        request.setAttribute("msg", msg);
        request.getRequestDispatcher("/?error=true").forward(request, response);
    }
}
