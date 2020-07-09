/*
 *
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 */

package org.entando.kubernetes.model.interprocesstest;

import io.fabric8.kubernetes.api.model.LoadBalancerStatus;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.entando.kubernetes.model.AbstractEntandoKeycloakServerTest;
import org.entando.kubernetes.model.DbServerStatus;
import org.entando.kubernetes.model.EntandoDeploymentPhase;
import org.entando.kubernetes.model.WebServerStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Tags({@Tag("inter-process"),@Tag("pre-deployment") })
public class EntandoKeycloakServerIntegratedTest extends AbstractEntandoKeycloakServerTest {

    private final KubernetesClient client = new AutoAdaptableKubernetesClient();

    @Override
    public KubernetesClient getClient() {
        return client;
    }

    @Test
    public void multipleStatuses() {
        super.testCreateEntandoKeycloakServer();
        keycloakServers().inNamespace(MY_NAMESPACE).withName(MY_KEYCLOAK).edit().withPhase(EntandoDeploymentPhase.STARTED).done();
        DbServerStatus dbStatus = new DbServerStatus("db-qualifier");
        dbStatus.setPodStatus(new PodStatus());
        keycloakServers().inNamespace(MY_NAMESPACE).withName(MY_KEYCLOAK).edit().withStatus(dbStatus).done();
        WebServerStatus status1 = new WebServerStatus("server");
        keycloakServers().inNamespace(MY_NAMESPACE).withName(MY_KEYCLOAK).edit().withStatus(status1).done();
        status1.setServiceStatus(new ServiceStatus(new LoadBalancerStatus()));
        keycloakServers().inNamespace(MY_NAMESPACE).withName(MY_KEYCLOAK).edit().withStatus(status1).done();
    }

}