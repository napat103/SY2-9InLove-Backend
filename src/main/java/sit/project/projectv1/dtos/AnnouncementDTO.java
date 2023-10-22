package sit.project.projectv1.dtos;

import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.models.User;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.models.Category;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AnnouncementDTO {

    private Integer id;

    private String announcementTitle;

    private ZonedDateTime publishDate;

    private ZonedDateTime closeDate;

    private Display announcementDisplay;

    private Category announcementCategory;
    public String getAnnouncementCategory() {
        return announcementCategory == null? null : announcementCategory.getCategoryName();
    }

    private User announcementOwner;
    public String getAnnouncementOwner() {
        return announcementOwner == null? null : announcementOwner.getUsername();
    }
}
