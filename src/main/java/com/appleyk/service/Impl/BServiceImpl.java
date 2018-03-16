package com.appleyk.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.appleyk.entity.B;
import com.appleyk.mapper.slave.BMapepr;
import com.appleyk.service.BService;

@Service
@Primary
public class BServiceImpl implements BService {

	@Autowired
	private BMapepr bMapper;

	@Override
	public boolean SaveB(B b) throws Exception{

		int count = bMapper.insert(b);
		
		if(b.getName().length()>5){
			System.err.println("B事务回滚");
			throw new Exception("名称超过5");
		}		
		System.err.println("B事务提交");
		return  count >0;
		
	}

}
