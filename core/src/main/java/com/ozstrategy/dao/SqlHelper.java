package com.ozstrategy.dao;

import com.ozstrategy.model.BaseEntity;

public class SqlHelper<T extends BaseEntity> {
	public String insert(T entity) {
		return entity.returnInsertSql();
	}
	public String update(T entity) {
		
		Class cl = entity.getClass();
		
		
		return entity.returnUpdateSql();
	}
	public String delete(T entity) {
		return entity.returnDeleteSql();
	}
	
	public String merge(Object[] objects){
		T t1=(T)objects[0];
		Class cl=(Class)objects[1];
		Class cl1 = objects[1].getClass();
		String slq="";
		System.out.println("sdfdsf");
		return slq;
	}


}
