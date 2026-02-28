package com.valora.gestion.repository;

import com.valora.gestion.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientRoleOrderByDateDesc(String recipientRole);

    List<Notification> findAllByOrderByDateDesc();
}
