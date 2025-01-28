package com.pegien.WellTalk.WellTalk.TWOFactor;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TwoFactor {

    @Id
    private UUID profileId;

    private String secret;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isVerified;
}
