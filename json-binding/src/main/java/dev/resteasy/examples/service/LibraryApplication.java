package dev.resteasy.examples.service;

import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@ApplicationPath("/")
public class LibraryApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(Library.class);
    }
}
