package com.notificationservice.controller;

import com.notificationservice.constants.NotificationConstants;
import com.notificationservice.dto.NotifyUserDTO;
import com.notificationservice.dto.NotifyDTO;
import com.notificationservice.dto.ResponseDTO;
import com.notificationservice.dto.SimpleResponseDTO;
import com.notificationservice.entity.Notifications;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("notify")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @PostMapping("notifications/notifyAll")
    public ResponseEntity<?> notifyEveryone(@Valid @RequestBody NotifyDTO notifydto) {
        notificationService.notifyAll(notifydto.getMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("notifications/notifyAdmin")
    public ResponseEntity<?> notifyAdmin(@Valid @RequestBody NotifyUserDTO notifyUserdto) {
        return buildOkResponse(() -> notificationService.notifyAdmin(notifyUserdto.getUserId(), notifyUserdto.getMessage()));
    }

    @PostMapping("notifications/notifyUser")
    public ResponseEntity<?> notifyUser(@Valid @RequestBody NotifyUserDTO notifyUserdto) {
        return buildOkResponse(() -> notificationService.notifyUser(notifyUserdto.getUserId(), notifyUserdto.getMessage()));
    }

    @GetMapping("notifications")
    public ResponseEntity<?> getAllNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, notificationService.getAllNotifications());
    }

    @PutMapping("notifications")
    public ResponseEntity<?> updateViewStatus(@RequestParam int id) {
        notificationService.updateViewStatus(id);
        return buildSimpleResponse(NotificationConstants.UPDATE_SUCCESS);
    }

    @GetMapping("notifications/user")
    public ResponseEntity<?> getUserNotifications(@RequestParam String userId) {
        return buildDataResponse(NotificationConstants.FETCHING_USER_NOTIFICATIONS_SUCCESS, notificationService.getUserNotifications(userId));
    }

    @GetMapping("notifications/user/all")
    public ResponseEntity<?> getAllNotificationsForUser(@RequestParam String userId) {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, notificationService.getAllNotificationsForUserByUserId(userId));
    }

    @GetMapping("notifications/admin/all")
    public ResponseEntity<?> getAllAdminNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_ADMIN_NOTIFICATIONS_SUCCESS, notificationService.getAllAdminNotifications());
    }

    @GetMapping("notifications/admin/unviewed")
    public ResponseEntity<?> getUnViewedAdminNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_UNVIEWED_ADMIN_NOTIFICATIONS_SUCCESS, notificationService.getUnViewedAdminNotifications());
    }

    // Helper methods for building responses
    private ResponseEntity<?> buildDataResponse(String message, List<Notifications> data) {
        ResponseDTO response = ResponseDTO.builder()
                .status(NotificationConstants.STATUS_OK)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<?> buildSimpleResponse(String message) {
        SimpleResponseDTO simpleResponseDTO = SimpleResponseDTO.builder()
                .status(NotificationConstants.STATUS_OK)
                .message(message)
                .build();
        return ResponseEntity.ok().body(simpleResponseDTO);
    }

    private ResponseEntity<?> buildOkResponse(Runnable runnable) {
        runnable.run();
        return ResponseEntity.ok().build();
    }
}
