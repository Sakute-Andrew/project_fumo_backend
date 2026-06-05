package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotAuthorizedException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyDto;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyMapper;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.IntellectualPropertyCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.IntellectualPropertyRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntellectualPropertyServiceImplTest {

    @Mock private IntellectualPropertyRepository ipRepository;
    @Mock private IntellectualPropertyCategoryRepository categoryRepository;
    @Mock private UserRepository userRepository;
    @Mock private IntellectualPropertyMapper mapper;

    @InjectMocks
    private IntellectualPropertyServiceImpl ipService;

    // -------------------------------------------------------
    // findById
    // -------------------------------------------------------

    @Test
    void findById_shouldReturnDto_whenExists() {
        UUID id = UUID.randomUUID();
        IntellectualProperty entity = new IntellectualProperty();
        IntellectualPropertyDto dto = new IntellectualPropertyDto();

        when(ipRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        assertThat(ipService.findById(id)).isEqualTo(dto);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(ipRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ipService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // -------------------------------------------------------
    // create
    // -------------------------------------------------------

    @Test
    void create_shouldSetOwnerAndAvailableStatus() {
        IntellectualPropertyDto dto = new IntellectualPropertyDto();
        IntellectualProperty entity = new IntellectualProperty();
        IntellectualProperty saved = new IntellectualProperty();
        IntellectualPropertyDto expectedDto = new IntellectualPropertyDto();
        User owner = new User();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(ipRepository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(expectedDto);

        mockSecurityContext("testuser", false);

        IntellectualPropertyDto result = ipService.create(dto);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(entity.getOwner()).isEqualTo(owner);
        assertThat(entity.getStatus()).isEqualTo(IpStatus.AVAILABLE);
    }

    @Test
    void create_shouldThrow_whenOwnerUserNotFound() {
        IntellectualPropertyDto dto = new IntellectualPropertyDto();
        IntellectualProperty entity = new IntellectualProperty();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockSecurityContext("ghost", false);

        assertThatThrownBy(() -> ipService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost");
    }

    // -------------------------------------------------------
    // update
    // -------------------------------------------------------

    @Test
    void update_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(ipRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ipService.update(id, new IntellectualPropertyDto()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_shouldThrow_whenNotOwnerAndNotAdmin() {
        UUID id = UUID.randomUUID();
        IntellectualProperty existing = new IntellectualProperty();

        when(ipRepository.findById(id)).thenReturn(Optional.of(existing));
        when(ipRepository.existsByIpIdAndOwner_Username(id, "stranger")).thenReturn(false);

        mockSecurityContext("stranger", false);

        assertThatThrownBy(() -> ipService.update(id, new IntellectualPropertyDto()))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessageContaining("доступу");
    }

    @Test
    void update_shouldSucceed_whenUserIsOwner() {
        UUID id = UUID.randomUUID();
        IntellectualProperty existing = new IntellectualProperty();
        IntellectualPropertyDto dto = buildUpdateDto("New name", "New desc");
        IntellectualPropertyDto expectedDto = new IntellectualPropertyDto();

        when(ipRepository.findById(id)).thenReturn(Optional.of(existing));
        when(ipRepository.existsByIpIdAndOwner_Username(id, "owner")).thenReturn(true);
        when(ipRepository.save(existing)).thenReturn(existing);
        when(mapper.toDto(existing)).thenReturn(expectedDto);

        mockSecurityContext("owner", false);

        IntellectualPropertyDto result = ipService.update(id, dto);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(existing.getName()).isEqualTo("New name");
        assertThat(existing.getDescription()).isEqualTo("New desc");
    }

    @Test
    void update_shouldSucceed_whenUserIsAdmin() {
        UUID id = UUID.randomUUID();
        IntellectualProperty existing = new IntellectualProperty();
        IntellectualPropertyDto dto = buildUpdateDto("Admin edit", "desc");
        IntellectualPropertyDto expectedDto = new IntellectualPropertyDto();

        when(ipRepository.findById(id)).thenReturn(Optional.of(existing));
        when(ipRepository.existsByIpIdAndOwner_Username(id, "admin")).thenReturn(false);
        when(ipRepository.save(existing)).thenReturn(existing);
        when(mapper.toDto(existing)).thenReturn(expectedDto);

        mockSecurityContext("admin", true);

        IntellectualPropertyDto result = ipService.update(id, dto);

        assertThat(result).isEqualTo(expectedDto);
    }

    // -------------------------------------------------------
    // delete
    // -------------------------------------------------------

    @Test
    void delete_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(ipRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ipService.delete(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotOwnerAndNotAdmin() {
        UUID id = UUID.randomUUID();
        IntellectualProperty entity = new IntellectualProperty();

        when(ipRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ipRepository.existsByIpIdAndOwner_Username(id, "stranger")).thenReturn(false);

        mockSecurityContext("stranger", false);

        assertThatThrownBy(() -> ipService.delete(id))
                .isInstanceOf(NotAuthorizedException.class);
    }

    @Test
    void delete_shouldDelete_whenUserIsOwner() {
        UUID id = UUID.randomUUID();
        IntellectualProperty entity = new IntellectualProperty();

        when(ipRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ipRepository.existsByIpIdAndOwner_Username(id, "owner")).thenReturn(true);

        mockSecurityContext("owner", false);

        ipService.delete(id);

        verify(ipRepository).delete(entity);
    }

    // -------------------------------------------------------
    // changeStatus
    // -------------------------------------------------------

    @Test
    void changeStatus_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(ipRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ipService.changeStatus(id, "AVAILABLE"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void changeStatus_shouldThrow_whenStatusStringInvalid() {
        UUID id = UUID.randomUUID();
        IntellectualProperty entity = new IntellectualProperty();
        when(ipRepository.findById(id)).thenReturn(Optional.of(entity));

        // IpStatus.valueOf("INVALID") кине IllegalArgumentException
        assertThatThrownBy(() -> ipService.changeStatus(id, "INVALID_STATUS"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeStatus_shouldUpdateStatus_whenValid() {
        UUID id = UUID.randomUUID();
        IntellectualProperty entity = new IntellectualProperty();
        IntellectualPropertyDto expectedDto = new IntellectualPropertyDto();

        when(ipRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ipRepository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expectedDto);

        ipService.changeStatus(id, "AVAILABLE");

        assertThat(entity.getStatus()).isEqualTo(IpStatus.AVAILABLE);
        verify(ipRepository).save(entity);
    }

    // -------------------------------------------------------
    // Хелпери
    // -------------------------------------------------------

    private void mockSecurityContext(String username, boolean isAdmin) {
        GrantedAuthority authority = () -> isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        lenient().when(auth.getAuthorities()).thenAnswer(inv -> (Collection<GrantedAuthority>) List.of(authority));

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    private IntellectualPropertyDto buildUpdateDto(String name, String description) {
        IntellectualPropertyDto dto = new IntellectualPropertyDto();
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }
}