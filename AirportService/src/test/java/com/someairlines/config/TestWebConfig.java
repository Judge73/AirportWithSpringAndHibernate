package com.someairlines.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.someairlines.entity.binder.StringToLocalDateTimeConverter;
import com.someairlines.service.EmployeeService;
import com.someairlines.service.FlightService;
import com.someairlines.service.RequestService;
import com.someairlines.web.CrewController;
import com.someairlines.web.EmployeeController;
import com.someairlines.web.FlightController;
import com.someairlines.web.RequestController;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class TestWebConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTimeConverter());
    }
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login.jsp");
    }
	
	@Bean
	public FlightController flightController() {
		return new FlightController(flightService());
	}
	
	@Bean
	public EmployeeController employeeController() {
		return new EmployeeController(employeeService());
	}
	
	@Bean
	public CrewController crewController() {
		return new CrewController(employeeService(), flightService());
	}
	
	@Bean
	public RequestController requestController() {
		return new RequestController(requestService());
	}
	
	@Bean
	public FlightService flightService() {
		return Mockito.mock(FlightService.class);
	}
	
	@Bean
	public EmployeeService employeeService() {
		return Mockito.mock(EmployeeService.class);
	}
	
	@Bean
	public RequestService requestService() {
		return Mockito.mock(RequestService.class);
	}
	
}