package org.app.gtsconnected3.repository;

import org.app.gtsconnected3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{
    public Role findByName(String name);
}
