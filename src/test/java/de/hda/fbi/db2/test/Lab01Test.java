package de.hda.fbi.db2.test;

import static org.junit.Assert.assertEquals;

import de.hda.fbi.db2.controller.Controller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by l.koehler on 05.08.2019.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Lab01Test {

  private static Controller controller;

  /**
   * Lab01Test init.
   */
  @BeforeClass
  public static void init() {
    controller = Controller.getInstance();
    if (controller.getLab01Data() == null) {
      Assert.fail("Could not find Lab01Data Implementation");
      System.exit(-1);
    }
    if (!controller.isCsvRead()) {
      controller.readCsv();
    }
  }

  @Test
  public void test1CategorySize() {
    assertEquals("There should be 51 categories", 51,
        controller.getLab01Data().getCategories().size());
  }

  @Test
  public void test2QuestionSize() {
    assertEquals("There should be 200 questions", 200,
        controller.getLab01Data().getQuestions().size());
  }

  @Test
  public void test3CategoryObject() {
    Object testObject = controller.getLab01Data().getCategories().get(0);
    assertEquals("Category object should be named 'Category'", "Category",
        testObject.getClass().getSimpleName());
  }

  @Test
  public void test4QuestionObject() {
    Object testObject = controller.getLab01Data().getQuestions().get(0);
    assertEquals("Question object should be named 'Question'", "Question",
        testObject.getClass().getSimpleName());
  }
}
