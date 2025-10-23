package com.jjk.env_test.maintest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class RecursiveService {

	@Transactional
	public void recursiveMethod(int count) {

		TransactionSynchronizationManager.setCurrentTransactionName("MyCustomTransaction");

		if (count >= 5) {
			return;
		}
		System.out.println("Processing: " + count);
		// 這是直接調用
		recursiveMethod(count + 1);
	}
}
