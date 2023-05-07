package sit.project.projectv1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sit.project.projectv1.Enum;

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
    private Enum announcementDisplay;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category announcementCategory;
}
