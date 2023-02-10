package com.increff.pos.dao;

import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserDao extends AbstractDao {

	private static final String DELETE_ID = "delete from UserPojo p where id=:id";
	private static final String SELECT_ID = "select p from UserPojo p where id=:id";
	private static final String SELECT_EMAIL = "select p from UserPojo p where email=:email";
	private static final String SELECT_ALL = "select p from UserPojo p";


	public void insert(UserPojo userPojo) {
		em().persist(userPojo);
	}

	public int delete(int id) {
		Query query = em().createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public UserPojo select(int id) {
		TypedQuery<UserPojo> query = getQuery(SELECT_ID, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(SELECT_ALL, UserPojo.class);
		return query.getResultList();
	}


}
