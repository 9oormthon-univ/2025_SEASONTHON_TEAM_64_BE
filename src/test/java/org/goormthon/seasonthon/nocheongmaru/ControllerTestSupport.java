package org.goormthon.seasonthon.nocheongmaru;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.controller.AuthController;
import org.goormthon.seasonthon.nocheongmaru.domain.auth.service.AuthService;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.controller.FeedController;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.service.FeedService;
import org.goormthon.seasonthon.nocheongmaru.domain.information.controller.InformationController;
import org.goormthon.seasonthon.nocheongmaru.domain.information.service.InformationService;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.controller.MissionController;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
        AuthController.class,
        InformationController.class,
        MissionController.class,
        FeedController.class
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
    
    @MockitoBean
    protected InformationService informationService;
    
    @MockitoBean
    protected MissionService missionService;
    
    @MockitoBean
    protected FeedService feedService;
    
}
