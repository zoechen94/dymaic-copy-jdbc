/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2017, xsjt- All Rights Reserved.
 * Project Name:springboot-test  
 * File Name:IStudentDao.java  
 * Package Name:com.xsjt.dao
 * Author   Joe
 * Date:2017年11月7日上午9:22:59
 * ---------------------------------------------------------------------------  
*/  
  
package com.xsjt.dao;

import java.util.List;
import java.util.Map;

import com.xsjt.bean.Student;

/**  
 * ClassName:IStudentDao 
 * Date:     2017年11月7日 上午9:22:59
 * @author   Joe  
 * @version    
 * @since    JDK 1.8
 */
public interface IStudentDao {

	/**
	 * findStudents:(查询List).  
	 * @author Joe
	 * Date:2017年11月7日上午9:30:37
	 *
	 * @param map
	 * @return
	 */
	List<Student> findStudents(Map<String, Object> map);
	
	/**
	 * addStudent:(增加).  
	 * @author Joe
	 * Date:2017年11月7日上午9:30:48
	 *
	 * @param student
	 * @return
	 */
	boolean addStudent(Student student);
	
}
  
