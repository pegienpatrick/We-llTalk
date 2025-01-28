package com.pegien.WellTalk.WellTalk.Logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {

    @Query(value = "select l.* from Log l order by l.date desc",nativeQuery = true)
    List<Log> findLastN(int limit);
}
