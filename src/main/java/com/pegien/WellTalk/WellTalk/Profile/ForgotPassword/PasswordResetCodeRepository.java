package com.pegien.WellTalk.WellTalk.Profile.ForgotPassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, UUID> {

    @Query("select c from PasswordResetCode c where c.userId=?1 and c.passwordResetCode=?2")
    Optional<PasswordResetCode> findResetCode(UUID userId,String resetCode);
}
