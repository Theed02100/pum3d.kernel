package com.pum.it.pum3d.kernel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KernelProperties {

  @Value("${admin.api.key}")
  private String adminApiKey;

  public String getAdminApiKey() {
    return adminApiKey;
  }

  public void setAdminApiKey(String adminApiKey) {
    this.adminApiKey = adminApiKey;
  }
}
