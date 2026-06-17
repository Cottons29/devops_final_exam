package live.cottons.lypheng.repository;

import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUuid(String uuid);

    Optional<Profile> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Profile> findByType(ProfileType type);

    List<Profile> findByFullNameContainingIgnoreCase(String query);

    List<Profile> findByDepartment(String department);

    long countByType(ProfileType type);

    long countByDepartment(String department);
}
