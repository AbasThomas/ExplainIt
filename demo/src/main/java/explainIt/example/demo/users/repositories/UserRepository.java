package explainIt.example.demo.users.repositories;

import explainIt.example.demo.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by email (for login)
    Optional<User> findByEmail(String email);

    // Find by username (for login or profile lookup)
    Optional<User> findByUsername(String username);

    // Check if email exists (for signup validation)
    boolean existsByEmail(String email);

    // Check if username exists (for signup validation)
    boolean existsByUsername(String username);

    // Find users by plan (for admin analytics)
    List<User> findByPlanId(Long planId);

    // Find users created after a date (for growth metrics)
    List<User> findByCreatedAtAfter(LocalDateTime date);

    // Find by email or username (useful for flexible login)
    Optional<User> findByEmailOrUsername(String email, String username);

    // Custom JPQL query: Find users with a specific plan name
    @Query("SELECT u FROM User u WHERE u.plan.name = :planName")
    List<User> findUsersByPlanName(@Param("planName") String planName);

    // Custom native SQL query: Count users per plan
    @Query(value = "SELECT COUNT(*) FROM users WHERE plan_id = :planId",
            nativeQuery = true)
    long countUsersByPlanId(@Param("planId") Long planId);
}
