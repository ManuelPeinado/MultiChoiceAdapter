package com.manuelpeinado.multichoiceadapter.demo.dbsample;

import com.tjeannin.provigen.InvalidContractException;
import com.tjeannin.provigen.ProviGenProvider;

public class BuildingsContentProvider extends ProviGenProvider {

    public BuildingsContentProvider() throws InvalidContractException {
        super(BuildingsContract.class);
    }
    
}