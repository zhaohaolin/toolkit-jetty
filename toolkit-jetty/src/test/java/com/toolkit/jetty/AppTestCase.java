/*
 * CopyRight (c) 2005-2012 GLOBE Co, Ltd. All rights reserved.
 * Filename:    AppTestCase.java
 * Creator:     qiaofeng
 * Create-Date: 上午10:43:42
 */
package com.toolkit.jetty;

import junit.framework.Assert;

import org.junit.Test;

/**
 * TODO
 * 
 * @author qiaofeng
 * @version $Id: AppTestCase, v 0.1 2012-10-13 上午10:43:42 Exp $
 */
public class AppTestCase {
	
	@Test
	public void test() {
		Assert.assertNotNull(EmbbedJetty.class);
	}
	
}
