package com.tjeannin.provigen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.content.ContentProvider;

/**
 * Defines the {@link ContentUri} that should be used to query the {@link ContentProvider}.<br/>
 * The last path segment will be used for the database table name.<br/>
 * The authority of this {@link ContentUri} will be used for the {@link ContentProvider}'s authority.
 * <br/><br/>
 * Example:
 * <br/>
 * <code>
 * content://some.authority/table_name
 * </code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ContentUri {
}
