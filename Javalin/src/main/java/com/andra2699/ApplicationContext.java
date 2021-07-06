package com.andra2699;

import com.andra2699.data.DataRepositoryFactory;
import com.andra2699.data.entities.RESTEndpoint;
import lombok.Getter;

public class ApplicationContext {

    @Getter
    private final DataRepositoryFactory dataRepositoryFactory;

    @Getter
    private final RESTEndpoint restEndpoint;

    public ApplicationContext() {
        dataRepositoryFactory = new DataRepositoryFactory(this, "/home/andra/Documents/bd_licenta");

        restEndpoint = new RESTEndpoint(this);
    }
}
