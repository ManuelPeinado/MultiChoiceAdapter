package com.tjeannin.provigen;

import java.util.ArrayList;

import android.net.Uri;

class ContractHolderList extends ArrayList<ContractHolder> {

	private static final long serialVersionUID = 2449365634235483191L;

	/**
	 * @return The sum of all the version values of each {@link ContractHolder}.
	 */
	public int getVersionSum() {
		int versionSum = 0;
		for (ContractHolder contract : this) {
			versionSum += contract.getVersion();
		}
		return versionSum;
	}

	/**
	 * @param uri The {@link Uri} to be matched.
	 * @return A {@link ContractHolder} matching the given {@link Uri}.
	 */
	public ContractHolder findMatching(Uri uri) {
		for (ContractHolder contract : this) {
			if (contract.getTable().equals(uri.getPathSegments().get(0))) {
				return contract;
			}
		}
		return null;
	}

}
