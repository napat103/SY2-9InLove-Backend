package sit.project.projectv1.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.exceptions.EnumSizeLimit;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InputAnnouncementDTO {
    @NotBlank
    @Size(min = 1, max = 200)
    private String announcementTitle;

    @NotBlank
    @Size(min = 1, max = 10000)
    private String announcementDescription;

    private ZonedDateTime publishDate;

    private ZonedDateTime closeDate;

    @EnumSizeLimit(targetClassType = Display.class, message = "must be either 'Y' or 'N'")
    private String announcementDisplay;

    @NotNull
    private Integer categoryId;

    public Display getAnnouncementDisplay() {
        return  announcementDisplay == null ? Display.N : Display.valueOf(announcementDisplay);
    }
}

