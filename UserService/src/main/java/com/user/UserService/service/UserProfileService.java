package com.user.UserService.service;

import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.User;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserDTO> getUserById(Long id) throws UserIdNotFoundException;
  //  public User updateUser(User user);

  // public  User convertToEntity(UserDTO dto);
   // public UserDTO createUser(User user);

   // public UserDTO createUser(UserDTO userDTO);
    public UserDTO updateUser(Long id, UserDTO user) throws UserIdNotFoundException;
    public UserDTO convertToDTO(User user);
   // User convertToEntity(UserDTO dto)
   public  <T> T convertToEntity(Object dto, Class<T> entityClass);





}
