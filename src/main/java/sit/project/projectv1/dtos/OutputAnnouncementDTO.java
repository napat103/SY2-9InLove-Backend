package sit.project.projectv1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.models.User;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OutputAnnouncementDTO {

    private Integer id;

    private String announcementTitle;

    private String announcementDescription;

    private ZonedDateTime publishDate;

    private ZonedDateTime closeDate;

    private Display announcementDisplay;

    // @JsonProperty => specify name in JSON
    @JsonProperty("categoryId")
    private Integer announcementCategoryCategoryId;

    @JsonProperty("announcementCategory")
    private String announcementCategoryCategoryName;

    private User announcementOwner;
    public String getAnnouncementOwner() {
        return announcementOwner == null? null : announcementOwner.getUsername();
    }

}
