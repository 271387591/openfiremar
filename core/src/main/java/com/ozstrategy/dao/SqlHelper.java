package com.ozstrategy.dao;

import com.ozstrategy.model.BaseEntity;

public class SqlHelper<T extends BaseEntity> {
	public String insert(T entity) {
		return entity.returnInsertSql();
	}
	public String update(T entity) {
		return entity.returnUpdateSql();
	}
	public String delete(T entity) {
		return entity.returnDeleteSql();
	}


}
