/*
 * Copyright (c) OSGi Alliance (2004, 2020). All Rights Reserved.
 * 
 * Implementation of certain elements of the OSGi Specification may be subject
 * to third party intellectual property rights, including without limitation,
 * patent rights (such a third party may or may not be a member of the OSGi
 * Alliance). The OSGi Alliance is not responsible and shall not be held
 * responsible in any manner for identifying or failing to identify any or all
 * such third party intellectual property rights.
 * 
 * This document and the information contained herein are provided on an "AS IS"
 * basis and THE OSGI ALLIANCE DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO ANY WARRANTY THAT THE USE OF THE INFORMATION
 * HEREIN WILL NOT INFRINGE ANY RIGHTS AND ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT WILL THE
 * OSGI ALLIANCE BE LIABLE FOR ANY LOSS OF PROFITS, LOSS OF BUSINESS, LOSS OF
 * USE OF DATA, INTERRUPTION OF BUSINESS, OR FOR DIRECT, INDIRECT, SPECIAL OR
 * EXEMPLARY, INCIDENTIAL, PUNITIVE OR CONSEQUENTIAL DAMAGES OF ANY KIND IN
 * CONNECTION WITH THIS DOCUMENT OR THE INFORMATION CONTAINED HEREIN, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH LOSS OR DAMAGE.
 * 
 * All Company, brand and product names may be trademarks that are the sole
 * property of their respective owners. All rights reserved.
 */

/*
 * REVISION HISTORY:
 *
 * Date          Author(s)
 * CR            Headline
 * ============  ==============================================================
 * Mar 02, 2005  Luiz Felipe Guimaraes
 * 11            Implement DMT Use Cases 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.support.compatibility.DefaultTestBundleControl;

import junit.framework.TestCase;

/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates the implementation of <code>rollback</code> method, 
 * according to MEG specification
 */
public class Rollback {

	private DmtTestControl tbc;

	public Rollback(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testRollback001();
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of rollback  
	 * to the correct plugin. 
	 * 
	 * @spec TransactionalDataSession.rollback()
	 */
	public void testRollback001() {
		DmtSession session = null;
		try {
			DefaultTestBundleControl.log("#testRollback001");

			session = tbc.getDmtAdmin().getSession(
					TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			TestDataPlugin.setRollbackThrowsException(true);
			session.rollback();
			DefaultTestBundleControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			TestCase
					.assertEquals(
							"Asserts that DmtAdmin fowarded the DmtException with the correct code: ",
							DmtException.ROLLBACK_FAILED, e.getCode());
			if (e.getCause() instanceof DmtException) {
				DmtException exception = (DmtException)e.getCause();
				TestCase
						.assertNull(
								"Asserts that DmtAdmin fowarded the DmtException with the correct subtree (null)",exception.getURI());
				TestCase
						.assertEquals(
								"Asserts that DmtAdmin fowarded the DmtException with the correct code: ",
								DmtException.ALERT_NOT_ROUTED, exception.getCode());
				TestCase
						.assertTrue(
								"Asserts that DmtAdmin fowarded the DmtException with the correct message. ",
								exception.getMessage().indexOf(
										TestDataPlugin.ROLLBACK) > -1);
				
			} else {
				DmtTestControl.failExpectedOtherException(DmtException.class, e.getCause());
			}
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
			TestDataPlugin.setRollbackThrowsException(false);
		}

	}
}
