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

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Optional;

/**
 * A RoleServiceImpl is a Spring managed transactional service that deals with {@link Role}s.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final BeanMapper mapper;

    RoleServiceImpl(RoleRepository repository, BeanMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Collection<Role> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Role save(Role role) {
        Optional<Role> roleOpt = repository.findByName(role.getName());
        return roleOpt.map(value -> repository.save(mapper.mapFromTo(role, value))).
                orElseGet(() -> repository.save(role));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotEmpty String pKey) {
        repository.deleteByPKey(pKey);
    }
}