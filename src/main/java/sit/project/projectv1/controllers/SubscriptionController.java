package sit.project.projectv1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.project.projectv1.dtos.SubscriptionDTO;
import sit.project.projectv1.exceptions.ResponseStatusValidationException;
import sit.project.projectv1.models.Subscription;
import sit.project.projectv1.services.CategoryService;
import sit.project.projectv1.services.SubscriptionService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<Subscription> getAllSubscription() {
        return subscriptionService.getAllSubscription();
    }

    @GetMapping("/{subscriptionId}")
    public Subscription getSubscriptionById(@PathVariable Integer subscriptionId) {
        return subscriptionService.getSubscriptionById(subscriptionId);
    }

    @PostMapping
    public Subscription createSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = modelMapper.map(subscriptionDTO, Subscription.class);
        subscription.setId(null); // if not assign. ID will not generate
        subscription.setCategory(categoryService.getCategoryById(subscriptionDTO.getCategoryId()));

        if (!subscriptionService.checkExistingEmailAndCategory(subscription)) {
            return subscriptionService.createSubscription(subscription);
        }
        throw new ResponseStatusValidationException(HttpStatus.BAD_REQUEST, "Email and Category", "Cannot add same");
    }

    @PutMapping("/{subscriptionId}")
    public Subscription updateSubscription(@PathVariable Integer subscriptionId, @Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = modelMapper.map(subscriptionDTO, Subscription.class);
        subscription.setCategory(categoryService.getCategoryById(subscriptionDTO.getCategoryId()));

        if (!subscriptionService.checkExistingEmailAndCategory(subscription)) {
            return subscriptionService.updateSubscription(subscriptionId, subscription);
        }
        throw new ResponseStatusValidationException(HttpStatus.BAD_REQUEST, "Email and Category", "Cannot update same");
    }

    @DeleteMapping("/{subscriptionId}")
    public void deleteSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.deleteSubscription(subscriptionId);
    }
}
