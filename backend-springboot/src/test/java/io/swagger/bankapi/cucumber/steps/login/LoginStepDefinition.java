package io.swagger.bankapi.cucumber.steps.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.bankapi.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
public class LoginStepDefinition extends BaseStepDefinitions implements En {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<String> response;
    private LoginDTO dto;

    public LoginStepDefinition() {
        When("^I call the login endpoint$", () -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(
                    dto),
                    httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/login",
                    request, String.class);
        });

//        Then("^I receive a status of (\\d+)$", (Integer status) -> {
//            Assertions.assertEquals(status, response.getStatusCodeValue());
//        });
//
//        And("^I get a JWT-token$", () -> {
//            JSONObject jsonObject = new JSONObject(response.getBody());
//            String token = jsonObject.getString("token");
//            Assertions.assertTrue(token.startsWith("ey"));
//        });
//        Given("^I have a valid user object$", () -> {
//            dto = new LoginDTO();
//        });
//        Given("^I have an invalid user object$", () -> {
//            dto = new LoginDTO();
//        });
    }


}
