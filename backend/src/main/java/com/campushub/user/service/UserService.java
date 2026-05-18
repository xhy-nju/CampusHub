package com.campushub.user.service;

import com.campushub.common.error.BusinessException;
import com.campushub.security.AuthenticatedUser;
import com.campushub.user.api.UpdateProfileRequest;
import com.campushub.user.api.UserSummaryResponse;
import com.campushub.user.domain.UserAccount;
import com.campushub.user.domain.UserProfile;
import com.campushub.user.mapper.UserMapper;
import com.campushub.user.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserResponseAssembler userResponseAssembler;

    public UserService(
            UserMapper userMapper,
            UserProfileMapper userProfileMapper,
            UserResponseAssembler userResponseAssembler
    ) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userResponseAssembler = userResponseAssembler;
    }

    public UserSummaryResponse getCurrentUser(AuthenticatedUser currentUser) {
        UserAccount user = getActiveUser(currentUser);
        UserProfile profile = userProfileMapper.findByUserId(user.getId());
        return userResponseAssembler.toSummary(user, profile);
    }

    @Transactional
    public UserSummaryResponse updateProfile(AuthenticatedUser currentUser, UpdateProfileRequest request) {
        UserAccount user = getActiveUser(currentUser);

        UserProfile profile = userProfileMapper.findByUserId(user.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getId());
        }
        profile.setNickname(request.nickname().trim());
        profile.setAvatarUrl(trimToNull(request.avatarUrl()));
        profile.setCollege(trimToNull(request.college()));
        profile.setContact(trimToNull(request.contact()));
        profile.setBio(trimToNull(request.bio()));

        if (userProfileMapper.findByUserId(user.getId()) == null) {
            userProfileMapper.insert(profile);
        } else {
            userProfileMapper.update(profile);
        }

        UserProfile updatedProfile = userProfileMapper.findByUserId(user.getId());
        return userResponseAssembler.toSummary(user, updatedProfile);
    }

    private UserAccount getActiveUser(AuthenticatedUser currentUser) {
        if (currentUser == null) {
            throw BusinessException.unauthorized("Unauthorized");
        }

        UserAccount user = userMapper.findById(currentUser.id());
        if (user == null) {
            throw BusinessException.notFound("User not found");
        }
        if (!"active".equals(user.getStatus())) {
            throw BusinessException.forbidden("User account is disabled");
        }
        return user;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
