package sit.project.projectv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.project.projectv1.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, EntityRepository<User> {
    @Query("SELECT u FROM User u order by u.role asc, u.username asc")
    List<User> findAllUser();

//    @Query("SELECT u. FROM User u WHERE u.fieldName = :value")
//    List<User> findAllByFieldName(@Param("fieldName") String fieldName, @Param("value") String value);

//    @Query("SELECT u FROM User u")
//    List<User> findAllByFieldName(@Param("fieldName") String fieldName);

    @Query("SELECT u.username FROM User u")
    List<String> findUserName();

    @Query("SELECT u.name FROM User u")
    List<String> findName();

    @Query("SELECt u.email FROM User u")
    List<String> findEmail();

    User findByUsername(String username);

    User findByName(String name);

    User findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);
}
