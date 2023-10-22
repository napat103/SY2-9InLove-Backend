package sit.project.projectv1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.project.projectv1.dtos.AnnouncementDTO;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.models.Announcement;
import sit.project.projectv1.models.Category;
import sit.project.projectv1.models.User;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    Page<Announcement> findAllByAnnouncementCategory(Pageable pageable, Category category);

    List<Announcement> findAllByAnnouncementCategory(Category category);

    List<Announcement> findAllByAnnouncementOwner(User user);

    Announcement findAnnouncementByAnnouncementOwner(User user);

    List<Announcement> findAllByAnnouncementDisplay(Display display);
}
