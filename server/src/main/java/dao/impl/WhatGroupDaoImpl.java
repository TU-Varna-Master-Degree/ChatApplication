package dao.impl;

import dao.WhatGroupDao;
import domain.dto.WhatGroupDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class WhatGroupDaoImpl implements WhatGroupDao {
    private EntityManager entityManager;

    public WhatGroupDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<WhatGroupDto> WhatGroup(long id){
        String sel="select new domain.dto.WhatGroupDto(gr.name, gr.creationDate) " +
                "from Group gr Where gr.id=:id";

        Query query= entityManager.createQuery(sel);
        query.setParameter("id",id);
        List<WhatGroupDto> list= query.getResultList();

        return list;


    };
}
