package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.enums.Mode;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.repositories.AnnouncementRepository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private CategoryService categoryService;
    ZonedDateTime now = ZonedDateTime.now();

    public Announcement getAnnouncementById(Integer announcementId) {
        return announcementRepository.findById(announcementId).orElseThrow(
                () -> new ItemNotFoundException("This announcement does not exist!!!"));
    }

    public void deleteAnnouncementById(Integer announcementId) {
        if (announcementRepository.existsById(announcementId)) {
            announcementRepository.deleteById(announcementId);
        } else {
            throw new ItemNotFoundException("This announcement does not exist!!!");
        }
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.saveAndFlush(announcement);
    }

    public Announcement updateAnnouncement(Integer announcementId, Announcement announcement) {
        Announcement ann = announcementRepository.findById(announcementId).orElseThrow(
                () -> new ItemNotFoundException("This announcement does not exist!!!"));
        ann.setAnnouncementTitle(announcement.getAnnouncementTitle());
        ann.setAnnouncementDescription(announcement.getAnnouncementDescription());
        ann.setPublishDate(announcement.getPublishDate());
        ann.setCloseDate(announcement.getCloseDate());
        ann.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        ann.setAnnouncementCategory(announcement.getAnnouncementCategory());
        return announcementRepository.saveAndFlush(ann);
    }

    public List<Announcement> getAnnouncementList(Mode mode, Integer categoryId) {
        List<Announcement> announcementListAdmin;
        List<Announcement> announcementList;
        Comparator<Announcement> byIdDescending = Comparator.comparingInt(Announcement::getId).reversed();
        if (categoryId.equals(0)) {
                announcementList = announcementRepository.findAll().stream()
                        .filter(a -> a.getAnnouncementDisplay() == Display.Y)
                        .collect(Collectors.toList());
        } else {
                announcementList = announcementRepository.findAllByAnnouncementCategory(categoryService.getCategoryById(categoryId)).stream()
                        .filter(a -> a.getAnnouncementDisplay() == Display.Y)
                        .collect(Collectors.toList());
        }
        if (mode == Mode.active) {
            List<Announcement> announcementActiveList = checkActiveDate(announcementList);
            announcementActiveList.sort(byIdDescending);
            return announcementActiveList;
        } else if (mode == Mode.close) {
            List<Announcement> announcementCloseList = checkCloseDate(announcementList);
            announcementCloseList.sort(byIdDescending);
            return announcementCloseList;
        }
        // Admin mode
        if (categoryId.equals(0)) {
            announcementListAdmin = announcementRepository.findAll();
        } else {
            announcementListAdmin = announcementRepository.findAllByAnnouncementCategory(categoryService.getCategoryById(categoryId));
        }
        announcementListAdmin.sort(byIdDescending);
        return announcementListAdmin;
    }

    public Page<Announcement> getAnnouncementPage(int page, int size, Mode mode, Integer categoryId) {
        List<Announcement> announcementList;
        Comparator<Announcement> byIdDescending = Comparator.comparingInt(Announcement::getId).reversed();
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        if (categoryId.equals(0)) {
            announcementList = announcementRepository.findAll().stream()
                    .filter(a -> a.getAnnouncementDisplay() == Display.Y)
                    .collect(Collectors.toList());
        } else {
            announcementList = announcementRepository.findAllByAnnouncementCategory(categoryService.getCategoryById(categoryId)).stream()
                    .filter(a -> a.getAnnouncementDisplay() == Display.Y)
                    .collect(Collectors.toList());
        }
        if (mode == Mode.active) {
            List<Announcement> announcementActiveList = checkActiveDate(announcementList);
            announcementActiveList.sort(byIdDescending);
            int start = (int) pageRequest.getOffset();
            if (start > announcementActiveList.size()) {
                return new PageImpl<>(new ArrayList<>(), pageRequest, announcementActiveList.size());
            }
            int end = Math.min((start + pageRequest.getPageSize()), announcementActiveList.size());
            return new PageImpl<>(announcementActiveList.subList(start, end), pageRequest, announcementActiveList.size());
        } else if (mode == Mode.close) {
            List<Announcement> announcementCloseList = checkCloseDate(announcementList);
            announcementCloseList.sort(byIdDescending);
            int start = (int) pageRequest.getOffset();
            if (start > announcementCloseList.size()) {
                return new PageImpl<>(new ArrayList<>(), pageRequest, announcementCloseList.size());
            }
            int end = Math.min((start + pageRequest.getPageSize()), announcementCloseList.size());
            return new PageImpl<>(announcementCloseList.subList(start, end), pageRequest, announcementCloseList.size());
        }
        // Admin mode
        if (categoryId.equals(0)) {
            return announcementRepository.findAll(pageRequest);
        }
        return announcementRepository.findAllByAnnouncementCategory(pageRequest, categoryService.getCategoryById(categoryId));
    }

    public List<Announcement> checkActiveDate(List<Announcement> announcementList) {
        List<Announcement> announcementActiveList = new ArrayList<>();
        announcementList.forEach(announcement -> {
            if (announcement.getPublishDate() == null && announcement.getCloseDate() == null) {
                announcementActiveList.add(announcement);
            } else if (announcement.getPublishDate() != null && announcement.getCloseDate() == null) {
                if (now.compareTo(announcement.getPublishDate()) > 0 || now.compareTo(announcement.getPublishDate()) == 0) {
                    announcementActiveList.add(announcement);
                }
            } else if (announcement.getPublishDate() != null && announcement.getCloseDate() != null) {
                if ((now.compareTo(announcement.getPublishDate()) > 0 || now.compareTo(announcement.getPublishDate()) == 0) &&
                        now.compareTo(announcement.getCloseDate()) < 0) {
                    announcementActiveList.add(announcement);
                }
            } else if (announcement.getPublishDate() == null && announcement.getCloseDate() != null) {
                if (now.compareTo(announcement.getCloseDate()) < 0) {
                    announcementActiveList.add(announcement);
                }
            }
        });
        return announcementActiveList;
    }

    public List<Announcement> checkCloseDate(List<Announcement> announcementList) {
        List<Announcement> announcementCloseList = new ArrayList<>();
        announcementList.forEach(announcement -> {
            if (announcement.getCloseDate() != null) {
                if ((now.compareTo(announcement.getCloseDate()) > 0 || now.compareTo(announcement.getCloseDate()) == 0)) {
                    announcementCloseList.add(announcement);
                }
            }
        });
        return announcementCloseList;
    }
}
