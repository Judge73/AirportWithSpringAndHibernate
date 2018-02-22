package com.someairlines.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.someairlines.db.RequestRepository;
import com.someairlines.entity.Request;
import com.someairlines.entity.util.RequestStatus;

@RequestMapping("/request*")
@Controller
public class RequestController {

	private RequestRepository requestRepository;

	private static final Logger logger = LogManager.getLogger(RequestController.class);
	
	@Autowired
	public RequestController(RequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}
	
	@GetMapping
	public String requestsPage(Model model) {
		String view;
		List<Request> requests;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		GrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
		if(auth.getAuthorities().contains(adminAuthority)) {
			view = "admin/requests";
			requests = requestRepository.findAllNew();
		} else {
			view = "dispatcher/requests";
			requests = requestRepository.findAllForUser(auth.getName());
		}
		model.addAttribute("requests", requests);
		return view;
	}
	
	@GetMapping("/open")
	public String openRequest(@RequestParam long requestId, Model model) {
		Request request = requestRepository.find(requestId);
		logger.debug("found request: " + request);
		model.addAttribute("request", request);
		return "admin/requestInfo";
	}
	
	@PostMapping("/update")
	public String processRequest(@RequestParam long requestId, @RequestParam String status) {
		Request request = requestRepository.find(requestId);
		RequestStatus requestStatus = RequestStatus.valueOf(status);
		request.setStatus(requestStatus);
		requestRepository.update(request);
		return "redirect:/request";
	}
	
	@GetMapping("/write")
	public String writeRequest(Model model) {
		model.addAttribute("request", new Request());
		return "dispatcher/requestForm";
	}
	
	@PostMapping("/write")
	public String writeRequest(@Valid @ModelAttribute Request request) {
		logger.debug("Got request: " + request);
		request.setStatus(RequestStatus.NEW);
		requestRepository.save(request);
		return "redirect:/request";
	}
}