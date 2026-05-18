package com.campushub.user.service;

import com.campushub.user.api.UserProfileResponse;
import com.campushub.user.api.UserSummaryResponse;
import com.campushub.user.domain.UserAccount;
import com.campushub.user.domain.UserProfile;
import org.springframework.stereotype.Component;

@Component
class UserResponseAssembler {

    UserSummaryResponse toSummary(UserAccount user, UserProfile profile) {
        return new UserSummaryResponse(
                user.getId(),
                user.getStudentNo(),
                user.getRole(),
                user.getStatus(),
                user.getCreditScore(),
                toProfile(profile)
        );
    }

    private UserProfileResponse toProfile(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        return new UserProfileResponse(
                profile.getNickname(),
                profile.getAvatarUrl(),
                profile.getCollege(),
                profile.getContact(),
                profile.getBio()
        );
    }
}
