package sit.project.projectv1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "announcements")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcementId")
    private Integer id;

    @Column(name = "announcementTitle")
    private String announcementTitle;

    @Column(name = "announcementDescription")
    private String announcementDescription;

    @Column(name = "publishDate")
    private ZonedDateTime publishDate;

    @Column(name = "closeDate")
    private ZonedDateTime closeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "announcementDisplay")
    private Display announcementDisplay;

    @Column(name = "viewCount")
    private Integer viewCount;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category announcementCategory;

    @ManyToOne
    @JoinColumn(name = "userOwner", referencedColumnName = "userId")
    private User announcementOwner;
}
