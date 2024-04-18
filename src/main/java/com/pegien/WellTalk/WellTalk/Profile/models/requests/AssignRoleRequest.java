package com.pegien.WellTalk.WellTalk.Profile.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AssignRoleRequest {

    @NotNull(message = "You Have null roles")
    List<String> roles;


}
