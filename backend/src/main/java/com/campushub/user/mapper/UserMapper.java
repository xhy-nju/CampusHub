package com.campushub.user.mapper;

import com.campushub.user.domain.UserAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("""
            SELECT id,
                   student_no,
                   password_hash,
                   role::text AS role,
                   status::text AS status,
                   credit_score,
                   created_at,
                   updated_at
            FROM users
            WHERE id = #{id}
            """)
    @Results(id = "UserAccountResult", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "student_no", property = "studentNo"),
            @Result(column = "password_hash", property = "passwordHash"),
            @Result(column = "role", property = "role"),
            @Result(column = "status", property = "status"),
            @Result(column = "credit_score", property = "creditScore"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    UserAccount findById(Long id);

    @Select("""
            SELECT id,
                   student_no,
                   password_hash,
                   role::text AS role,
                   status::text AS status,
                   credit_score,
                   created_at,
                   updated_at
            FROM users
            WHERE student_no = #{studentNo}
            """)
    @Results(id = "UserAccountByStudentNoResult", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "student_no", property = "studentNo"),
            @Result(column = "password_hash", property = "passwordHash"),
            @Result(column = "role", property = "role"),
            @Result(column = "status", property = "status"),
            @Result(column = "credit_score", property = "creditScore"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    UserAccount findByStudentNo(String studentNo);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE student_no = #{studentNo}")
    boolean existsByStudentNo(String studentNo);

    @Insert("""
            INSERT INTO users (student_no, password_hash)
            VALUES (#{studentNo}, #{passwordHash})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(UserAccount user);
}
