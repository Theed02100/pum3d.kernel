package com.pum.it.pum3d.kernel.service.impl;

import com.pum.it.pum3d.kernel.dao.ModuleDao;
import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.service.IModuleService;
import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ModuleServiceImpl implements IModuleService {

  @Autowired
  private ModuleDao moduleDao;

  @Override
  public void addModule(Module module) {
    moduleDao.save(module);
  }

  @Override
  public List<Module> findAllModulesSortedByLibelle() throws HttpResponseException {
    List<Module> modules = moduleDao.findAll(Sort.by(Sort.Direction.ASC, "libelle"));
    if (CollectionUtils.isEmpty(modules)) {
      throw new HttpResponseException(HttpStatus.NOT_FOUND);
    }
    return modules;
  }

  @Override
  public Module findById(String id) {
    Optional<Module> optionalModule = moduleDao.findById(id);
    return optionalModule.orElse(null);
  }
}
