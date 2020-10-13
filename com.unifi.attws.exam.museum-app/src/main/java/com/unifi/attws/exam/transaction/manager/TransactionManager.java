package com.unifi.attws.exam.transaction.manager;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code);

}
