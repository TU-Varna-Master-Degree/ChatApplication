package dao;

import domain.client.dto.WhatGroupDto;

import java.util.List;

public interface WhatGroupDao {
    public List<WhatGroupDto> WhatGroup(long id);
}
