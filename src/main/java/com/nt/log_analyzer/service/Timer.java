package com.nt.log_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Timer {
	
	@Autowired
	private IndexService indexService;
	
	@Scheduled(cron="*/30 * * * * ?")
	public void Timer1() {
		try {
			//indexService.poll();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
