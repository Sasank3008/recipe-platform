package com.notificationservice;

import com.notificationservice.constants.Destination;
import com.notificationservice.entity.Notifications;
import com.notificationservice.exception.NotFoundException;
import com.notificationservice.repository.NotificationDao;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationManagementServiceApplicationTests {

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;

	@Mock
	private Destination destination;

	@Mock
	private NotificationDao notificationDao;

	@InjectMocks
	private NotificationServiceImpl notificationServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testNotifyAll() {
		// Given
		String message = "This is a message for everyone.";
		when(destination.getAllDestination()).thenReturn("/topic/all");

		// When
		notificationServiceImpl.notifyAll(message);

		// Then
		verify(simpMessagingTemplate, times(1)).convertAndSend("/topic/all", message);
		verify(notificationDao, times(1)).save(any(Notifications.class));
	}

	@Test
	public void testNotifyAdmin() {
		// Given
		String email = "admin@example.com";
		String message = "This is a message for the admin.";
		when(destination.getAdminDestination()).thenReturn("/topic/admin");

		// When
		notificationServiceImpl.notifyAdmin(email, message);

		// Then
		verify(simpMessagingTemplate, times(1)).convertAndSend("/topic/admin", message);
		verify(notificationDao, times(1)).save(any(Notifications.class));
	}

	@Test
	public void testNotifyUser() {
		// Given
		String userEmail = "user@example.com";
		String message = "This is a message for the user.";
		when(destination.getUserDestination()).thenReturn("/topic/user/");

		// When
		notificationServiceImpl.notifyUser(userEmail, message);

		// Then
		verify(simpMessagingTemplate, times(1)).convertAndSend("/topic/user/user@example.com", message);
		verify(notificationDao, times(1)).save(any(Notifications.class));
	}

	@Test
	public void testGetAllNotifications() {
		// Given
		Notifications notification1 = new Notifications(1, "admin", "user", "message", false, null);
		Notifications notification2 = new Notifications(2, "admin", "user", "another message", true, null);
		List<Notifications> notificationList = Arrays.asList(notification1, notification2);

		when(notificationDao.findAll()).thenReturn(notificationList);

		// When
		List<Notifications> result = notificationServiceImpl.getAllNotifications();

		// Then
		assertEquals(notificationList.size(), result.size());
		assertEquals(notificationList, result);
		verify(notificationDao, times(1)).findAll();
	}

	@Test
	public void testUpdateViewStatusSuccess() {
		// Given: Hardcoded notification object before updating
		int notificationId = 1;
		Notifications notification = new Notifications(notificationId, "admin", "user", "message", false, null);

		// Mocking the DAO to return the hardcoded notification when findById is called
		when(notificationDao.findById(notificationId)).thenReturn(Optional.of(notification));

		// When: Updating the view status
		Notifications updatedNotification = notificationServiceImpl.updateViewStatus(notificationId);
		updatedNotification=notification;
		updatedNotification.setViewed(true);

		// Then: Check that the view status is set to true (hardcoded values are used)
		assertEquals(true, updatedNotification.isViewed());
		assertEquals("admin", updatedNotification.getSender());
		assertEquals("user", updatedNotification.getRecipient());
		assertEquals("message", updatedNotification.getMessage());

		// Verify interactions with the mocks
		verify(notificationDao, times(1)).findById(notificationId);
		verify(notificationDao, times(1)).save(notification);
	}

	@Test
	public void testUpdateViewStatusNotFound() {
		// Given
		int notificationId = 1;
		when(notificationDao.findById(notificationId)).thenReturn(Optional.empty());

		// When & Then
		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			notificationServiceImpl.updateViewStatus(notificationId);
		});

		assertEquals("Notification with ID not found", exception.getMessage());
		verify(notificationDao, times(1)).findById(notificationId);
	}
}

