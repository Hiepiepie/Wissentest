package de.hda.fbi.db2.test;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.controller.Controller;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by l.koehler on 05.08.2019.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Lab03Test {

  private static Metamodel metaData;

  private static Controller controller;

  private static EntityType<?> gameEntity;

  private static synchronized void setGameEntity(EntityType<?> gameEntity) {
    Lab03Test.gameEntity = gameEntity;
  }

  /**
   * Lab03Test init.
   */
  @BeforeClass
  public static void init() {
    controller = Controller.getInstance();
    if (controller.getLab01Data() == null) {
      Assert.fail("Could not find Lab01Data Implementation");
      System.exit(-1);
    }
    if (controller.getLab02EntityManager() == null) {
      Assert.fail("Could not find Lab02EntityManager Implementation");
      System.exit(-1);
    }
    if (controller.getLab03Game() == null) {
      Assert.fail("Could not find Lab03Game Implementation");
      System.exit(-1);
    }

    if (!controller.isCsvRead()) {
      controller.readCsv();
    }

    if (!controller.isPersisted()) {
      controller.persistData();
    }

    try {
      if (controller.getLab02EntityManager().getEntityManager() == null) {
        Assert.fail("Lab02EntityManager.getEntityManager() returns null");
      }
      metaData = controller.getLab02EntityManager().getEntityManager().getMetamodel();
    } catch (Exception e) {
      Assert.fail("Exception during entityManager creation");
    }
  }

  @Test
  public void test1Functionality() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    Lab03Game gameController = controller.getLab03Game();
    Lab01Data lab01Data = controller.getLab01Data();
    List<Object> categories = new ArrayList<>();
    categories.add(lab01Data.getCategories().get(0));
    categories.add(lab01Data.getCategories().get(1));
    List<?> questions = gameController.getQuestions(categories, 2);
    Object player = gameController.getOrCreatePlayer("PlayerName");
    Object game = gameController.createGame(player, questions);
    gameController.playGame(game);
    gameController.persistGame(game);
  }

  @Test
  public void test2FindGameEntity() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    Lab03Game gameController = controller.getLab03Game();
    Lab01Data lab01Data = controller.getLab01Data();
    List<Object> categories = new ArrayList<>();
    categories.add(lab01Data.getCategories().get(0));
    categories.add(lab01Data.getCategories().get(1));
    List<?> questions = gameController.getQuestions(categories, 2);
    Object player = gameController.getOrCreatePlayer("PlayerName");
    Object game = gameController.createGame(player, questions);

    boolean gameFound = false;
    for (EntityType<?> clazzes : metaData.getEntities()) {
      if (clazzes.getJavaType() == game.getClass()) {
        gameFound = true;
        setGameEntity(clazzes);
      }
    }

    if (!gameFound) {
      Assert.fail("Could not find game class as entity");
    }
  }

  @Test
  public void test3FindPlayerEntity() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    Lab03Game gameController = controller.getLab03Game();
    Object player = gameController.getOrCreatePlayer("PlayerName");

    boolean playerFound = false;
    for (EntityType<?> clazzes : metaData.getEntities()) {
      if (clazzes.getJavaType() == player.getClass()) {
        playerFound = true;
      }
    }

    if (!playerFound) {
      Assert.fail("Could not find player class as entity");
    }
  }
}
