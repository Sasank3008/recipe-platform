package com.notificationservice.service;

import com.notificationservice.entity.Notifications;
import com.notificationservice.exception.NotFoundException;
import com.notificationservice.constants.DestinationConstants;
import com.notificationservice.repository.NotificationDao;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceImplTest {

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;

	@Mock
	private DestinationConstants destinationConstants;

	@Mock
	private NotificationDao notificationDao;

	@InjectMocks
	private NotificationServiceImpl notificationServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testNotifyAll() {
		String message = "Test Message";

		notificationServiceImpl.notifyAll(message);

		verify(simpMessagingTemplate, times(1)).convertAndSend(destinationConstants.getAllDestination(), message);
		verify(notificationDao, times(1)).save(ArgumentMatchers.any(Notifications.class));
	}

	@Test
	void testNotifyAdmin() {
		String userId = "adminUser";
		String message = "Admin Message";
		String userEmail = "user_email_for_adminUser@example.com";

		when(destinationConstants.getAdminDestination()).thenReturn("/admin/notifications");
		when(notificationDao.save(any(Notifications.class))).thenReturn(new Notifications());

		notificationServiceImpl.notifyAdmin(userId, message);

		verify(simpMessagingTemplate, times(1)).convertAndSend(destinationConstants.getAdminDestination(), message);
		verify(notificationDao, times(1)).save(Notifications.builder()
				.recipient("Admin")
				.sender(userEmail)
				.message(message)
				.build());
	}

	@Test
	void testNotifyMultipleUsers() {
		List<String> userIds = Arrays.asList("user123", "user456", "user789");
		String message = "User Message";
		when(destinationConstants.getUserDestination()).thenReturn("/user/notifications/");
		when(notificationDao.save(any(Notifications.class))).thenReturn(new Notifications());
		notificationServiceImpl.notifyUsers(userIds, message);
		for (String userId : userIds) {
			String userEmail = "user_email_for_" + userId + "@example.com";
			verify(simpMessagingTemplate, times(1)).convertAndSend(destinationConstants.getUserDestination() + userId, message);
			verify(notificationDao, times(1)).save(Notifications.builder()
					.recipient(userEmail)
					.sender("Admin")
					.message(message)
					.build());
		}
	}

	@Test
	void testGetAllNotifications() {
		List<Notifications> notifications = Arrays.asList(
				new Notifications(),
				new Notifications()
		);

		when(notificationDao.findAll()).thenReturn(notifications);

		List<Notifications> result = notificationServiceImpl.getAllNotifications();

		assertEquals(notifications, result);
		verify(notificationDao, times(1)).findAll();
	}

	@Test
	void testUpdateViewStatus() {
		int id = 1;
		Notifications notification = new Notifications();
		notification.setViewed(false);

		when(notificationDao.findById(id)).thenReturn(Optional.of(notification));
		when(notificationDao.save(notification)).thenReturn(notification);

		Notifications result = notificationServiceImpl.updateViewStatus(id);

		assertTrue(result.isViewed());
		verify(notificationDao, times(1)).findById(id);
		verify(notificationDao, times(1)).save(notification);
	}

	@Test
	void testUpdateViewStatusNotFound() {
		int id = 1;

		when(notificationDao.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> notificationServiceImpl.updateViewStatus(id));

		verify(notificationDao, times(1)).findById(id);
		verify(notificationDao, never()).save(any(Notifications.class));
	}

	@Test
	void testGetUserNotifications() {
		String userId = "user123";
		String userEmail = "user_email_for_user123@example.com";
		List<Notifications> notifications = Arrays.asList(
				new Notifications(),
				new Notifications()
		);

		when(notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList(userId, "Everyone")))
				.thenReturn(notifications);

		List<Notifications> result = notificationServiceImpl.getUserNotifications(userId);

		assertEquals(notifications, result);
		verify(notificationDao, times(1)).findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList(userId, "Everyone"));
	}

	@Test
	void testGetAllNotificationsForUserByUserId() {
		String userId = "user123";
		String userEmail = "user_email_for_user123@example.com";
		List<Notifications> notifications = Arrays.asList(
				new Notifications(),
				new Notifications()
		);

		when(notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList(userId, "Everyone")))
				.thenReturn(notifications);

		List<Notifications> result = notificationServiceImpl.getAllNotificationsForUserByUserId(userId);

		assertEquals(notifications, result);
		verify(notificationDao, times(1)).findByRecipientInOrderByCreateAtDesc(Arrays.asList(userId, "Everyone"));
	}

	@Test
	void testGetAllAdminNotifications() {
		List<Notifications> notifications = Arrays.asList(
				new Notifications(),
				new Notifications()
		);

		when(notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList("Admin")))
				.thenReturn(notifications);

		List<Notifications> result = notificationServiceImpl.getAllAdminNotifications();

		assertEquals(notifications, result);
		verify(notificationDao, times(1)).findByRecipientInOrderByCreateAtDesc(Arrays.asList("Admin"));
	}

	@Test
	void testGetUnViewedAdminNotifications() {
		List<Notifications> notifications = Arrays.asList(
				new Notifications(),
				new Notifications()
		);
		notifications.get(0).setViewed(false);
		notifications.get(1).setViewed(true);

		when(notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList("Admin")))
				.thenReturn(notifications);

		List<Notifications> result = notificationServiceImpl.getUnViewedAdminNotifications();

		assertEquals(1, result.size());
		assertFalse(result.get(0).isViewed());
		verify(notificationDao, times(1)).findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList("Admin"));
	}
}
