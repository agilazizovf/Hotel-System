package com.project.hotel.repository;

import com.project.hotel.entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Transactional
    @Query(value = "INSERT INTO user_roles (user_id, role_id) select ?1,id from roles where customer=1", nativeQuery = true)
    @Modifying
    void addCustomerRoles(Long userId);

    @Query(value = "INSERT INTO user_roles (user_id, role_id) select ?1,id from roles where employee=1", nativeQuery = true)
    @Modifying
    void addEmployeeRoles(Long userId);

    @Transactional
    @Query(value = "INSERT INTO user_roles (user_id, role_id) select ?1,id from roles where admin=1", nativeQuery = true)
    @Modifying
    void addAdminRoles(Long userId);
}
