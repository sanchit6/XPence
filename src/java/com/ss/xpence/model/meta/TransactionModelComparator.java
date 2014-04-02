package com.ss.xpence.model.meta;

import java.util.Comparator;

import com.ss.xpence.model.TransactionModel;

public class TransactionModelComparator implements Comparator<TransactionModel> {

	public int compare(TransactionModel lhs, TransactionModel rhs) {
		return lhs.getDate().equals(rhs.getDate()) ? 0 : lhs.getDate().after(rhs.getDate()) ? -1 : 1;
	}

}
