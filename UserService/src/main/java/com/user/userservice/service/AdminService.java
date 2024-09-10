package com.user.userservice.service;

import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.UserIdNotFoundException;

public interface AdminService {
    public AdminUserDTO updateUser(Long id, AdminUserDTO user) throws UserIdNotFoundException;
    public AdminUserDTO convertToDTO(User user);
    public  <T> T convertToEntity(Object dto, Class<T> entityClass);





}
