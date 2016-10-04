package model.settings;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import model.CustomerBean;
import model.CustomerService;
import model.ProductBean;
import model.ProductService;
import model.dao.CustomerDAOJdbc;
import model.dao.ProductDAOJdbc;

@Configuration
public class SpringJavaConfiguration {
	@Bean
	public DataSource dataSource() {
		DataSource result = null;
		try {
			Context ctx = new InitialContext();
			result = (DataSource) ctx.lookup("java:comp/env/jdbc/xxx");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Bean
	public CustomerService customerService() {
		return new CustomerService(new CustomerDAOJdbc(dataSource()));
	}
	@Bean
	public ProductService productService() {
		return new ProductService(new ProductDAOJdbc(dataSource()));
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringJavaConfiguration.class); 
		
		CustomerService customerService = (CustomerService) context.getBean("customerService");
		CustomerBean bean = customerService.login("Carol", "C");
		System.out.println(bean);
		
		ProductService service = (ProductService) context.getBean("productService");
		List<ProductBean> beans = service.select(null);
		System.out.println("beans="+beans);
		
		((ConfigurableApplicationContext) context).close();
	}
}
