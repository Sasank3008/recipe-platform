package com.user.UserService.Clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ADMIN-SERVICE")
public interface AdminService {
}
