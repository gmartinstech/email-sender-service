/*
package net.martins.email;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import net.martins.email.model.EmailRequest;
import net.martins.email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EmailResourceTest {

    @InjectMock
    EmailService emailService;

    private EmailRequest validEmailRequest;

    @BeforeEach
    void setUp() {
        validEmailRequest = new EmailRequest(
                List.of("test@example.com"),
                null,
                null,
                "Test Subject",
                "Test Body",
                false);
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/email")
                .then()
                .statusCode(200)
                .body(is("Hello from email-sender-service"));
    }

    @Test
    public void testSendEmail_Success() {
        ArgumentCaptor<EmailRequest> captor = ArgumentCaptor.forClass(EmailRequest.class);
        doNothing().when(emailService).sendEmail(captor.capture());

        given()
                .contentType(ContentType.JSON)
                .body(validEmailRequest)
                .when().post("/email/send")
                .then()
                .statusCode(200)
                .body("message", is("Email sent successfully"));

        verify(emailService).sendEmail(any(EmailRequest.class));
        assertEquals("test@example.com", captor.getValue().to().get(0));
    }

    @Test
    public void testSendEmail_ServiceThrowsException() {
        doThrow(new RuntimeException("SMTP server down")).when(emailService).sendEmail(validEmailRequest);

        given()
                .contentType(ContentType.JSON)
                .body(validEmailRequest)
                .when().post("/email/send")
                .then()
                .statusCode(500)
                .body("error", is("Failed to send email: SMTP server down"));
    }

    @Test
    public void testSendEmail_InvalidRequest() {
        EmailRequest invalidRequest = new EmailRequest(
                null, // 'to' is missing
                null,
                null,
                "", // 'subject' is blank
                "", // 'body' is blank
                false);

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when().post("/email/send")
                .then()
                .statusCode(400); // Bad Request due to validation
    }

    @Test
    public void testSendEmailAsync_Success() {
        when(emailService.sendEmailAsync(validEmailRequest)).thenReturn(Uni.createFrom().voidItem());

        given()
                .contentType(ContentType.JSON)
                .body(validEmailRequest)
                .when().post("/email/send-async")
                .then()
                .statusCode(202)
                .body("message", is("Email queued for sending"));

        verify(emailService).sendEmailAsync(validEmailRequest);
    }

    @Test
    public void testSendEmailAsync_Failure() {
        when(emailService.sendEmailAsync(validEmailRequest))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Queueing failed")));

        given()
                .contentType(ContentType.JSON)
                .body(validEmailRequest)
                .when().post("/email/send-async")
                .then()
                .statusCode(500)
                .body("error", is("Failed to queue email: Queueing failed"));
    }
}
*/