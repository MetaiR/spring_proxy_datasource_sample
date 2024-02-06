package com.github.metair.proxydatasource.repository;

import com.github.metair.proxydatasource.entity.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
}
