package sit.project.projectv1.models;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {

    private String recipient;

    private String msgBody;

    private String subject;

    private String attachment;
}
