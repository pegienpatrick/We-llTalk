package com.pegien.WellTalk.WellTalk.Logs.controller;



import com.pegien.WellTalk.WellTalk.Logs.Log;
import com.pegien.WellTalk.WellTalk.Logs.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class LogsController {

    @Autowired
    private LogService logService;

    @GetMapping("/listLogs")
    public ResponseEntity<List<Log>> findPreviousLogs()
    {
        return logService.listLogs();
    }


}
