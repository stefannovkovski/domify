package com.example.domify.jobs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduledJobs {

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 9 1-5 * *")
    @Transactional
    public void callReminderProcedure() {
        entityManager
                .createNativeQuery("CALL domify.send_monthly_payment_reminders()")
                .executeUpdate();
    }

    @Scheduled(cron = "0 0 10 * * 1")
    @Transactional
    public void callUnresolvedServiceReminderProcedure() {
        entityManager
                .createNativeQuery("CALL domify.send_unresolved_service_request_reminders()")
                .executeUpdate();
    }

}
