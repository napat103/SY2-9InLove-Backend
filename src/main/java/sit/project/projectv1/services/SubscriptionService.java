package sit.project.projectv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.project.projectv1.enums.Display;
import sit.project.projectv1.exceptions.ItemNotFoundException;
import sit.project.projectv1.models.Announcement;
import sit.project.projectv1.models.Category;
import sit.project.projectv1.models.EmailDetails;
import sit.project.projectv1.models.Subscription;
import sit.project.projectv1.repositories.SubscriptionRepository;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAllSubscription() {
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Integer subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElseThrow(
                () -> new ItemNotFoundException("Not found this subscription"));
    }

    public List<String> getEmailByCategory(Category category) {
        return subscriptionRepository.findEmailByCategory(category);
    }

    public Subscription createSubscription(Subscription subscription) {
        Subscription saveSubscription = subscriptionRepository.saveAndFlush(subscription);
        subscriptionRepository.refresh(saveSubscription);
        return saveSubscription;
    }

    public Subscription updateSubscription(Integer subscriptionId, Subscription subscription) {
        Subscription storedSubscription = subscriptionRepository.findById(subscriptionId).orElseThrow(
                () -> new ItemNotFoundException("Not found this subscription"));
        storedSubscription.setSubscriberEmail(subscription.getSubscriberEmail());
        storedSubscription.setCategory(subscription.getCategory());
        Subscription saveSubscription = subscriptionRepository.saveAndFlush(storedSubscription);
        subscriptionRepository.refresh(saveSubscription);
        return saveSubscription;
    }

    public void deleteSubscription(Integer subscriptionId) {
        if (subscriptionRepository.existsById(subscriptionId)) {
            subscriptionRepository.deleteById(subscriptionId);
        } else {
            throw new ItemNotFoundException("Not found this subscription");
        }
    }

    public boolean existEmailAndCategory(Subscription subscription) {
        return subscriptionRepository.existsBySubscriberEmailAndCategory(subscription.getSubscriberEmail(), subscription.getCategory());
    }
}
