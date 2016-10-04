package model;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CustomerService {
	private CustomerDAO customerDao;
	public CustomerService(CustomerDAO customerDao) {
		this.customerDao = customerDao;
	}
	public CustomerBean login(String username, String password) {
		CustomerBean bean = customerDao.select(username);
		if(bean!=null) {
			if(password!=null && password.length()!=0) {
				byte[] pass = password.getBytes();
				byte[] temp = bean.getPassword();
				if(Arrays.equals(pass, temp)) {
					return bean;
				}
			}
		}
		return null;
	}
	public boolean changePassword(String username, String oldPassword, String newPassword) {
		CustomerBean bean = this.login(username, oldPassword);
		if(bean!=null) {
			if(newPassword!=null && newPassword.length()!=0) {
				byte[] pass = newPassword.getBytes();
				return customerDao.update(
						pass, bean.getEmail(), bean.getBirth(), username);
			}
		}
		return false;
	}
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.config.xml");
		
		CustomerService customerService = (CustomerService) context.getBean("customerService");
		CustomerBean bean = customerService.login("Babe", "B");
		System.out.println(bean);
		customerService.changePassword("Ellen", "EEE", "E");
		
		((ConfigurableApplicationContext) context).close();
	}
}
