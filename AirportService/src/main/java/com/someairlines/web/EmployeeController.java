package com.someairlines.web;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.someairlines.db.EmployeeRepository;
import com.someairlines.entity.Employee;
import com.someairlines.entity.util.Job;

@Controller
@RequestMapping("/employee*")
public class EmployeeController {

	private static final Logger logger = LogManager.getLogger(EmployeeController.class);
	
	private EmployeeRepository employeeRepository;
	
	private final String REDIRECT = "redirect:/employee";
	
	@Autowired
	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	
	@GetMapping
	public String employees(Model model) {
		List<Employee> employees = employeeRepository.findAll();
		logger.debug("Found employees: " + employees);
		model.addAttribute("employees", employees);
		return "admin/employees";
	}
	
	@PostMapping(params = "addEmployeePage")
	public String addEmployeePage(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("jobs", Arrays.asList(Job.values()));
		return "admin/addEmployee";
	}
	
	@PostMapping("/add")
	public String addEmployee(@Valid @ModelAttribute Employee employee) {
		employee.setFree(true);
		logger.debug("Employee to add: " + employee);
		employeeRepository.save(employee);
		return REDIRECT;
	}
	
	@PostMapping(params = "removeEmployeeId")
	public String removeEmployee(@RequestParam("removeEmployeeId") long Id) {
		logger.debug("got Id: " + Id);
		Employee employeeToDelete = employeeRepository.find(Id);
		employeeRepository.delete(employeeToDelete);
		logger.debug("removed Employee: " + employeeToDelete);
		return REDIRECT;
	}
	
	@PostMapping(params = "configureEmployeeId")
	public String changeEmployeePage(Model model, @RequestParam("configureEmployeeId") long Id) {
		logger.debug("got Id: " + Id);
		Employee employeeToChange = employeeRepository.find(Id);
		logger.debug("Employee to change: " + employeeToChange);
		model.addAttribute("employee", employeeToChange);
		model.addAttribute("jobs", Arrays.asList(Job.values()));
		return "admin/configureEmployee";
	}
	
	@PostMapping("/change")
	public String changeEmployee(@Valid @ModelAttribute Employee employee) {
		logger.debug("Got changed employee: " + employee);
		employeeRepository.update(employee);
		logger.debug("Updated employee: " + employee);
		return REDIRECT;
	}
}