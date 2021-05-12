package com.pum.it.pum3d.kernel.controller;

import com.pum.it.pum3d.kernel.dao.IncrementDao;
import com.pum.it.pum3d.kernel.model.entity.Increment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class IncrementController {

  static final Logger LOGGER = LogManager.getLogger(IncrementController.class);

  @Autowired
  private IncrementDao incrementDao;

  /**
   * Reset the increment entry to 0 in the database.
   * Initiliaze the entry if it doesn't exist.
   *
   * @return 200 response with 0 as body
   */
  @GetMapping("/reset")
  public ResponseEntity<Integer> resetCount() {
    LOGGER.info("request /reset");
    Increment increment = null;
    if (incrementDao.count() != 0) {
      increment = incrementDao.findAll().get(0);
    } else {
      increment = new Increment();
    }
    increment.setCount(0);
    incrementDao.save(increment);
    return new ResponseEntity<>(0, HttpStatus.OK);
  }

  /**
   * Increment the entry b y 1 in the database.
   * Initiliaze the entry if it doesn't exist.
   *
   * @return 200 response with the count value of entry as body
   */
  @GetMapping("/increment")
  public ResponseEntity<Integer> increment() {
    LOGGER.info("request /increment");
    Increment increment = null;
    if (incrementDao.count() != 0) {
      increment = incrementDao.findAll().get(0);
      increment.setCount(increment.getCount() + 1);
    } else {
      increment = new Increment();
      increment.setCount(1);
    }
    incrementDao.save(increment);
    return new ResponseEntity<>(increment.getCount(), HttpStatus.OK);
  }
}
