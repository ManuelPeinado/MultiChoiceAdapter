package com.manuelpeinado.multichoiceadapter.demo.simplecursoradaptersample;

import com.tjeannin.provigen.InvalidContractException;
import com.tjeannin.provigen.ProviGenProvider;

public class BuildingsContentProvider extends ProviGenProvider {

    public BuildingsContentProvider() throws InvalidContractException {
        super(BuildingsContract.class);
    }
    
}