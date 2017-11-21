package com.xsjt.springboottest;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xsjt.bean.Order;
import com.xsjt.bean.OrderDetail;
import com.xsjt.bean.Student;
import com.xsjt.service.IOrderDetailService;
import com.xsjt.service.IOrderService;
import com.xsjt.service.IStudentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTestApplicationTests {

	@Autowired
	private IStudentService studentService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderDetailService orderDetailService;
	
	
	@Test
	public void testqryStudent(){
		List<Student> students = studentService.findStudents(null);
		System.out.println("students==" + students);
	}
	
	@Test
	public void testaddOrder(){
		Order order = new Order();
		order.setName("商品");
		order.setOrderNo("666666");
		order.setPrice(12.3);
		boolean flag = orderService.addOrder(order);
		System.out.println("flag==" + flag);
	}
	
	@Test
	public void testqryOrder(){
		List<Order> order = orderService.findOrders(null);
		System.out.println("order==" + order);
	}
	
	@Test
	public void testaddOrderDetail(){
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(456);
		orderDetail.setGoodsId(789);
		orderDetail.setGoodsNum("56");
		boolean flag = orderDetailService.addOrderDeatil(orderDetail);
		System.out.println("flag==" + flag);
	}
	
	@Test
	public void testqryOrderDetail(){
		List<OrderDetail> orderDetail = orderDetailService.findOrderDetail(null);
		System.out.println("order==" + orderDetail);
	}

}
