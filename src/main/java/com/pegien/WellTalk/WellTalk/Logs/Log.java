package com.pegien.WellTalk.WellTalk.Logs;



import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private Long date;

    @ManyToOne
    private Profile profile;

    @Column(columnDefinition = "TEXT",length = 10000)
    private String log;


}
