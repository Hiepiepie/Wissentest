package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameData extends Lab01Data {
  List<Question> questions;
  List<Category> categories;
  HashMap<String, Category> categoriesMap;

  /**
   * Standard Contructor.
   *
   */
  public GameData() {
    questions = new ArrayList<>();
    categories = new ArrayList<>();
    categoriesMap = new HashMap<String, Category>();
  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

  @Override
  public List<Category> getCategories() {
    return categories;
  }

  @Override
  public void loadCsvFile(List<String[]> additionalCsvLines) {
    additionalCsvLines.remove(0);

    for (String[] line : additionalCsvLines) {
      //Parse Array seperate variables
      List<String> choices = Arrays.asList(line[2],line[3],line[4],line[5]);
      int questionId = Integer.parseInt(line[0]);
      String quest = line[1];
      int correctAns = Integer.parseInt(line[6]);

      //finds Category in the HashMap
      Category category = new Category(line[7]);
      if (categoriesMap.containsKey(line[7])) {
        category = categoriesMap.get(line[7]);
      } else {
        categoriesMap.put(line[7], category);
      }

      //new Question and add into QuestionList in DataManager and each Category.
      Question question =
          new Question(questionId,quest,choices,correctAns,category);
      questions.add(question);
      category.addQuestion(question);
    }

    //we convert the map to list
    categories = new ArrayList<>(categoriesMap.values());

    //Print all the Categories and Questions
    for (Category category : categories) {
      System.out.println("CATEGORY : " + category.getName());
      System.out.println("==================================");
      for (Question question : category.getQuestions()) {
        System.out.println(question.toString());
      }
      System.out.println();
    }
    System.out.println("Number of Categories : " + categoriesMap.size());
    System.out.println("Number of Questions : " + questions.size());
  }
}
