/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.uaa.admin.impl;

import org.ameba.i18n.Translator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openwms.core.AbstractMockitoTests;
import org.openwms.core.uaa.admin.impl.Grant;
import org.openwms.core.uaa.admin.impl.RoleRepository;
import org.openwms.core.uaa.admin.impl.SecurityObject;
import org.openwms.core.uaa.admin.impl.SecurityObjectRepository;
import org.openwms.core.uaa.admin.impl.SecurityServiceImpl;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * A SecurityServiceTest.
 *
 * @author Heiko Scherrer
 * @version $Revision: $
 * @since 0.1
 */
public class SecurityServiceTest extends AbstractMockitoTests {

    @Mock
    private SecurityObjectRepository dao;
    @Mock
    private RoleRepository roleDao;
    @Mock
    private MessageSource messageSource;
    @Mock
    private Translator translator;
    @InjectMocks
    private SecurityServiceImpl srv;

    public @Rule ExpectedException thrown = ExpectedException.none();

    /**
     * Setting up some test data.
     */
    @Before
    public void onBefore() {
        when(messageSource.getMessage(anyString(), new Object[]{anyObject()}, any(Locale.class))).thenReturn("");
    }

    /**
     * Test to call service with null.
     */
    public
    @Test
    final void testMergeGrantsWithNull() {
        thrown.expect(IllegalArgumentException.class);
        srv.mergeGrants(null, null);
    }

    /**
     * Test method for {@link SecurityServiceImpl#mergeGrants(java.lang.String, java.util.List)} .
     * <p>
     * Add a new Grant.
     */
    @Ignore
    @Test
    public final void testMergeGrantsNew() {
        // prepare data
        Grant testGrant = new Grant("TMS_NEW");

        // preparing mocks
        when(dao.findAllOfModule("TMS%")).thenReturn(createGrants("TMS_KEY1"));
//        when(dao.merge(testGrant)).thenReturn(testGrant);

        // do test
        List<Grant> result = srv.mergeGrants("TMS", createGrants("TMS_NEW"));

        // verify mock invocations
        // New Grant should be added...
//        verify(dao).merge(testGrant);
        // verify the the new Grant is not in the list of removed Grants...
        verify(dao, never()).deleteAll(Collections.singletonList(testGrant));
        // But the existing Grant has been removed, because it is not in the
        // list of Grants to merge...
        verify(dao).deleteAll(Collections.singletonList(new Grant("TMS_KEY1")));

        // check the results
        assertEquals(1, result.size());
        assertTrue(result.contains(testGrant));
    }

    /**
     * Test method for {@link SecurityServiceImpl#mergeGrants(java.lang.String, java.util.List)} .
     * <p>
     * Merge existing Grants.
     */
    @Test
    public final void testMergeGrantsExisting() {
        // prepare data
        Grant testGrant = new Grant("TMS_NEW");

        // preparing mocks
        when(dao.findAllOfModule("TMS%")).thenReturn(createGrants("TMS_NEW"));
        //  when(dao.merge(testGrant)).thenReturn(testGrant);
        when(translator.translate(any())).thenReturn("");

        // do test
        List<Grant> result = srv.mergeGrants("TMS", createGrants("TMS_NEW"));

        // verify mock invocations
        // new Grant should be added...
        //    verify(dao, never()).merge(testGrant);
        // verify the new Grant is not in the list of removed Grants...
        verify(dao, never()).deleteAll(Collections.singletonList(testGrant));

        // check the results
        assertEquals(1, result.size());
        assertTrue(result.contains(testGrant));
    }

    private List<Grant> createGrants(String... names) {
        List<Grant> result = new ArrayList<>(names.length);
        for (String name : names) {
            result.add(new Grant(name));
        }
        return result;
    }

    private List<SecurityObject> createSecurityObjects(String... names) {
        List<SecurityObject> result = new ArrayList<>(names.length);
        for (String name : names) {
            result.add(new Grant(name));
        }
        return result;
    }
}