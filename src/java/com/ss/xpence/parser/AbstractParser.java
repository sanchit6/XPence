package com.ss.xpence.parser;

import com.ss.xpence.model.AbstractModel;

public abstract class AbstractParser {

	public abstract AbstractModel<?> parse(String message);

	public abstract boolean canParse(String message, String accountNo, String cardNo);

}
