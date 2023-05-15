package sit.project.projectv1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.enums.Display;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "announcement")
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

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category announcementCategory;

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public ZonedDateTime getCloseDate() {
        return closeDate;
    }
}
