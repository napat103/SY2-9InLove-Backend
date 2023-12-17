package sit.project.projectv1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.*;
import sit.project.projectv1.enums.Mode;
import sit.project.projectv1.enums.Role;
import sit.project.projectv1.models.Announcement;
import sit.project.projectv1.models.User;
import sit.project.projectv1.services.AnnouncementService;
import sit.project.projectv1.services.CategoryService;
import sit.project.projectv1.services.SubscriptionService;
import sit.project.projectv1.services.UserService;
import sit.project.projectv1.utils.ListMapper;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;


    @GetMapping
    public List<AnnouncementDTO> getAnnouncementList(@RequestParam(defaultValue = "admin") Mode mode,
                                                     @RequestParam(defaultValue = "0") int category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        List<Announcement> announcementList = announcementService.getAnnouncementList(mode, category, user);
        List<AnnouncementDTO> announcementDTOList = announcementList.stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementDTO.class))
                .collect(Collectors.toList());
        return announcementDTOList;
    }

    @GetMapping("/pages")
    public PageDTO<AnnouncementDTO> getAnnouncementPageDTO(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size,
                                                           @RequestParam(defaultValue = "admin") Mode mode,
                                                           @RequestParam(defaultValue = "0") int category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        Page<Announcement> announcementPage = announcementService.getAnnouncementPage(page, size, mode, category, user);
        return listMapper.toPageDTO(announcementPage, AnnouncementDTO.class, modelMapper);
    }

    @GetMapping("/{announcementId}")
    public AnnouncementDetailDTO getAnnouncementById(@PathVariable Integer announcementId) {
        Announcement storedAnnouncement = announcementService.getAnnouncementById(announcementId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        // Guest, admin, owner announcement can access
        if (user == null || user.getRole() == Role.admin || user.getUsername().equals(storedAnnouncement.getAnnouncementOwner().getUsername())) {
            return modelMapper.map(announcementService.getAnnouncementById(announcementId), AnnouncementDetailDTO.class);
        }

        throw new AccessDeniedException("Access denied!!!");
    }

    @GetMapping("/update/{announcementId}")
    public OutputUpdateAnnouncementDTO getAnnouncementForUpdate(@PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.getAnnouncementById(announcementId), OutputUpdateAnnouncementDTO.class);
    }

    @PostMapping
    public OutputAnnouncementDTO createAnnouncement(@Valid @RequestBody InputAnnouncementDTO announcementDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
        announcement.setId(null);
        announcement.setAnnouncementCategory(categoryService.getCategoryById(announcementDTO.getCategoryId()));
        announcement.setAnnouncementOwner(user);

        Announcement newAnnouncement = announcementService.createAnnouncement(announcement); // create and declare newAnnouncement
        announcementService.checkNewAnnouncement(newAnnouncement);

        return modelMapper.map(announcement, OutputAnnouncementDTO.class);
    }

    @PutMapping("/{announcementId}")
    public OutputAnnouncementDTO updateAnnouncement(@PathVariable Integer announcementId, @Valid @RequestBody InputAnnouncementDTO announcementDTO) {
        Announcement storedAnnouncement = announcementService.getAnnouncementById(announcementId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        if (user.getRole() == Role.admin || user.getUsername().equals(storedAnnouncement.getAnnouncementOwner().getUsername())) {
            Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
            announcement.setId(announcementId);
            announcement.setAnnouncementCategory(categoryService.getCategoryById(announcementDTO.getCategoryId()));
            announcement.setAnnouncementOwner(storedAnnouncement.getAnnouncementOwner());

            Announcement newAnnouncement = announcementService.updateAnnouncement(announcementId, announcement); // update and declare newAnnouncement
            announcementService.checkNewAnnouncement(newAnnouncement);

            return modelMapper.map(announcement, OutputAnnouncementDTO.class);
        }
        throw new AccessDeniedException("Access denied!!!");
    }

    @DeleteMapping("/{announcementId}")
    public void deleteAnnouncementById(@PathVariable Integer announcementId) {
        Announcement storedAnnouncement = announcementService.getAnnouncementById(announcementId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserFromToken(authentication);

        if (user.getRole() == Role.admin || user.getUsername().equals(storedAnnouncement.getAnnouncementOwner().getUsername())) {
            announcementService.deleteAnnouncementById(announcementId);
        } else {
            throw new AccessDeniedException("Access denied!!!");
        }
    }

}
