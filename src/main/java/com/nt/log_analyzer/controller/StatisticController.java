package com.nt.log_analyzer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nt.log_analyzer.enums.ResultEnum;
import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogResult;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.service.StatisticService;

@Controller
public class StatisticController {
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private StatisticService statisticService;
	
	@RequestMapping("/statistic")
	public String statistic(Model model) {
		Map<String, Integer> map = statisticService.count();
		model.addAttribute("result", map);
		
		//return map;
		return "statistic";
	}
	
	/*@RequestMapping("/query")
	public String getBykey(@RequestParam(value="key") String key,Model model){
		
		if (key == null || key.equals("")) {
			model.addAttribute("logResult",LogResult.build(-1, "查询关键字为空"));
			return "search";
		}
		
		
		List<FileModel> fileModels = null;
		LogResult logResult = null;
		try {
			//fileModels = indexService.getTopDocs(20, key);
			if (fileModels.size()==0) {
				//System.out.println("查询结果为空");
				model.addAttribute("logResult", logResult.build(-2, "查询结果为空"));
			}else {
				model.addAttribute("logResult", logResult.success(fileModels));
			}
			
		} catch (Exception e) {
			model.addAttribute("logResult",logResult.build(ResultEnum.SELECT_ERROR.getCode(),ResultEnum.SELECT_ERROR.getMsg()));
			e.printStackTrace();
		}
		
		return "search";
	}*/
	
	@RequestMapping("/poll")
	@ResponseBody
	public String poll() {
		try {
			indexService.poll();
			return "创建成功";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "创建失败";
		}
		
	}
	
	
}
