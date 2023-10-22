package sit.project.projectv1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.models.Category;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OutputUpdateAnnouncementDTO {

    private String announcementTitle;

    private String announcementDescription;

    private ZonedDateTime publishDate;

    private ZonedDateTime closeDate;

    private Display announcementDisplay;

    @JsonIgnore
    private Category announcementCategory;
    public Integer getCategoryID() {
        return announcementCategory == null? null : announcementCategory.getCategoryId();
    }
}
