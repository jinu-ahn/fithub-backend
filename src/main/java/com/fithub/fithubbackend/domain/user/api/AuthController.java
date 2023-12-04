package com.fithub.fithubbackend.domain.user.api;

import com.fithub.fithubbackend.domain.user.application.AuthService;
import com.fithub.fithubbackend.domain.user.dto.SignInDto;
import com.fithub.fithubbackend.domain.user.dto.SignOutDto;
import com.fithub.fithubbackend.domain.user.dto.SignUpDto;
import com.fithub.fithubbackend.domain.user.dto.SignUpResponseDto;
import com.fithub.fithubbackend.global.auth.TokenInfoDto;
import com.fithub.fithubbackend.global.exception.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", responses = {
            @ApiResponse(responseCode = "200", description = "회원 생성"),
            @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "닉네임 중복", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "아이디 중복", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "형식 에러 (이메일 형식 , 비밀번호 형식(8자이상 특수문자 포함), 닉네임 형식(특수문자 제외 한글, 영어, 숫자), 전화번호 형식 (xxx-xxx(xxxx)-xxxx)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpDto signUpDto, BindingResult bindingResult){
        return authService.signUp(signUpDto,bindingResult);
    }

    @Operation(summary = "로그인", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원"),
            @ApiResponse(responseCode = "403", description = "로그인 실패 - 비밀번호 불일치")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<TokenInfoDto> signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.signIn(signInDto, response));
    }

    @Operation(summary = "로그아웃", responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "검증되지 않는 토큰이거나 만료된 access Token"),
            @ApiResponse(responseCode = "403", description = "로그아웃 실패")
    })
    @DeleteMapping("/sign-out")
    public ResponseEntity signOut(@CookieValue(name = "accessToken") String cookieAccessToken,
                                  @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response, HttpServletRequest request){
        SignOutDto signOutDto = SignOutDto.builder().accessToken(cookieAccessToken).build();
        authService.signOut(signOutDto, userDetails, response, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발급", responses = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "검증되지 않는 토큰"),
            @ApiResponse(responseCode = "403", description = "토큰 재발급 실패")
    })
    @PatchMapping("/reissue")
    public  ResponseEntity<TokenInfoDto> reissue(@CookieValue(name = "refreshToken") String cookieRefreshToken,
                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                 HttpServletRequest request,HttpServletResponse response) {
        return ResponseEntity.ok(authService.reissue(cookieRefreshToken, request, response));
    }
}
