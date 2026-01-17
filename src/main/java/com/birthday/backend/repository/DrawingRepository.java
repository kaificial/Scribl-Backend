package com.birthday.backend.repository;

import com.birthday.backend.model.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {
}
