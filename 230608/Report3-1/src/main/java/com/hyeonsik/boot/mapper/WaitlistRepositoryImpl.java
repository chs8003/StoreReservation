package com.hyeonsik.boot.mapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class WaitlistRepositoryImpl implements WaitlistRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void updateQueueNumbers(Long queueNumber, Integer adminNo) {
        Query query = entityManager.createQuery(
                "UPDATE Waitlist w SET w.queueNumber = w.queueNumber - 1 WHERE w.queueNumber > :queueNumber AND w.restaurant.adminNo = :adminNo")
                .setParameter("queueNumber", queueNumber)
                .setParameter("adminNo", adminNo);
        query.executeUpdate();
    }
}