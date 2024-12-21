package com.ersandev.universalpetcare.repository;

import com.ersandev.universalpetcare.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
