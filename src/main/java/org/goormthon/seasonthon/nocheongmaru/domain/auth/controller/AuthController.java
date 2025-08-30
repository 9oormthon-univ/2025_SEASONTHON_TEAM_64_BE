package org.goormthon.seasonthon.nocheongmaru.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.docs.AuthControllerDocs;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.dto.request.ReissueTokenRequest;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.AuthService;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController implements AuthControllerDocs {
    
    private final AuthService authService;
    
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@RequestBody @Valid ReissueTokenRequest reissueTokenRequest) {
        TokenResponse tokenResponse = authService.reissueToken(reissueTokenRequest.toServiceRequest());
        return ResponseEntity.ok(tokenResponse);
    }
    
}
