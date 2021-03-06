
package org.osgi.impl.service.resourcemonitoring.bundlemanagement;

/**
 *
 */
public class BundleLock {

	private boolean	isLocked;

	/**
	 * Acquire bundle lock. This method blocks until the bundle lock is
	 * acquired.
	 */
	public void acquireLock() {
		boolean acquired = false;

		synchronized (this) {
			while (!acquired) {
				if (!isLocked) {
					isLocked = true;
					acquired = true;
					return;
				}

				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Release the lock.
	 */
	public void releaseLock() {
		synchronized (this) {
			isLocked = false;
			notifyAll();
		}
	}

}
