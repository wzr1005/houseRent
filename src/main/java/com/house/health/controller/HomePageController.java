package com.house.health.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.house.health.dao.HouseMapper;
import com.house.health.entity.FindReq;
import com.house.health.entity.House;
import com.house.health.service.IHouserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomePageController {

	@Autowired
	private IHouserService service;
	@Resource
	private HouseMapper houseMapper;
	private static final int PAGE_SIZE = 10;
	@Value("${pic.requestPath:http://localhost:8090/images/}")
	private String requestPath;
	@RequestMapping("/toIndexPage")
	public String toIndexPage(HttpServletRequest request) {
		List<House> findHomeInfo = service.findHomeInfo();
		request.getSession().setAttribute("House", findHomeInfo);
		return "index";
	}

	@RequestMapping("/findHouse")
	public String findHouse(FindReq req, HttpServletRequest request) {
		String keywords = request.getParameter("keywords");
		int offset = (req.getPageNum() - 1) * req.getPageSize();
		Map<String, Object> params = new HashMap<>();
		params.put("keywords", keywords);
		params.put("offset", offset);
		params.put("pageSize", req.getPageSize());
		if(req.getOrderBy()!= null){
			params.put("orderBy", req.getOrderBy());
		}
		params.put("orderBy", request.getParameter("orderBy"));
		int totalRecords = houseMapper.countHouseByParams(params);
		int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
		List<House> findHomeInfo = houseMapper.findHouseByParams(params);
		for (House house : findHomeInfo) {
			house.setHouseImage(requestPath + house.getHouseImage());
		}
		request.getSession().removeAttribute("House");
		request.getSession().setAttribute("House", findHomeInfo);
		// 设置属性
		request.setAttribute("currentPage", req.getPageNum());
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("totalRecords", totalRecords);
		return "index";
	}
//	@RequestMapping("/findHouseByLike")
//	public String findHouseByLike(HttpServletRequest request,String keywords) {
//		String pageStr = request.getParameter("page");
//		int currentPage = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
//		int offset = (currentPage - 1) * PAGE_SIZE;
//		int totalRecords = houseMapper.countHouseByLike(keywords);
//		int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
//		List<House> findHomeInfo = service.findHouseByLike(keywords);
//		request.getSession().removeAttribute("House");
//		request.getSession().setAttribute("House", findHomeInfo);
//		// 设置属性
//		request.setAttribute("currentPage", currentPage);
//		request.setAttribute("totalPages", totalPages);
//		return "index";
//	}

	@RequestMapping("/findHousrOrderByAsc")
	public String findHousrOrderByAsc(HttpServletRequest request) {
		List<House> findHomeInfo = service.findHouseOrderByAsc();
		request.getSession().removeAttribute("House");
		request.getSession().setAttribute("House", findHomeInfo);
		return "index";
	}

	@RequestMapping("/findHousrOrderByDesc")
	public String findHousrOrderByDesc(HttpServletRequest request) {
		List<House> findHomeInfo = service.findHouseOrderByDesc();
		request.getSession().removeAttribute("House");
		request.getSession().setAttribute("House", findHomeInfo);
		return "index";
	}
}
