package com.campushub.user.mapper;

import com.campushub.user.domain.UserProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserProfileMapper {

    @Select("""
            SELECT user_id,
                   nickname,
                   avatar_url,
                   college,
                   contact,
                   bio,
                   created_at,
                   updated_at
            FROM user_profiles
            WHERE user_id = #{userId}
            """)
    @Results(id = "UserProfileResult", value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column = "avatar_url", property = "avatarUrl"),
            @Result(column = "college", property = "college"),
            @Result(column = "contact", property = "contact"),
            @Result(column = "bio", property = "bio"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    UserProfile findByUserId(Long userId);

    @Insert("""
            INSERT INTO user_profiles (user_id, nickname, avatar_url, college, contact, bio)
            VALUES (#{userId}, #{nickname}, #{avatarUrl}, #{college}, #{contact}, #{bio})
            """)
    int insert(UserProfile profile);

    @Update("""
            UPDATE user_profiles
            SET nickname = #{nickname},
                avatar_url = #{avatarUrl},
                college = #{college},
                contact = #{contact},
                bio = #{bio}
            WHERE user_id = #{userId}
            """)
    int update(UserProfile profile);
}
