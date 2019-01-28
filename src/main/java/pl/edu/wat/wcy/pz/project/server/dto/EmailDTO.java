package pl.edu.wat.wcy.pz.project.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String emailAddress;
    private String emailSubject;
    private String emailText;
    private EmailType emailType;

    public enum EmailType {
        VERIFICATION_EMAIL,
        OTHER
    }
}
