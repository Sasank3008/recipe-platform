package com.notificationservice.controller;

import com.notificationservice.constants.NotificationConstants;
import com.notificationservice.dto.*;
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
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @PostMapping("notifications/notifyAll")
    public ResponseEntity<Void> notifyEveryone(@Valid @RequestBody NotifyDTO notifydto) {
        notificationService.notifyAll(notifydto.getMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("notifications/notifyAdmin")
    public ResponseEntity<Void> notifyAdmin(@Valid @RequestBody NotifyUserDTO notifyUserdto) {
        return buildOkResponse(() -> notificationService.notifyAdmin(notifyUserdto.getUserId(), notifyUserdto.getMessage()));
    }

    @PostMapping("notifications/notifyUsers")
    public ResponseEntity<String> notifyMultipleUsers(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        List<String> userIds = notificationRequestDTO.getUserIds();
        String message = notificationRequestDTO.getMessage();

        notificationService.notifyUsers(userIds, message);

        return ResponseEntity.ok("Notifications sent successfully to all users");
    }

    @GetMapping("notifications")
    public ResponseEntity<ResponseDTO> getAllNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, notificationService.getAllNotifications());
    }

    @PutMapping("notifications")
    public ResponseEntity<SimpleResponseDTO> updateViewStatus(@RequestParam int id) {
        notificationService.updateViewStatus(id);
        return buildSimpleResponse(NotificationConstants.UPDATE_SUCCESS);
    }

    @GetMapping("notifications/user")
    public ResponseEntity<ResponseDTO> getUserNotifications(@RequestParam String userId) {
        return buildDataResponse(NotificationConstants.FETCHING_USER_NOTIFICATIONS_SUCCESS, notificationService.getUserNotifications(userId));
    }

    @GetMapping("notifications/user/all")
    public ResponseEntity<ResponseDTO> getAllNotificationsForUser(@RequestParam String userId) {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, notificationService.getAllNotificationsForUserByUserId(userId));
    }

    @GetMapping("notifications/admin/all")
    public ResponseEntity<ResponseDTO> getAllAdminNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_ALL_ADMIN_NOTIFICATIONS_SUCCESS, notificationService.getAllAdminNotifications());
    }

    @GetMapping("notifications/admin/unviewed")
    public ResponseEntity<ResponseDTO> getUnViewedAdminNotifications() {
        return buildDataResponse(NotificationConstants.FETCHING_UNVIEWED_ADMIN_NOTIFICATIONS_SUCCESS, notificationService.getUnViewedAdminNotifications());
    }

    // Helper methods for building responses
    private ResponseEntity<ResponseDTO> buildDataResponse(String message, List<Notifications> data) {
        ResponseDTO response = ResponseDTO.builder()
                .status(NotificationConstants.STATUS_OK)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<SimpleResponseDTO> buildSimpleResponse(String message) {
        SimpleResponseDTO simpleResponseDTO = SimpleResponseDTO.builder()
                .status(NotificationConstants.STATUS_OK)
                .message(message)
                .build();
        return ResponseEntity.ok().body(simpleResponseDTO);
    }

    private ResponseEntity<Void> buildOkResponse(Runnable runnable) {
        runnable.run();
        return ResponseEntity.ok().build();
    }
@GetMapping("/count/unread/{userId}")
public UnreadNotificationCountResponse getUnreadNotificationCount(@PathVariable String userId) {
    long unreadCount = notificationService.getUnreadNotificationCount(userId);
    return new UnreadNotificationCountResponse(unreadCount); // Return the count wrapped in a DTO
}
}
