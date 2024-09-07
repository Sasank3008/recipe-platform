package com.user.userservice.controller;
import com.user.userservice.handler.UserIdNotFoundException;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PutMapping("editUser/{id}")
    public ResponseEntity<AdminUserDTO> editUser(@PathVariable Long id, @RequestBody AdminUserDTO userDTO) throws  UserIdNotFoundException {
        AdminUserDTO updatedUserDTO = adminService.updateUser(id, userDTO);
        if (updatedUserDTO != null) {
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
