package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.project.projectv1.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
