package org.keycloak.dashboard.beans.filters;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class PriorityFilter extends LabelFilter {

    public PriorityFilter(String... priorities) {
        super(Stream.of(priorities).map(p -> "priority/" + p).collect(Collectors.toSet()));
    }

}
