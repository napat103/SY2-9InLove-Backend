package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.project.projectv1.models.Category;
import sit.project.projectv1.models.Subscription;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer>, EntityRepository<Subscription> {

//    @Query("SELECT s FROM Subscription s WHERE s.subscriberEmail = :subscriberEmail AND s.category = :category")
    Subscription findBySubscriberEmailAndCategory(String subscriberEmail, Category category);

    boolean existsBySubscriberEmailAndCategory(String subscriberEmail, Category category);

    @Query("SELECT s.subscriberEmail FROM Subscription s WHERE s.category = :category")
    List<String> findEmailByCategory(Category category);
}

