package org.goormthon.seasonthon.nocheongmaru;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.AuthController;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
        AuthController.class
    }
)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public abstract class ControllerTestSupport {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @MockitoBean
    protected AuthService authService;
    
}
