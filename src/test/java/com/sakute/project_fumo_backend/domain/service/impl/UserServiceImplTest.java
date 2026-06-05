package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.dto.user.AdminUserDto;
import com.sakute.project_fumo_backend.domain.dto.user.UserMapper;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    // -------------------------------------------------------
    // getAllUsers — search routing
    // -------------------------------------------------------

    @Test
    void getAllUsers_withSearch_callsSearchRepository() {
        String search = "john";
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user("john", "john@test.com")));

        when(userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                search, search, pageable)).thenReturn(page);

        Page<AdminUserDto> result = userService.getAllUsers(pageable, search);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(userRepository).findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                search, search, pageable);
        verify(userRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getAllUsers_withNullSearch_callsFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(Page.empty());

        userService.getAllUsers(pageable, null);

        verify(userRepository).findAll(pageable);
        verify(userRepository, never())
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(any(), any(), any());
    }

    @Test
    void getAllUsers_withBlankSearch_callsFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(Page.empty());

        userService.getAllUsers(pageable, "   ");

        verify(userRepository).findAll(pageable);
        verify(userRepository, never())
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(any(), any(), any());
    }

    @Test
    void getAllUsers_shouldMapEntitiesToDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        User u = user("alice", "alice@test.com");
        AdminUserDto dto = AdminUserDto.builder().username("alice").email("alice@test.com").build();

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(u)));
        when(userMapper.toAdminDto(u)).thenReturn(dto);

        Page<AdminUserDto> result = userService.getAllUsers(pageable, null);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo("alice");
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("alice@test.com");
    }

    // -------------------------------------------------------
    // updateUser
    // -------------------------------------------------------

    @Test
    void updateUser_shouldThrow_whenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(id, new AdminUserDto()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void updateUser_shouldUpdateFieldsAndReturnDto() {
        UUID id = UUID.randomUUID();
        User existing = user("oldname", "old@test.com");

        AdminUserDto dto = AdminUserDto.builder()
                .email("new@test.com")
                .fullName("New Name")
                .role(Role.ADMIN)
                .permissions(Set.of(Permission.CAN_FUNDRAISE))
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        ResponseEntity<AdminUserDto> response = userService.updateUser(id, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(existing.getEmail()).isEqualTo("new@test.com");
        assertThat(existing.getFullName()).isEqualTo("New Name");
        assertThat(existing.getRole()).isEqualTo(Role.ADMIN);
        assertThat(existing.getPermissions()).contains(Permission.CAN_FUNDRAISE);
    }

    // -------------------------------------------------------
    // updatePermissions
    // -------------------------------------------------------

    @Test
    void updatePermissions_shouldThrow_whenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updatePermissions(id, Set.of(Permission.CAN_SELL_IP)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updatePermissions_shouldSetAndSavePermissions() {
        UUID id = UUID.randomUUID();
        User u = user("test", "test@test.com");
        Set<Permission> newPermissions = Set.of(Permission.CAN_FUNDRAISE, Permission.CAN_SELL_IP);

        when(userRepository.findById(id)).thenReturn(Optional.of(u));

        userService.updatePermissions(id, newPermissions);

        assertThat(u.getPermissions()).isEqualTo(newPermissions);
        verify(userRepository).save(u);
    }

    // -------------------------------------------------------
    // deleteById
    // -------------------------------------------------------

    @Test
    void deleteById_shouldCallRepositoryAndReturn204() {
        UUID id = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(id);

        ResponseEntity<Void> response = userService.deleteById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userRepository).deleteById(id);
    }

    // -------------------------------------------------------
    // findByName
    // -------------------------------------------------------

    @Test
    void findByName_shouldReturn200_whenUsersFound() {
        String name = "alice";
        User u = user("alice", "alice@test.com");
        when(userRepository.findUserByUsername(name)).thenReturn(List.of(u));

        ResponseEntity<List<User>> response = userService.findByName(name);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void findByName_shouldReturn204_whenNoUsersFound() {
        String name = "ghost";
        when(userRepository.findUserByUsername(name)).thenReturn(List.of());

        ResponseEntity<List<User>> response = userService.findByName(name);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // -------------------------------------------------------
    // helpers
    // -------------------------------------------------------

    private User user(String username, String email) {
        User u = new User();
        u.setUserId(UUID.randomUUID());
        u.setUsername(username);
        u.setEmail(email);
        u.setRole(Role.USER);
        u.setCreatedAt(Timestamp.from(Instant.now()));
        return u;
    }
}
