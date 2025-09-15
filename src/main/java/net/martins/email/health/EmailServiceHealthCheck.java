package net.martins.email.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import jakarta.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class EmailServiceHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        // Simple health check - in a real application, you might check SMTP connectivity
        return HealthCheckResponse.named("Email Service")
                .up()
                .withData("status", "Email service is running")
                .build();
    }
}