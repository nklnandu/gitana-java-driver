/**
 * Copyright 2010 Gitana Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more information, please contact Gitana Software, Inc. at this
 * address:
 *
 *   info@gitanasoftware.com
 */

package org.gitana.repo.client;

import java.util.List;
import java.util.Map;

/**
 * @author uzi
 */
public interface SecurityGroup extends SecurityPrincipal
{
    // CHILDREN

    public Map<String, SecurityPrincipal> fetchPrincipals();

    public Map<String, SecurityPrincipal> fetchPrincipals(boolean includeInherited);

    public List<SecurityPrincipal> listPrincipals();

    public List<SecurityPrincipal> listPrincipals(boolean includeInherited);


    // ADD / REMOVE CHILDREN

    public void addPrincipal(SecurityPrincipal principal);

    public void addPrincipal(String principalId);

    public void removePrincipal(SecurityPrincipal principal);

    public void removePrincipal(String principalId);
}