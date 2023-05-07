package sit.project.projectv1.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.*;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.services.AnnouncementService;
import sit.project.projectv1.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

//    @GetMapping
//    public List<Announcement> getAllAnnouncement() {
//        return announcementService.getAllAnnouncement();
//    }

    @GetMapping
    public List<SimpleAnnouncementDTO> getAllAnnouncementDTO() {
        List<Announcement> announcementList = announcementService.getAllAnnouncement();
        List<SimpleAnnouncementDTO> simpleAnnouncementDTOS = announcementList.stream()
                .map(announcement -> modelMapper.map(announcement, SimpleAnnouncementDTO.class))
                .collect(Collectors.toList());
        return simpleAnnouncementDTOS;
    }

    @GetMapping("/{announcementId}")
    public AnnouncementDetailDTO getAnnouncementById(@PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.getAnnouncementById(announcementId), AnnouncementDetailDTO.class);
    }

    @GetMapping("update/{announcementId}")
    public OutputForUpdateAnnouncementDTO getAnnouncementForUpdate(@PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.getAnnouncementById(announcementId), OutputForUpdateAnnouncementDTO.class);
    }

    @DeleteMapping("/{announcementId}")
    public void deleteAnnouncementById(@PathVariable Integer announcementId) {
        announcementService.deleteAnnouncementById(announcementId);
    }

    @PostMapping
    public OutputAnnouncementDTO createAnnouncement(@RequestBody InputAnnouncementDTO announcementDTO) {
        Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
        announcement.setId(null);
        announcement.setAnnouncementCategory(categoryService.getCategoryById(announcementDTO.getCategoryId()));
        announcementService.createAnnouncement(announcement);
        return modelMapper.map(announcement, OutputAnnouncementDTO.class);
    }

    @PutMapping("/{announcementId}")
    public OutputAnnouncementDTO updateAnnouncement(@PathVariable Integer announcementId, @RequestBody InputAnnouncementDTO announcementDTO) {
        Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
        announcement.setId(announcementId);
        announcement.setAnnouncementCategory(categoryService.getCategoryById(announcementDTO.getCategoryId()));
        announcementService.updateAnnouncement(announcementId, announcement);
        return modelMapper.map(announcement, OutputAnnouncementDTO.class);
    }
}
