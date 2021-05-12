package com.pum.it.pum3d.kernel.dao;

import com.pum.it.pum3d.kernel.model.entity.Module;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModuleDao extends MongoRepository<Module, String> {
}
