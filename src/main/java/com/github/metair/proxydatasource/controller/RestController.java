package com.github.metair.proxydatasource.controller;

import com.github.metair.proxydatasource.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
@Slf4j
@RequiredArgsConstructor
public class RestController {
    private final MyService myService;

    @GetMapping("save")
    public void saveSomeEntity() {
        myService.saveSomeEntity();
    }

    @GetMapping("not-save")
    public void saveSomeEntityWithError() {
        myService.saveSomeEntityWithException();
    }

    @GetMapping("none-transactional")
    public void saveSomeEntityNoneTransactional() {
        myService.saveSomeEntityNoneTransactional();
    }

    @GetMapping("none-transactional-with-error")
    public void saveSomeEntityNoneTransactionalWithError() {
        myService.saveSomeEntityNoneTransactionalWithError();
    }

    @GetMapping("save-inner-call-with-error")
    public void saveSomeEntityInnerCallWithError() {
        myService.saveSomeEntityInnerCallWithError();
    }

    @GetMapping("save-inner-call-required-new-with-error")
    public void saveSomeEntityInnerCallTransactionRequiredNewWithError() {
        myService.saveSomeEntityInnerCallTransactionRequiredNewWithError();
    }
}
