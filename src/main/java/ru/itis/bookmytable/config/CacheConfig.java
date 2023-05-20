package ru.itis.bookmytable.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@EnableCaching
public class CacheConfig {

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    @CacheEvict(cacheNames = "USER_BY_USERNAME")
    public void cacheEvict(){}

}
