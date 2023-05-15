package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.entities.Category;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserAnnouncementDTO {
    private Integer id;
    private String announcementTitle;
    private ZonedDateTime closeDate;

    private Category announcementCategory;
    public String getAnnouncementCategory() {
        return announcementCategory == null? null : announcementCategory.getCategoryName();
    }
}
