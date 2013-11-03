package com.tjeannin.provigen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a field of a contract class that corresponds to a column in the database.</br>
 * The value attribute should be one of the following:
 * <ul>
 * <li>{@link Type#INTEGER}</li>
 * <li>{@link Type#BLOB}</li>
 * <li>{@link Type#REAL}</li>
 * <li>{@link Type#TEXT}</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	String value();

	public class Type {
		public static final String INTEGER = "INTEGER";
		public static final String REAL = "REAL";
		public static final String TEXT = "TEXT";
		public static final String BLOB = "BLOB";
	}
}
