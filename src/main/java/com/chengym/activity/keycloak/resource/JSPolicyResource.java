/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chengym.activity.keycloak.resource;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.representations.idm.authorization.JSPolicyRepresentation;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;

import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.*;
/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
public interface JSPolicyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    JSPolicyRepresentation toRepresentation();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void update(JSPolicyRepresentation representation);

    @DELETE
    void remove();

    @Path("/associatedPolicies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    List<PolicyRepresentation> associatedPolicies();

    @Path("/dependentPolicies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    List<PolicyRepresentation> dependentPolicies();

    @Path("/resources")
    @GET
    @Produces("application/json")
    @NoCache
    List<ResourceRepresentation> resources();

}
