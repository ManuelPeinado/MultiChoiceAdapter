package com.tjeannin.provigen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tjeannin.provigen.ProviGenProvider;

import android.content.ContentProvider;

/**
 * Identifies a class representing a {@link ContentProvider} contract.<br/>
 * You should <b>increase the contract version each time you modify it.</b><br/>
 * On contract version increased, the {@link ProviGenProvider} will makes the necessary changes to the underlying database.<br/>
 * A {@link Contract} class should have:
 * <ul>
 * <li>One unique {@link ContentUri} annotated field.</li>
 * <li>One unique {@link Id} annotated field.</li>
 * <li>At least one {@link Column} annotated field.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Contract {
	int version();
}