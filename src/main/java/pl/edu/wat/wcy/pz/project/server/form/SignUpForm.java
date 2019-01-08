package pl.edu.wat.wcy.pz.project.server.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignUpForm {
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    @NotBlank
    @Email
    @Size(max = 30)
    private String email;
    private Set<String> role;
    @NotBlank
    @Size(min = 6, max = 16)
    private String password;

}
