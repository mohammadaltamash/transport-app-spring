package com.transport.app.rest.domain;

import com.transport.app.rest.config.authenticationfacade.IAuthenticationFacade;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomRevisionEntityListener implements RevisionListener {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
        customRevisionEntity.setUserName(authenticationFacade.getAuthentication().getName());
    }
}
