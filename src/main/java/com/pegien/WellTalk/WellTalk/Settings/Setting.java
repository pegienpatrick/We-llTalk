package com.pegien.WellTalk.WellTalk.Settings;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @Column(length = 10000)
    private String settingName;

    private String Stringvalue;

    private String[] StringArrayValue;

    private Integer integerValue;

    private Integer[] integerArrayValue;

    private Double doubleValue;

    private Double[] doubleArrayValue;

}
