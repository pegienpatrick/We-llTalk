package com.pegien.WellTalk.WellTalk.Settings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SettingRepository extends JpaRepository<Setting, UUID> {

    Optional<Setting> findBySettingNameIgnoreCase(String setting);


}
