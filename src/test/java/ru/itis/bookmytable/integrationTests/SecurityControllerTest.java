package ru.itis.bookmytable.integrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.itis.bookmytable.dto.JwtTokenResponse;
import ru.itis.bookmytable.dto.SignUpRequest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityControllerTest extends BaseTest {

    private final String SIGN_UP = "/sign-up";
    private final String HELLO_WORLD = "/hello-world";
    private final String SIGN_IN = "/sign-in";
    private final String LOGOUT = "/logout";

    @Test
    public void signUp_phoneNumberAlreadyTaken() throws Exception {

        SignUpRequest request = new SignUpRequest("89991346847", "password12345", "emil", "davlyatov");
        var response = mockMvc.perform(post(SIGN_UP)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        // Убеждаемся, что регистрация выполнена успешно
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.token").hasJsonPath(),
                jsonPath("$.token").isNotEmpty()
        );

        assertTrue(userRepository.existsByPhoneNumberIgnoreCase(request.getPhoneNumber()));
        JwtTokenResponse token = mapper.readValue(response.andReturn().getResponse().getContentAsString(), JwtTokenResponse.class);

        // Проверяем что токен валидный
        mockMvc.perform(get(HELLO_WORLD)
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk());
    }

}
