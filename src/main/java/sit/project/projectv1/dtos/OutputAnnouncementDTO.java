package sit.project.projectv1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.Enum;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OutputAnnouncementDTO {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private Enum announcementDisplay;

    @JsonProperty("categoryId")
    private Integer announcementCategoryCategoryId;

    @JsonProperty("announcementCategory")
    private String announcementCategoryCategoryName;
}
