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

package org.gitana.repo.client.types;

import org.codehaus.jackson.node.ObjectNode;
import org.gitana.repo.client.Branch;
import org.gitana.repo.client.Driver;
import org.gitana.repo.client.Response;
import org.gitana.repo.client.nodes.Association;
import org.gitana.repo.client.nodes.Node;
import org.gitana.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author uzi
 */
public class TypeDefinitionImpl extends AbstractDefinitionImpl implements TypeDefinition
{
    public TypeDefinitionImpl(Driver driver, Branch branch, ObjectNode obj, boolean isSaved)
    {
        super(driver, branch, obj, isSaved);
    }

    private Map<String, HasFormAssociation> _convert(Map<String, Association> map)
    {
        Map<String, HasFormAssociation> converted = new HashMap<String, HasFormAssociation>();
        for (Association association : map.values())
        {
            converted.put(association.getId(), (HasFormAssociation) association);
        }

        return converted;
    }

    @Override
    public Map<String, HasFormAssociation> fetchFormAssociations()
    {
        Response response = getRemote().get("/repositories/" + getRepositoryId() + "/branches/" + getBranchId() + "/definitions/" + getQName().toString() + "/forms");

        Map<String, Association> associations = getFactory().associations(getBranch(), response);
        return _convert(associations);
    }

    @Override
    public List<HasFormAssociation> listFormAssociations()
    {
        Map<String, HasFormAssociation> map = fetchFormAssociations();

        List<HasFormAssociation> list = new ArrayList<HasFormAssociation>();
        for (HasFormAssociation association : map.values())
        {
            list.add(association);
        }

        return list;
    }

    @Override
    public Form readForm(String formKey)
    {
        Form form = null;

        try
        {
            Response response = getRemote().get("/repositories/" + getRepositoryId() + "/branches/" + getBranchId() + "/definitions/" + getQName().toString() + "/forms/" + formKey);
            form = (Form) getFactory().node(getBranch(), response);
        }
        catch (Exception ex)
        {
            // swallow for the time being
            // TODO: the remote layer needs to hand back more interesting more interesting
            // TODO: information so that we can detect a proper 404
        }

        return form;
    }

    @Override
    public Form createForm(String formKey)
    {
        return createForm(formKey, JsonUtil.createObject());
    }

    @Override
    public Form createForm(String formKey, ObjectNode object)
    {
        // create the form
        object.put(Node.FIELD_TYPE_QNAME, Form.QNAME.toString());
        Form form = (Form) getBranch().createNode(object);

        // create the association
        HasFormAssociation association = (HasFormAssociation) associate(form, HasFormAssociation.QNAME);
        association.set(HasFormAssociation.FIELD_FORM_KEY, formKey);
        association.update();

        return form;
    }

    @Override
    public void deleteForm(String formKey)
    {
        Form form = readForm(formKey);
        if (form == null)
        {
            throw new RuntimeException("Unable to find form for form-key: " + formKey + " for node: " + getId());
        }

        form.delete();
    }

}
