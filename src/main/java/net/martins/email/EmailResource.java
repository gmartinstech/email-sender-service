package net.martins.email;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.martins.email.model.EmailRequest;
import net.martins.email.service.EmailService;
import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;

@Path("/email")
public class EmailResource {
    
    private static final Logger LOG = Logger.getLogger(EmailResource.class);
    
    @Inject
    EmailService emailService;
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from email-sender-service";
    }
    
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(@Valid EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest);
            return Response.ok().entity("{\"message\":\"Email sent successfully\"}").build();
        } catch (Exception e) {
            LOG.errorf("Error sending email: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to send email: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    @Path("/send-async")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> sendEmailAsync(@Valid EmailRequest emailRequest) {
        return emailService.sendEmailAsync(emailRequest)
                .onItem().transform(ignore -> Response.ok().entity("{\"message\":\"Email queued for sending\"}").build())
                .onFailure().recoverWithItem(e -> {
                    LOG.errorf("Error queuing email: %s", e.getMessage());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\":\"Failed to queue email: " + e.getMessage() + "\"}")
                            .build();
                });
    }
}