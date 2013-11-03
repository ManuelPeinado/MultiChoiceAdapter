package com.tjeannin.provigen;

import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.Contract;
import com.tjeannin.provigen.annotation.Id;

/**
 * Base interface for a {@link ProviGenProvider} {@link Contract}.
 */
public interface ProviGenBaseContract {

	/**
	 * The unique ID for a row.
	 */
	@Id
	@Column(Type.INTEGER)
	public static final String _ID = "_id";

}
