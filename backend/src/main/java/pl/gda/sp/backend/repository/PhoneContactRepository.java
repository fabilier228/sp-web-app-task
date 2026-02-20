package pl.gda.sp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gda.sp.backend.domain.PhoneContact;

public interface PhoneContactRepository extends JpaRepository<PhoneContact, Long> {
    PhoneContact findByName(String name);
    PhoneContact findByPhoneNumber(String phoneNumber);

    PhoneContact findByNameOrPhoneNumber(String name, String phoneNumber);
    void deleteByName(String name);
    void deleteByPhoneNumber(String phoneNumber);
    boolean existsByName(String name);
    boolean existsByPhoneNumber(String phoneNumber);
}
