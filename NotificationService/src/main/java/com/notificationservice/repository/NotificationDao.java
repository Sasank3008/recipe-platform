package com.notificationservice.repository;

import com.notificationservice.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationDao extends JpaRepository<Notifications,Integer>{
    List<Notifications> findByRecipientInOrderByViewedAscCreateAtDesc(List<String> recipients);
    List<Notifications> findByRecipientInOrderByCreateAtDesc(List<String> recipients);
}
