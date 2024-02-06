package com.github.metair.proxydatasource.service;

import com.github.metair.proxydatasource.entity.MyEntity;
import com.github.metair.proxydatasource.repository.MyEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyService {
    private final MyEntityRepository myEntityRepository;
    private final MyService2 myService2;

    @Transactional
    public void saveSomeEntity() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);
    }

    @Transactional
    public void saveSomeEntityWithException() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);

        throw new RuntimeException("something still not implemented");
    }

    public void saveSomeEntityNoneTransactional() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        myService2.innerTransactional();

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);
    }

    public void saveSomeEntityNoneTransactionalWithError() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        myService2.innerTransactional();

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);

        throw new RuntimeException("not implemented details");
    }

    @Transactional
    public void saveSomeEntityInnerCallWithError() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        myService2.innerTransactional();

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);

        throw new RuntimeException("not implemented details");
    }

    @Transactional
    public void saveSomeEntityInnerCallTransactionRequiredNewWithError() {
        var entity = MyEntity.builder()
                .name("Ali")
                .age(10)
                .build();

        myEntityRepository.save(entity);

        myService2.innerTransactionalRequiredNew();

        entity = MyEntity.builder()
                .name("M")
                .age(7)
                .build();

        myEntityRepository.save(entity);

        throw new RuntimeException("not implemented details");
    }
}
