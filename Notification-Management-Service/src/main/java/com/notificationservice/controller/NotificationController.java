package com.notificationservice.controller;

import com.notificationservice.dto.NotifyUserdto;
import com.notificationservice.dto.Notifydto;
import com.notificationservice.dto.Response;
import com.notificationservice.dto.SimpleResponse;
import com.notificationservice.entity.Notifications;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("notify")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @PostMapping("notifications/notifyAll")
    public ResponseEntity<?> notifyEveryone(@Valid @RequestBody Notifydto notifydto) {
        notificationService.notifyAll(notifydto.getMessage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("notifications/notifyAdmin")
    public ResponseEntity<?> notifyAdmin(@Valid @RequestBody NotifyUserdto notifyUserdto) {
        return buildOkResponse(() -> notificationService.notifyAdmin(notifyUserdto.getEmail(), notifyUserdto.getMessage()));
    }

    @PostMapping("notifications/notifyUser")
    public ResponseEntity<?> notifyUser(@Valid @RequestBody NotifyUserdto notifyUserdto) {
        return buildOkResponse(() -> notificationService.notifyUser(notifyUserdto.getEmail(), notifyUserdto.getMessage()));
    }

    @GetMapping("notifications")
    public ResponseEntity<?> getAllNotifications() {
        return buildDataResponse("Fetching all Notifications was Successful", notificationService.getAllNotifications());
    }

    @PutMapping("notifications")
    public ResponseEntity<?> updateViewStatus(@RequestParam int id) {
        notificationService.updateViewStatus(id);
        return buildSimpleResponse("The Update was Successful");
    }

    @PostMapping("notifications/user")
    public ResponseEntity<?> getUserNotifications(@RequestBody NotifyUserdto notifyUserdto) {
        return buildDataResponse("Fetching User Notifications was Successful", notificationService.getUserNotifications(notifyUserdto.getEmail()));
    }

    @PostMapping("notifications/user/all")
    public ResponseEntity<?> getAllNotificationsForUser(@RequestBody NotifyUserdto notifyUserdto) {
        return buildDataResponse("Fetching all notifications was successful", notificationService.getAllNotificationsForUser(notifyUserdto.getEmail()));
    }

    @GetMapping("notifications/admin/all")
    public ResponseEntity<?> getAllAdminNotifications() {
        return buildDataResponse("Fetching All Admin Notifications was Successful", notificationService.getAllAdminNotifications());
    }

    @GetMapping("notifications/admin/unviewed")
    public ResponseEntity<?> getUnviewedAdminNotifications() {
        return buildDataResponse("Fetching Unviewed Admin Notifications was Successful", notificationService.getUnviewedAdminNotifications());
    }

    private ResponseEntity<?> buildDataResponse(String message, List<Notifications> data) {
        Response response = Response.builder()
                .status("200")
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }

    private ResponseEntity<?> buildSimpleResponse(String message) {
        SimpleResponse simpleResponse = SimpleResponse.builder()
                .status("200")
                .message(message)
                .build();
        return ResponseEntity.ok().body(simpleResponse);
    }

    private ResponseEntity<?> buildOkResponse(Runnable runnable) {
        runnable.run();
        return ResponseEntity.ok().build();
    }
}