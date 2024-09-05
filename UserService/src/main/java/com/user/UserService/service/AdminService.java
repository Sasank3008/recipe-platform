package com.user.UserService.service;

import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.dto.AdminUserDTO;
import com.user.UserService.entity.User;

public interface AdminService {
    public AdminUserDTO updateUser(Long id, AdminUserDTO user) throws UserIdNotFoundException;
    public AdminUserDTO convertToDTO(User user);
    public  <T> T convertToEntity(Object dto, Class<T> entityClass);





}
