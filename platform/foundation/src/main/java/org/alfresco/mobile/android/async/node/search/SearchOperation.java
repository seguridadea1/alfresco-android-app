/*
 *  Copyright (C) 2005-2016 Alfresco Software Limited.
 *
 *  This file is part of Alfresco Mobile for Android.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.alfresco.mobile.android.async.node.search;

import org.alfresco.mobile.android.api.model.KeywordSearchOptions;
import org.alfresco.mobile.android.api.model.Node;
import org.alfresco.mobile.android.api.model.PagingResult;
import org.alfresco.mobile.android.api.model.SearchLanguage;
import org.alfresco.mobile.android.async.LoaderResult;
import org.alfresco.mobile.android.async.OperationAction;
import org.alfresco.mobile.android.async.OperationsDispatcher;
import org.alfresco.mobile.android.async.Operator;
import org.alfresco.mobile.android.async.impl.ListingOperation;
import org.alfresco.mobile.android.platform.EventBusManager;

import android.util.Log;

public class SearchOperation extends ListingOperation<PagingResult<Node>>
{
    private static final String TAG = SearchOperation.class.getName();

    private String keywords;

    private KeywordSearchOptions sp;

    private String statement;

    private SearchLanguage language;

    // ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // ///////////////////////////////////////////////////////////////////////////
    public SearchOperation(Operator operator, OperationsDispatcher dispatcher, OperationAction action)
    {
        super(operator, dispatcher, action);
        if (request instanceof SearchRequest)
        {
            this.keywords = ((SearchRequest) request).keywords;
            this.sp = ((SearchRequest) request).sp;
            this.statement = ((SearchRequest) request).statement;
            this.language = ((SearchRequest) request).language;
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    // ///////////////////////////////////////////////////////////////////////////
    protected LoaderResult<PagingResult<Node>> doInBackground()
    {
        try
        {
            super.doInBackground();

            LoaderResult<PagingResult<Node>> result = new LoaderResult<PagingResult<Node>>();
            PagingResult<Node> pagingResult = null;

            try
            {
                String[] keyw = keywords.split(" ");
                if (keyw.length == 2 && keyw[0].equals("parte") && keyw[1].matches("[0-9]+")){
                    System.out.println("BUSQUEDA DE PARTE");
                    String nParte = keyw[1];
                    pagingResult = session.getServiceRegistry().getSearchService()
                            .search("SELECT\n" +
                                    "d.alfcmis:nodeRef as \"alfcmis:nodeRef\",\n" +
                                    "d.cmis:isImmutable as \"cmis:isImmutable\",\n" +
                                    "d.cmis:versionLabel as \"cmis:versionLabel\",\n" +
                                    "d.cmis:objectTypeId as \"cmis:objectTypeId\",\n" +
                                    "d.cmis:description as \"cmis:description\",\n" +
                                    "d.cmis:createdBy as \"cmis:createdBy\",\n" +
                                    "d.cmis:checkinComment as \"cmis:checkinComment\",\n" +
                                    "d.cmis:creationDate as \"cmis:creationDate\",\n" +
                                    "d.cmis:isMajorVersion as \"cmis:isMajorVersion\",\n" +
                                    "d.cmis:contentStreamFileName as \"cmis:contentStreamFileName\",\n" +
                                    "d.cmis:name as \"cmis:name\",\n" +
                                    "d.cmis:isLatestVersion as \"cmis:isLatestVersion\",\n" +
                                    "d.cmis:lastModificationDate as \"cmis:lastModificationDate\",\n" +
                                    "d.cmis:contentStreamLength as \"cmis:contentStreamLength\",\n" +
                                    "d.cmis:objectId as \"cmis:objectId\",\n" +
                                    "d.cmis:lastModifiedBy as \"cmis:lastModifiedBy\",\n" +
                                    "d.cmis:contentStreamId as \"cmis:contentStreamId\",\n" +
                                    "d.cmis:contentStreamMimeType as \"cmis:contentStreamMimeType\",\n" +
                                    "d.cmis:baseTypeId as \"cmis:baseTypeId\",\n" +
                                    "d.cmis:changeToken as \"cmis:changeToken\",\n" +
                                    "d.cmis:isPrivateWorkingCopy as \"cmis:isPrivateWorkingCopy\",\n" +
                                    "d.cmis:versionSeriesCheckedOutBy as \"cmis:versionSeriesCheckedOutBy\",\n" +
                                    "d.cmis:isVersionSeriesCheckedOut as \"cmis:isVersionSeriesCheckedOut\",\n" +
                                    "d.cmis:versionSeriesId as \"cmis:versionSeriesId\",\n" +
                                    "d.cmis:isLatestMajorVersion as \"cmis:isLatestMajorVersion\",\n" +
                                    "d.cmis:versionSeriesCheckedOutId as \"cmis:versionSeriesCheckedOutId\"\n" +
                                    "FROM a1:de_x002d_parte AS p JOIN cmis:document AS d\n" +
                                    "ON p.cmis:objectId = d.cmis:objectId\n" +
                                    "WHERE p.a1:CodParte = '"+nParte+"'", SearchLanguage.CMIS, listingContext);
                    //pagingResult = session.getServiceRegistry().getSearchService().search("SELECT * FROM cmis:document where cmis:objectId in ('cc9fd6a2-e545-4301-bf2f-00296a75eb81')", SearchLanguage.CMIS, listingContext);
                } else if (keywords != null)
                {
                    pagingResult = session.getServiceRegistry().getSearchService()
                            .keywordSearch(keywords, sp, listingContext);
                }
                else if (statement != null)
                {
                    pagingResult = session.getServiceRegistry().getSearchService()
                            .search(statement, language, listingContext);
                }
            }
            catch (Exception e)
            {
                result.setException(e);
            }

            result.setData(pagingResult);

            return result;
        }
        catch (Exception e)
        {
            Log.w(TAG, Log.getStackTraceString(e));
        }
        return new LoaderResult<PagingResult<Node>>();
    }

    @Override
    protected void onPostExecute(LoaderResult<PagingResult<Node>> result)
    {
        super.onPostExecute(result);

        EventBusManager.getInstance().post(new SearchEvent(getRequestId(), result));
    }
}
