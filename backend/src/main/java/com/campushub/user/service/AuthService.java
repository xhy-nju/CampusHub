package com.campushub.user.service;

import com.campushub.common.error.BusinessException;
import com.campushub.security.JwtService;
import com.campushub.user.api.AuthResponse;
import com.campushub.user.api.LoginRequest;
import com.campushub.user.api.RegisterRequest;
import com.campushub.user.domain.UserAccount;
import com.campushub.user.domain.UserProfile;
import com.campushub.user.mapper.UserMapper;
import com.campushub.user.mapper.UserProfileMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserResponseAssembler userResponseAssembler;

    public AuthService(
            UserMapper userMapper,
            UserProfileMapper userProfileMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserResponseAssembler userResponseAssembler
    ) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userResponseAssembler = userResponseAssembler;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String studentNo = request.studentNo().trim();
        if (userMapper.existsByStudentNo(studentNo)) {
            throw BusinessException.conflict("Student number already exists");
        }

        UserAccount user = new UserAccount();
        user.setStudentNo(studentNo);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userMapper.insert(user);

        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setNickname(defaultNickname(request.nickname(), studentNo));
        profile.setCollege(trimToNull(request.college()));
        profile.setContact(trimToNull(request.contact()));
        userProfileMapper.insert(profile);

        UserAccount createdUser = userMapper.findById(user.getId());
        UserProfile createdProfile = userProfileMapper.findByUserId(user.getId());
        return issueToken(createdUser, createdProfile);
    }

    public AuthResponse login(LoginRequest request) {
        UserAccount user = userMapper.findByStudentNo(request.studentNo().trim());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw BusinessException.unauthorized("Invalid student number or password");
        }
        if (!"active".equals(user.getStatus())) {
            throw BusinessException.forbidden("User account is disabled");
        }

        UserProfile profile = userProfileMapper.findByUserId(user.getId());
        return issueToken(user, profile);
    }

    private AuthResponse issueToken(UserAccount user, UserProfile profile) {
        return new AuthResponse(
                "Bearer",
                jwtService.generateToken(user),
                jwtService.getExpiresInSeconds(),
                userResponseAssembler.toSummary(user, profile)
        );
    }

    private String defaultNickname(String nickname, String studentNo) {
        if (StringUtils.hasText(nickname)) {
            return nickname.trim();
        }
        return studentNo;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
