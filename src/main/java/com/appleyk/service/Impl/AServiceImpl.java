package com.appleyk.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.appleyk.entity.A;
import com.appleyk.mapper.master.AMapepr;
import com.appleyk.service.AService;

@Service
@Primary
public class AServiceImpl implements AService {

	@Autowired
	private AMapepr aMapper;

	@Override
	public boolean SaveA(A a) {

		return aMapper.insert(a) > 0;
	}

}
