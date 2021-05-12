package com.pum.it.pum3d.kernel.service;

import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import java.util.List;

public interface IModuleService {

  void addModule(Module module);

  List<Module> findAllModulesSortedByLibelle() throws HttpResponseException;

  Module findById(String id);
}
