package de.hda.fbi.db2.api;

/**
 * Api for MassData.
 */
public abstract class Lab04MassData {

  public void init() {
  }

  /**
   * You can use the data from lab01, this variable will be automatically set.
   */
  protected Lab01Data lab01Data;

  /**
   * You can use the EntityManager or other stuff from lab02, this variable will be automatically
   * set.
   */
  protected Lab02EntityManager lab02EntityManager;

  /**
   * You can use the Lab03Game, this variable will be automatically set.
   */
  protected Lab03Game lab03Game;

  /**
   * Setter for Lab01Data.
   *
   * @param lab01Data lab01Data
   */
  public void setLab01Data(Lab01Data lab01Data) {
    this.lab01Data = lab01Data;
  }

  /**
   * Setter fro Lab02EntityManager.
   *
   * @param lab02EntityManager lab02EntityManager
   */
  public void setLab02EntityManager(Lab02EntityManager lab02EntityManager) {
    this.lab02EntityManager = lab02EntityManager;
  }

  /**
   * Setter fro lab03Game.
   *
   * @param lab03Game lab03Game
   */
  public void setLab03Game(Lab03Game lab03Game) {
    this.lab03Game = lab03Game;
  }

  public abstract void createMassData();
}
