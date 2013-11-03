package com.tjeannin.provigen;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.tjeannin.provigen.annotation.Contract;

/**
 * Behaves as a {@link ContentProvider} for the given {@link Contract} class.
 */
public class ProviGenProvider extends ContentProvider {

	private ContractHolderList contracts = new ContractHolderList();
	private String databaseName = "ProviGenDatabase";
	private ProviGenOpenHelper openHelper;

	private UriMatcher uriMatcher;
	private static final int ITEM = 1;
	private static final int ITEM_ID = 2;

	/**
	 * @param contractClass A {@link Contract} class to build the {@link ContentProvider} with.
	 * @throws InvalidContractException
	 */
	@SuppressLint("Registered")
	@SuppressWarnings("rawtypes")
	public ProviGenProvider(Class contractClass) throws InvalidContractException {
		contracts.add(new ContractHolder(contractClass));
	}

	/**
	 * @param contractClass A {@link Contract} class to build the {@link ContentProvider} with.
	 * @param databaseName The name of the database to work with.
	 * @throws InvalidContractException
	 */
	@SuppressLint("Registered")
	@SuppressWarnings("rawtypes")
	public ProviGenProvider(Class contractClass, String databaseName) throws InvalidContractException {
		this(contractClass);
		this.databaseName = databaseName;
	}

	/**
	 * @param contractClasses An array of {@link Contract} classes to build the {@link ContentProvider} with.
	 * @throws InvalidContractException
	 */
	@SuppressLint("Registered")
	@SuppressWarnings("rawtypes")
	public ProviGenProvider(Class[] contractClasses) throws InvalidContractException {
		for (int i = 0; i < contractClasses.length; i++) {
			contracts.add(new ContractHolder(contractClasses[i]));
		}
	}

	/**
	 * @param contractClasses An array of {@link Contract} classes to build the {@link ContentProvider} with.
	 * @param databaseName The name of the database to work with.
	 * @throws InvalidContractException
	 */
	@SuppressLint("Registered")
	@SuppressWarnings("rawtypes")
	public ProviGenProvider(Class[] contractClasses, String databaseName) throws InvalidContractException {
		this(contractClasses);
		this.databaseName = databaseName;
	}

	@Override
	public boolean onCreate() {

		openHelper = new ProviGenOpenHelper(getContext(), this, databaseName, contracts.getVersionSum());

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		for (ContractHolder contract : contracts) {
			uriMatcher.addURI(contract.getAuthority(), contract.getTable(), ITEM);
			uriMatcher.addURI(contract.getAuthority(), contract.getTable() + "/#", ITEM_ID);
		}

		return true;
	}

	/**
	 * Called when the database is created for the first time. </br>
	 * The {@link ProviGenProvider} automatically creates database tables and the needed columns
	 * if {@code super.onCreateDatabase(database)} is called.</br>
	 * The initial population of the tables should happen here.
	 * @param database The database.
	 */
	public void onCreateDatabase(SQLiteDatabase database) {
		for (ContractHolder contract : contracts) {
			openHelper.createTable(database, contract);
		}
	}

	/**
	 * Creates a table in the database for the specified {@link Contract}.<br/>
	 * This may be used if you're <b>not</b> calling {@code super.onCreateDatabase(database)}.
	 * @param database The database.
	 * @param contractClass A {@link Contract} class to create the table with.
	 */
	@SuppressWarnings("rawtypes")
	public void createTable(SQLiteDatabase database, Class contractClass) {
		try {
			openHelper.createTable(database, new ContractHolder(contractClass));
		} catch (InvalidContractException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Called when the database needs to be upgraded. </br></br>
	 * Call {@code super.onUpgradeDatabase(database, oldVersion, newVersion)} to:
	 * <ul>
	 * <li>Automatically add columns if some are missing.</li>
	 * <li>Automatically create tables and needed columns for new added {@link Contract}s.</li>
	 * </ul>
	 * Anything else related to database upgrade should be done here.
	 * <p>
	 * This method executes within a transaction. If an exception is thrown, all changes
	 * will automatically be rolled back.
	 * </p>
	 * @param database The database.
	 * @param oldVersion The old database version (same as contract old version).
	 * @param newVersion The new database version (same as contract new version).
	 */
	public void onUpgradeDatabase(SQLiteDatabase database, int oldVersion, int newVersion) {
		for (ContractHolder contract : contracts) {
			if (!openHelper.hasTableInDatabase(database, contract)) {
				openHelper.createTable(database, contract);
			} else {
				openHelper.addMissingColumnsInTable(database, contract);
			}
		}
	}

	/**
	 * Adds missing table columns for the specified {@link Contract}.<br/>
	 * This may be used if you're <b>not</b> calling {@code super.onCreateDatabase(database)}.
	 * @param database The database.
	 * @param contractClass A {@link Contract} class to use to create missing columns.
	 */
	@SuppressWarnings("rawtypes")
	public void addMissingColumnsInTable(SQLiteDatabase database, Class contractClass) {
		try {
			openHelper.addMissingColumnsInTable(database, new ContractHolder(contractClass));
		} catch (InvalidContractException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Used to switch from a {@link Contract} to an other dynamically.<br/>
	 * Note that this may change the database schema.
	 * May be used for testing purpose. <br/>
	 * @param contractClass The {@link Contract} to switch to.
	 * @throws InvalidContractException
	 */
	@SuppressWarnings("rawtypes")
	public void setContractClasses(Class[] contractClasses) throws InvalidContractException {
		contracts.clear();
		for (int i = 0; i < contractClasses.length; i++) {
			contracts.add(new ContractHolder(contractClasses[i]));
		}
		onCreate();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase database = openHelper.getWritableDatabase();

		int numberOfRowsAffected = 0;
		ContractHolder contractHolder = contracts.findMatching(uri);

		switch (uriMatcher.match(uri)) {
		case ITEM:
			numberOfRowsAffected = database.delete(contractHolder.getTable(), selection, selectionArgs);
			break;
		case ITEM_ID:
			String itemId = String.valueOf(ContentUris.parseId(uri));

			if (TextUtils.isEmpty(selection)) {
				numberOfRowsAffected = database.delete(contractHolder.getTable(), contractHolder.getIdField() + " = ? ", new String[] { itemId });
			} else {
				numberOfRowsAffected = database.delete(contractHolder.getTable(), selection + " AND " +
						contractHolder.getIdField() + " = ? ", appendToStringArray(selectionArgs, itemId));
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown uri " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return numberOfRowsAffected;
	}

	@Override
	public String getType(Uri uri) {

		ContractHolder contractHolder = contracts.findMatching(uri);

		switch (uriMatcher.match(uri)) {
		case ITEM:
			return "vnd.android.cursor.dir/vdn." + contractHolder.getTable();
		case ITEM_ID:
			return "vnd.android.cursor.item/vdn." + contractHolder.getTable();
		default:
			throw new IllegalArgumentException("Unknown uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase database = openHelper.getWritableDatabase();

		ContractHolder contractHolder = contracts.findMatching(uri);
		switch (uriMatcher.match(uri)) {
		case ITEM:
			long itemId = database.insert(contractHolder.getTable(), null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.withAppendedPath(uri, String.valueOf(itemId));
		default:
			throw new IllegalArgumentException("Unknown uri " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase database = openHelper.getReadableDatabase();

		ContractHolder contractHolder = contracts.findMatching(uri);
		Cursor cursor = null;

		switch (uriMatcher.match(uri)) {
		case ITEM:
			cursor = database.query(contractHolder.getTable(), projection, selection, selectionArgs, "", "", sortOrder);
			break;
		case ITEM_ID:
			String itemId = String.valueOf(ContentUris.parseId(uri));
			if (TextUtils.isEmpty(selection)) {
				cursor = database.query(contractHolder.getTable(), projection, contractHolder.getIdField() + " = ? ", new String[] { itemId }, "", "", sortOrder);
			} else {
				cursor = database.query(contractHolder.getTable(), projection, selection + " AND " + contractHolder.getIdField() + " = ? ",
						appendToStringArray(selectionArgs, itemId), "", "", sortOrder);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown uri " + uri);
		}

		// Make sure that potential listeners are getting notified.
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase database = openHelper.getWritableDatabase();

		ContractHolder contractHolder = contracts.findMatching(uri);
		int numberOfRowsAffected = 0;

		switch (uriMatcher.match(uri)) {
		case ITEM:
			numberOfRowsAffected = database.update(contractHolder.getTable(), values, selection, selectionArgs);
			break;
		case ITEM_ID:
			String itemId = String.valueOf(ContentUris.parseId(uri));

			if (TextUtils.isEmpty(selection)) {
				numberOfRowsAffected = database.update(contractHolder.getTable(), values, contractHolder.getIdField() + " = ? ", new String[] { itemId });
			} else {
				numberOfRowsAffected = database.update(contractHolder.getTable(), values, selection + " AND " + contractHolder.getIdField() + " = ? ",
						appendToStringArray(selectionArgs, itemId));
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown uri " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return numberOfRowsAffected;
	}

	/**
	 * Appends the given element to a copy of the given array.
	 * @param array The array to copy.
	 * @param element The element to append.
	 * @return An Array with the element appended.
	 */
	private static String[] appendToStringArray(String[] array, String element) {
		if (array != null) {
			String[] newArray = new String[array.length + 1];
			for (int i = 0; i < array.length; i++) {
				newArray[i] = array[i];
			}
			newArray[array.length] = element;
			return newArray;
		} else {
			return new String[] { element };
		}
	}

}
