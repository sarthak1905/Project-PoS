package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public void add(UserPojo userPojo) throws ApiException {
		normalize(userPojo);
		UserPojo existing = userDao.select(userPojo.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		userDao.insert(userPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return userDao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return userDao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		userDao.delete(id);
	}

	protected static void normalize(UserPojo userPojo) {
		userPojo.setEmail(userPojo.getEmail().toLowerCase().trim());
		userPojo.setRole(userPojo.getRole().toLowerCase().trim());
	}
}
