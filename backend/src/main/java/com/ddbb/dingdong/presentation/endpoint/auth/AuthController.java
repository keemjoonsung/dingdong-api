package com.ddbb.dingdong.presentation.endpoint.auth;

import com.ddbb.dingdong.application.exception.APIException;
import com.ddbb.dingdong.application.usecase.auth.CheckEmailUseCase;
import com.ddbb.dingdong.application.usecase.auth.LoginUseCase;
import com.ddbb.dingdong.application.usecase.auth.LogoutUseCase;
import com.ddbb.dingdong.application.usecase.auth.SignUpUseCase;
import com.ddbb.dingdong.domain.auth.service.error.AuthErrors;
import com.ddbb.dingdong.domain.common.exception.DomainException;
import com.ddbb.dingdong.infrastructure.auth.security.AuthUser;
import com.ddbb.dingdong.infrastructure.auth.security.annotation.LoginUser;
import com.ddbb.dingdong.presentation.endpoint.auth.exchanges.CheckEmailDto;
import com.ddbb.dingdong.presentation.endpoint.auth.exchanges.SignUpRequestDto;
import com.ddbb.dingdong.presentation.endpoint.auth.exchanges.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final CheckEmailUseCase checkEmailUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        LoginUseCase.Param param = new LoginUseCase.Param(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        try {
            loginUseCase.execute(param);
        } catch (DomainException e) {
            throw new APIException(e, HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok().build();
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @LoginUser AuthUser user
    ) {
        Long userId = user.id();
        LogoutUseCase.Param param = new LogoutUseCase.Param(userId);
        try {
            logoutUseCase.execute(param);
        } catch (DomainException e) {
            throw new APIException(e, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<Void> checkEmail(
            @RequestBody CheckEmailDto email
    ) {
        CheckEmailUseCase.Param param = new CheckEmailUseCase.Param(email);
        try {
            checkEmailUseCase.execute(param);
        } catch (DomainException e) {
            if (e.error.equals(AuthErrors.EMAIL_ALREADY_EXISTS)) {
                throw new APIException(e, HttpStatus.CONFLICT);
            } else {
                throw new APIException(e, HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        SignUpUseCase.Param param = new SignUpUseCase.Param(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getHome(),
                signUpRequestDto.getSchoolId()
        );
        try {
            signUpUseCase.execute(param);
        } catch (DomainException e) {
            if (e.error.equals(AuthErrors.EMAIL_ALREADY_EXISTS)) {
                throw new APIException(e, HttpStatus.CONFLICT);
            } else {
                throw new APIException(e, HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Void> check(
            @LoginUser AuthUser user
    ) {
        return ResponseEntity.ok().build();
    }
}
