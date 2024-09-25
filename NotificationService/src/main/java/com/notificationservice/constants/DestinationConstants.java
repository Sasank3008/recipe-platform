package com.notificationservice.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DestinationConstants {
    private String allDestination="/queue/notifications";
    private String adminDestination="/topic/admin-notifications";
    private String userDestination="/queue/user/";
}
