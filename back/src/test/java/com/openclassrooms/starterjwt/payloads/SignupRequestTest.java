package com.openclassrooms.starterjwt.payloads;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;

public class SignupRequestTest {

    @Test
    public void testToString() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        String toString = signupRequest.toString();
        assertThat(toString).contains("john.doe@test.com");
        assertThat(toString).contains("John");
        assertThat(toString).contains("Doe");
    }

    @Test
    public void testEqualsAndHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("john.doe@test.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("john.doe@test.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("jane.smith@test.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("password123");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
    
}
