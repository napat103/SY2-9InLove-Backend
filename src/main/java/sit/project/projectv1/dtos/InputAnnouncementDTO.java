package sit.project.projectv1.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.Enum;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InputAnnouncementDTO {
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private Enum announcementDisplay;
    private Integer categoryId;
}

