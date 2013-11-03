package com.tjeannin.provigen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ProviGenOpenHelper extends SQLiteOpenHelper {

	private ProviGenProvider provigenProvider;

	ProviGenOpenHelper(Context context, ProviGenProvider proviGenProvider, String databaseName, int version) {
		super(context, databaseName, null, version);
		this.provigenProvider = proviGenProvider;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		provigenProvider.onCreateDatabase(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		provigenProvider.onUpgradeDatabase(database, oldVersion, newVersion);
	}

	void createTable(SQLiteDatabase database, ContractHolder contractHolder) {

		// CREATE TABLE myTable (
		StringBuilder builder = new StringBuilder("CREATE TABLE ");
		builder.append(contractHolder.getTable() + " ( ");

		// myInt INTEGER, myString TEXT
		for (int i = 0; i < contractHolder.getFields().size(); i++) {
			DatabaseField field = contractHolder.getFields().get(i);
			builder.append(" " + field.getName() + " " + field.getType());
			if (field.getName().equals(contractHolder.getIdField())) {
				builder.append(" PRIMARY KEY AUTOINCREMENT ");
			}
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 2);

		if (contractHolder.hasUniqueDatabaseFields()) {

			// , UNIQUE ( myInt, myString ) ON CONFLICT REPLACE
			builder.append(" , UNIQUE( ");
			String onConflict = null;
			for (int i = 0; i < contractHolder.getFields().size(); i++) {
				DatabaseField field = contractHolder.getFields().get(i);
				if (field.isUnique()) {
					builder.append(field.getName() + ", ");
					onConflict = field.getOnConflict();
				}
			}
			builder.deleteCharAt(builder.length() - 2);
			builder.append(" ) ON CONFLICT " + onConflict);
		}

		// )
		builder.append(" ) ");

		database.execSQL(builder.toString());
	}

	void addMissingColumnsInTable(SQLiteDatabase database, ContractHolder contractHolder) {

		Cursor cursor = database.rawQuery("PRAGMA table_info(" + contractHolder.getTable() + ")", null);
		for (DatabaseField field : contractHolder.getFields()) {
			if (!fieldExistAsColumn(field.getName(), cursor)) {
				database.execSQL("ALTER TABLE " + contractHolder.getTable() + " ADD COLUMN " + field.getName() + " " + field.getType() + ";");
			}
		}
	}

	public boolean hasTableInDatabase(SQLiteDatabase database, ContractHolder contractHolder) {

		Cursor cursor = database.rawQuery(
				"SELECT * FROM sqlite_master WHERE name = ? ",
				new String[] { contractHolder.getTable() });
		boolean exists = cursor.getCount() != 0;
		cursor.close();
		return exists;
	}

	private boolean fieldExistAsColumn(String field, Cursor columnCursor) {
		if (columnCursor.moveToFirst()) {
			do {
				if (field.equals(columnCursor.getString(1))) {
					return true;
				}
			} while (columnCursor.moveToNext());
		}
		return false;
	}

}
