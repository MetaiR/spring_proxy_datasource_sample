package com.github.metair.proxydatasource.service;

import com.github.metair.proxydatasource.entity.MyEntity;
import com.github.metair.proxydatasource.repository.MyEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyService2 {
    private final MyEntityRepository myEntityRepository;

    @Transactional
    public void innerTransactional() {
        var entity = MyEntity.builder()
                .name("A")
                .age(20)
                .build();

        myEntityRepository.save(entity);

        entity = MyEntity.builder()
                .name("S")
                .age(30)
                .build();

        myEntityRepository.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerTransactionalRequiredNew() {
        var entity = MyEntity.builder()
                .name("A")
                .age(20)
                .build();

        myEntityRepository.save(entity);

        entity = MyEntity.builder()
                .name("S")
                .age(30)
                .build();

        myEntityRepository.save(entity);
    }
}
