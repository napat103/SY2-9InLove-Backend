package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.repositories.AnnouncementRepository;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getAllAnnouncement() {
        List<Announcement> announcementList = announcementRepository.findAll();
        if (announcementList == null) {
            throw new RuntimeException("Announcements is empty");
        }
        announcementList.sort((a, b) -> b.getId() - a.getId());
        return announcementList;
    }

    public Announcement getAnnouncementById(Integer announcementId) {
        return announcementRepository.findById(announcementId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This announcement does not exist!!!"));
    }

    public void deleteAnnouncementById(Integer announcementId) {
        if (announcementRepository.existsById(announcementId)) {
            announcementRepository.deleteById(announcementId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This announcement does not exist!!!");
        }
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.saveAndFlush(announcement);
    }

    public Announcement updateAnnouncement(Integer announcementId, Announcement announcement) {
        Announcement ann = announcementRepository.findById(announcementId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This announcement does not exist!!!"));
        ann.setAnnouncementTitle(announcement.getAnnouncementTitle());
        ann.setAnnouncementDescription(announcement.getAnnouncementDescription());
        ann.setPublishDate(announcement.getPublishDate());
        ann.setCloseDate(announcement.getCloseDate());
        ann.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        ann.setAnnouncementCategory(announcement.getAnnouncementCategory());
        return announcementRepository.saveAndFlush(ann);
    }
}
