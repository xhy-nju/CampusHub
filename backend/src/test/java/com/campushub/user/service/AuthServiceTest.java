package com.campushub.user.service;

import com.campushub.security.JwtService;
import com.campushub.user.api.AuthResponse;
import com.campushub.user.api.RegisterRequest;
import com.campushub.user.domain.UserAccount;
import com.campushub.user.domain.UserProfile;
import com.campushub.user.mapper.UserMapper;
import com.campushub.user.mapper.UserProfileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userMapper,
                userProfileMapper,
                passwordEncoder,
                jwtService,
                new UserResponseAssembler()
        );
    }

    @Test
    void registerStoresPhoneInUserProfileContact() {
        when(userMapper.existsByStudentNo("student001")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        doAnswer(invocation -> {
            UserAccount account = invocation.getArgument(0);
            account.setId(1L);
            return 1;
        }).when(userMapper).insert(any(UserAccount.class));

        UserAccount createdUser = user("student001");
        UserProfile createdProfile = profile("Student", "13800138000");
        when(userMapper.findById(1L)).thenReturn(createdUser);
        when(userProfileMapper.findByUserId(1L)).thenReturn(createdProfile);
        when(jwtService.generateToken(createdUser)).thenReturn("token-value");
        when(jwtService.getExpiresInSeconds()).thenReturn(86400L);

        AuthResponse response = authService.register(new RegisterRequest(
                " student001 ",
                "password123",
                " Student ",
                null,
                " 13800138000 "
        ));

        ArgumentCaptor<UserProfile> profileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileMapper).insert(profileCaptor.capture());
        UserProfile savedProfile = profileCaptor.getValue();

        assertEquals(1L, savedProfile.getUserId());
        assertEquals("Student", savedProfile.getNickname());
        assertEquals("13800138000", savedProfile.getContact());
        assertEquals("token-value", response.accessToken());
        assertEquals("13800138000", response.user().profile().contact());
    }

    private UserAccount user(String studentNo) {
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setStudentNo(studentNo);
        user.setPasswordHash("encoded-password");
        user.setRole("user");
        user.setStatus("active");
        user.setCreditScore(100);
        return user;
    }

    private UserProfile profile(String nickname, String contact) {
        UserProfile profile = new UserProfile();
        profile.setUserId(1L);
        profile.setNickname(nickname);
        profile.setContact(contact);
        return profile;
    }
}
