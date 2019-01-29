package pl.edu.wat.wcy.pz.project.server.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDTO {
    private String emailAddress;
    private String emailSubject;
    private String emailText;
    private String username;
    private String url;
    private EmailType emailType;

    public enum EmailType {
        VERIFICATION_EMAIL,
        PASSWORD_RESET,
        OTHER
    }
}
