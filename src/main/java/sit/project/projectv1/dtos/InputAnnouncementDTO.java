package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InputAnnouncementDTO {
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private Display announcementDisplay;
    private Integer categoryId;
}

