package com.pum.it.pum3d.kernel.dao;

import com.pum.it.pum3d.kernel.model.entity.Increment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncrementDao extends MongoRepository<Increment, String> {
}
