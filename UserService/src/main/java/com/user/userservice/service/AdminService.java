package com.user.userservice.service;

import com.user.userservice.exception.UserIdNotFoundException;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.entity.User;

public interface AdminService {
    public AdminUserDTO updateUser(Long id, AdminUserDTO user) throws UserIdNotFoundException;
    public AdminUserDTO convertToDTO(User user);
    public  <T> T convertToEntity(Object dto, Class<T> entityClass);





}
