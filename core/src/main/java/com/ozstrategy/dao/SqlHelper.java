package com.ozstrategy.dao;

import com.ozstrategy.model.BaseEntity;

public class SqlHelper<T extends BaseEntity> {

	public String save(T entity) {
		return entity.returnInsertSql();
	}
	
}
