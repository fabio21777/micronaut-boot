package com.fsm.security;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.http.HttpStatus;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.endpoints.TokenRefreshRequest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class RequestSpecificationJwtTest {

    @Inject
    RequestSpecification spec;

    @Test
    void verifyJwtAuthenticationWorks() throws ParseException {
        // Create credentials
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "admin");

        // Login request
        Response loginResponse = spec
                .contentType("application/json")
                .body(creds)
                .when()
                .post("/login");

        // Status check
        assertEquals(HttpStatus.OK.getCode(), loginResponse.getStatusCode());


        String objetoCompleto = loginResponse.asString();
        //
        // Extract token
        String accessToken = loginResponse.jsonPath().getString("access_token");
        String refreshToken = loginResponse.jsonPath().getString("refresh_token");


        // Verify token
        assertNotNull(accessToken);
        assertInstanceOf(SignedJWT.class, JWTParser.parse(accessToken));

        // Verify refresh token
        assertNotNull(refreshToken);
        assertInstanceOf(SignedJWT.class, JWTParser.parse(refreshToken));
    }

}