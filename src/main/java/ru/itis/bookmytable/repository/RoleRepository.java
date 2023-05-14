package ru.itis.bookmytable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.bookmytable.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getReferenceByName(String name);

}
