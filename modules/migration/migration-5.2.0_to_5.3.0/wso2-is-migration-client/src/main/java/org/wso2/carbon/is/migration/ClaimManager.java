/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.is.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.metadata.mgt.dao.ClaimDialectDAO;
import org.wso2.carbon.identity.claim.metadata.mgt.util.ClaimConstants;
import org.wso2.carbon.is.migration.bean.Claim;
import org.wso2.carbon.is.migration.dao.ClaimDAO;

import java.util.List;

/**
 * Claim Manager
 */
public class ClaimManager {

    private static Log log = LogFactory.getLog(ClaimDialectDAO.class);
    private ClaimDAO claimDAO = ClaimDAO.getInstance();
    private static ClaimManager claimManager = new ClaimManager();

    private ClaimManager() {
    }

    public static ClaimManager getInstance() {
        return claimManager;
    }

    /**
     * Adding claim dialects
     *
     * @param claimList
     * @throws ISMigrationException
     */
    public void addClaimDialects(List<Claim> claimList) throws ISMigrationException {
        log.info("started adding claim dialects");

        for (Claim claim : claimList) {
            int id = claimDAO.getClaimDialect(claim.getDialectURI(), claim.getTenantId());
            if (id == 0) {
                claimDAO.addClaimDialect(claim.getDialectURI(), claim.getTenantId());
            }
        }

        log.info("end adding claim dialects");
    }

    /**
     * Adding local claims
     *
     * @param claimCList
     * @throws ISMigrationException
     */
    public void addLocalClaims(List<Claim> claimCList) throws ISMigrationException {
        log.info("started adding local claims");

        for (Claim claim : claimCList) {
            if (ClaimConstants.LOCAL_CLAIM_DIALECT_URI.equalsIgnoreCase(claim.getDialectURI())) {
                claimDAO.addLocalClaim(claim);
            }
        }

        log.info("end adding local claims");
    }

    /**
     * Adding external claims
     *
     * @param claims
     * @throws ISMigrationException
     */
    public void addExternalClaim(List<Claim> claims) throws ISMigrationException {
        log.info("started adding external claims");

        for (Claim claim : claims) {
            if (!ClaimConstants.LOCAL_CLAIM_DIALECT_URI.equalsIgnoreCase(claim.getDialectURI())) {
                claimDAO.addExternalClaim(claim);
            }
        }
        log.info("end adding external claims");
    }
}
