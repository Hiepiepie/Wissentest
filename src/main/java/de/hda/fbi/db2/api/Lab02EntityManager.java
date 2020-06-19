package de.hda.fbi.db2.api;

import javax.persistence.EntityManager;

/**
 * API Class for lab02 Created by l.koehler on 05.08.2019.
 */
public abstract class Lab02EntityManager {

  public void init() {
  }

  /**
   * You can use the data from lab01, this variable will be automatically set.
   */
  protected Lab01Data lab01Data;

  /**
   * Setter for Lab01Data; Don't overwrite, you will break the automation.
   *
   * @param lab01Data lab01Data
   */
  public void setLab01Data(Lab01Data lab01Data) {
    this.lab01Data = lab01Data;
  }

  /**
   * There you have to persist the data in the database.
   */
  public abstract void persistData();

  /**
   * Return a valid EntityManager.
   *
   * @return EntityManager
   */
  public abstract EntityManager getEntityManager();
}
