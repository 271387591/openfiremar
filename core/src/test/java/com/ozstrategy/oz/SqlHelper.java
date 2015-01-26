package com.ozstrategy.oz;


public class SqlHelper<T extends BaseEntity> {

	public String insert(T entity) {
		return entity.returnInsertSql();
	}
	public String update(T entity) {
		return entity.returnInsertSql();
	}
	public String delete(T entity) {
		return entity.returnInsertSql();
	}
	
	
}
