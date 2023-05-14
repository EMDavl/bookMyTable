package ru.itis.bookmytable.integrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class JwtTokenFilterTest extends BaseTest{

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String URL = "/hello-world";

    @Test
    public void validMessageMustBeReturned_whenNoTokenPassed() throws Exception {
        var response = mockMvc.perform(get(URL));

        response
                .andExpectAll(
                        status().is(401),
                        jsonPath("$.status").value(401),
                        jsonPath("$.content").value("No authorization header present")
                );
    }

    @Test
    public void validMessageMustBeReturned_whenAuthHeaderWithoutBearerPrefix() throws Exception {
        var response = mockMvc.perform(get(URL)
                .header(AUTHORIZATION_HEADER, "token without prefix"));

        response
                .andExpectAll(
                        status().is(401),
                        jsonPath("$.status").value(401),
                        jsonPath("$.content").value("Authorization token must have \"Bearer\" prefix")
                );
    }

    @Test
    public void validMessageMustBeReturned_whenMalformedTokenPassed() throws Exception {
        var response = mockMvc.perform(get(URL)
                .header(AUTHORIZATION_HEADER, "Bearer fawdawdawd"));

        response
                .andExpectAll(
                        status().is(401),
                        jsonPath("$.status").value(401),
                        jsonPath("$.content").value("Malformed token passed")
                );
    }

}
