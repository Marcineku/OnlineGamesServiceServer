package pl.edu.wat.wcy.pz.project.server.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
    private String type;
    private String title;
    private String text;
    private String language;
}
