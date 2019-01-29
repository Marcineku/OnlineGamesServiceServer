package pl.edu.wat.wcy.pz.project.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EMAIL_VERIFICATION_TOKEN")
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID")
    private Long tokenId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "ISSUED_DATE")
    private Date issuedDate;

    @Column(name = "EXPIRATION_DATE")
    private Date expiraionDate;

    @Column(name = "EXPIRED")
    private String expired;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public EmailVerificationToken(User user) {
        this.token = UUID.randomUUID().toString();
        Calendar c = Calendar.getInstance();
        this.issuedDate = c.getTime();
        c.add(Calendar.DATE, 1);
        this.expiraionDate = c.getTime();
        this.expired = "N";
        this.user = user;
    }
}
