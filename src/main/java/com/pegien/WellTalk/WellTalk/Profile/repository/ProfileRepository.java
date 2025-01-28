package com.pegien.WellTalk.WellTalk.Profile.repository;

import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {


    Optional<Profile> findByProfileNameIgnoreCase(String trim);

    Optional<Profile> findByUsernameIgnoreCase(String trim);
}
