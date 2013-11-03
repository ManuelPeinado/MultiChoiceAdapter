package com.manuelpeinado.multichoiceadapter.samples.stock.simplecursoradaptersample;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;
import com.tjeannin.provigen.annotation.Contract;

@Contract(version = 1)
public interface BuildingsContract extends ProviGenBaseContract {

    @Column(Type.TEXT)
    public static final String NAME = "name";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://com.manuelpeinado.multichoiceadapter.demo/buildings");
}