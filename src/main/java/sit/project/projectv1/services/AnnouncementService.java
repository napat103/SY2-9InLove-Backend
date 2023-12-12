package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.enums.Mode;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.models.Announcement;
import sit.project.projectv1.models.User;
import sit.project.projectv1.repositories.AnnouncementRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private CategoryService categoryService;

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

    public List<Announcement> getAnnouncementList(Mode mode, Integer categoryId, User user) {
        List<Announcement> announcementList;
        String role;

        if (user == null) {
            role = "guest";
        } else {
            role = user.getRole().toString();
        }

        if (role.equals("admin")) { // FindAll, Filter category, mode
            announcementList = announcementRepository.findAll();
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return announcementList;

        } else if (role.equals("announcer")) { // FindByOwner, Filter category, mode
            announcementList = announcementRepository.findAllByAnnouncementOwner(user);
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return announcementList;

        } else { // Guest role => FindByDisplay, Filter category, mode
            announcementList = announcementRepository.findAllByAnnouncementDisplay(Display.Y);
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return announcementList;
        }
    }

    public Page<Announcement> getAnnouncementPage(int page, int size, Mode mode, Integer categoryId, User user) {
        List<Announcement> announcementList;
        String role;
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (user == null) {
            role = "guest";
        } else {
            role = user.getRole().toString();
        }

        if (role.equals("admin")) { // FindAll, Filter category, mode
            announcementList = announcementRepository.findAll();
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return changeListToPage(announcementList, pageRequest);

        } else if (role.equals("announcer")) { // FindByOwner, Filter category, mode
            announcementList = announcementRepository.findAllByAnnouncementOwner(user);
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return changeListToPage(announcementList, pageRequest);

        } else { // Guest role, FindByDisplay, Filter category, mode
            announcementList = announcementRepository.findAllByAnnouncementDisplay(Display.Y);
            announcementList = filterCategoryAndMode(announcementList, categoryId, mode);
            return changeListToPage(announcementList, pageRequest);
        }
    }

    public List<Announcement> filterCategoryAndMode(List<Announcement> announcementList, Integer categoryId, Mode mode) {
        Comparator<Announcement> byIdDescending = Comparator.comparingInt(Announcement::getId).reversed();

        if (!categoryId.equals(0)) {
            announcementList = announcementList.stream()
                    .filter(announcement -> announcement.getAnnouncementCategory() == categoryService.getCategoryById(categoryId))
                    .collect(Collectors.toList());
        }

        if (mode == Mode.active) {
            announcementList = getActiveDate(announcementList);
            announcementList.sort(byIdDescending);
            return announcementList;
        } else if (mode == Mode.close) {
            announcementList = getCloseDate(announcementList);
            announcementList.sort(byIdDescending);
            return announcementList;
        } else { // Mode = admin
            announcementList.sort(byIdDescending);
            return announcementList;
        }
    }

    public Page<Announcement> changeListToPage(List<Announcement> announcementList, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        if (start > announcementList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, announcementList.size());
        }
        int end = Math.min((start + pageRequest.getPageSize()), announcementList.size());
        return new PageImpl<>(announcementList.subList(start, end), pageRequest, announcementList.size());
    }

    public List<Announcement> getActiveDate(List<Announcement> announcementList) {
        List<Announcement> announcementActiveList = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();

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

    public List<Announcement> getCloseDate(List<Announcement> announcementList) {
        List<Announcement> announcementCloseList = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();

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
