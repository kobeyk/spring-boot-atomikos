package com.appleyk.service.Impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appleyk.entity.A;
import com.appleyk.entity.B;
import com.appleyk.service.AService;
import com.appleyk.service.BService;
import com.appleyk.service.ObjectService;

@Service
@Primary
public class ObjectServiceImpl implements ObjectService {

	@Autowired
	private AService aService;

	@Autowired
	private BService bService;

	@Override
	@Transactional(rollbackFor = { Exception.class, SQLException.class })
	public boolean Save(A a) throws Exception {

		if (!aService.SaveA(a)) {
			return false;
		}

		//int i = 1 / 0;

		B b = new B(a);

		try {
			if (!bService.SaveB(b)) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("A事务回滚");
			throw new Exception("我的错，保存B异常");
		}

		System.err.println("A事务提交");
		return true;

	}

}
