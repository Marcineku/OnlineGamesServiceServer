package pl.edu.wat.wcy.pz.project.server.entity;

import lombok.*;

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
public class News {

    @Id
    @GeneratedValue()
    @Column(name = "NEWS_ID")
    private Long newsId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "PUBLICATION_DATE")
    private Date publicationDate;
    @Column(name = "LANGUAGE")
    private String language;
}
