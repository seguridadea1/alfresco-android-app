/*******************************************************************************
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco Mobile for Android.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.alfresco.mobile.android.ui;

public interface ListingTemplate
{
    String ARGUMENT_LISTING = "listingContext";

    /** Defines the order of the results set. */
    String ARGUMENT_ORDER_BY = "orderBy";

    /** The maximum number of items to return in the response. */
    String ARGUMENT_MAX_ITEMS = "maxItems";

    /** The number of objects to skip over before returning any results. */
    String ARGUMENT_SKIP_COUNT = "skipCount";

    String ARGUMENT_HAS_FILTER = "hasFilter";
}
