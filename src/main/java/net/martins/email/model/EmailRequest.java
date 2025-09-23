package net.martins.email.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collections;
import java.util.List;

public record EmailRequest(
    @NotEmpty(message = "At least one recipient is required")
    List<@Email String> to,
    List<@Email String> cc,
    List<@Email String> bcc,
    @NotBlank(message = "Subject is required")
    String subject,
    @NotBlank(message = "Body is required")
    String body,
    boolean html
) {
    /**
     * Compact constructor to ensure cc and bcc are never null.
     */
    public EmailRequest {
        cc = (cc == null) ? Collections.emptyList() : cc;
        bcc = (bcc == null) ? Collections.emptyList() : bcc;
    }
}