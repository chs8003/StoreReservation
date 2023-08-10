package com.hyeonsik.boot.mapper;

public interface WaitlistRepositoryCustom {
    void updateQueueNumbers(Long queueNumber, Integer adminNo);
    
}