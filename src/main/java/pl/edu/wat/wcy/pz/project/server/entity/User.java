package pl.edu.wat.wcy.pz.project.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;
    @Column(name = "LAST_LOGON_DATE")
    private Date lastLogonDate;
    @Column(name = "IS_EMAIL_VERIFIED")
    private String isEmailVerified;
}
