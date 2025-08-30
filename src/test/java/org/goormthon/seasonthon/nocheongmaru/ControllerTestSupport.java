package org.goormthon.seasonthon.nocheongmaru;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
    }
)
// TODO : Security 설정 시, 주석 해제
//@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public abstract class ControllerTestSupport {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
}
