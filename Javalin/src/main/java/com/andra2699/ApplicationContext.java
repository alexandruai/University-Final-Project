package com.andra2699;

import com.andra2699.data.DataRepositoryFactory;
import lombok.Getter;

public class ApplicationContext {

    @Getter
    private final DataRepositoryFactory dataRepositoryFactory;

    public ApplicationContext() {
        dataRepositoryFactory = new DataRepositoryFactory(this, "/home/andra/Documents/bd_licenta");
    }
}
