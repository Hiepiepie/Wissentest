package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder.In;

public class GameApi extends Lab03Game {


  @Override
  public Object getOrCreatePlayer(String playerName) {
    EntityManager em = lab02EntityManager.getEntityManager();
    Player player = new Player(playerName);
    try {
      List playerResult = em.createQuery("select p from Player p "
          + "where p.name = :name").setParameter("name", playerName).getResultList();
      if (!playerResult.isEmpty()) {
        player = (Player) em.createQuery("select p from Player p "
            + "where p.name = :name").setParameter("name", playerName)
            .getSingleResult();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return player;
  }

  @Override
  public Object interactiveGetOrCreatePlayer() {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    System.out.println("Enter your name : ");
    String playerName = null;
    try {
      playerName = reader.readLine();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return getOrCreatePlayer(playerName);
  }

  @Override
  public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {
    EntityManager em = lab02EntityManager.getEntityManager();
    Random rand = new Random();
    List<Question> selectedQuestions = new ArrayList<>();
    for (Object o : categories) {
      Category category = (Category) o;
      List<Question> questions = new ArrayList<>();
      //get Questions from Database
      try {
        List resultL = em.createQuery("SELECT q FROM Question q WHERE q.category.id = :id")
            .setParameter("id", category.getId()).getResultList();
        for (Object oq : resultL) {
          questions.add((Question) oq);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (questions.size() <= amountOfQuestionsForCategory) {
        selectedQuestions.addAll(questions);
      } else {
        for (int i = 0; i < amountOfQuestionsForCategory; i++) {
          int randomIndex = rand.nextInt(questions.size());
          selectedQuestions.add(questions.get(randomIndex));
          questions.remove(randomIndex);
        }
      }
    }
    return selectedQuestions;
  }

  @Override
  public List<?> interactiveGetQuestions() {
    EntityManager em = lab02EntityManager.getEntityManager();
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    List<Category> categories = new ArrayList<>();
    List<Category> selectedCategories = new ArrayList<>();
    List resultL = null;

    //get Categories from Database
    try {
      resultL = em.createQuery("SELECT c FROM Category c").getResultList();
      for (Object o : resultL) {
        categories.add((Category) o);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // print all available categories
    System.out.println("\n List all available Categories : ");
    for (int i = 0; i < categories.size(); i++) {
      System.out.println((i + 1) + " " + categories.get(i).getName());
    }

    //insert at least 2 category to play
    Integer[] selectedInt = new Integer[1];
    while (selectedInt.length < 2) {
      System.out.println("Please choose at least 2 categories (example= 5 11 9): ");
      String selection = null;
      try {
        selection = reader.readLine();
        if (selection != null) {
          String[] selectedString = selection.split(" ");
          //set int array size
          selectedInt = new Integer[selectedString.length];
          //copy string to int array
          for (int i = 0; i < selectedString.length; i++) {
            selectedInt[i] = Integer.parseInt(selectedString[i]);
            //Category must not be < 0 or > 51
            if (selectedInt[i] < 1 || selectedInt[i] > categories.size()) {
              System.out.println("Category not found!");
              throw new Exception();
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Please insert numbers!");
        selectedInt = new Integer[1];
      }
    }

    // print chosen categories
    System.out.println("\nCategories you've chosen : ");
    for (int pos : selectedInt) {
      System.out.println(pos + " " + categories.get(pos - 1).getName());
      selectedCategories.add(categories.get(pos - 1));
    }

    int numberOfQuestions = -1;
    // ========== choose number of questions per category
    while (numberOfQuestions < 1) {
      System.out.println("\nPlease select number of question for each category (minimum 1) : ");
      while (true) {
        try {
          numberOfQuestions = Integer.parseInt(reader.readLine());
          break;
        } catch (Exception e) {
          System.out.println("Please insert a number!");
        }
      }
    }
    return getQuestions(selectedCategories, numberOfQuestions);
  }

  @Override
  public Object createGame(Object player, List<?> questions) {
    Game game = new Game((Player) player);
    Map<Question,Boolean> selectedQuestions = new HashMap<>();
    for (Object o : questions) {
      selectedQuestions.put((Question) o, false);
    }
    game.setSelectedQuestions(selectedQuestions);
    return game;
  }

  @Override
  public void playGame(Object game) {
    Random rand = new Random();
    Game gm = (Game) game;
    Map<Question, Boolean> selectedQuestions = gm.getSelectedQuestions();
//    gm.setStartTime();
    gm.setBothTimes();
    for (Question question : selectedQuestions.keySet()) {
      int answer = rand.nextInt(4) + 1;
      if (question.getCorrectAns() == answer) {  //correct
        System.out.println("Correct!");
        gm.getSelectedQuestions().put(question, true);
      } else {
        gm.getSelectedQuestions().put(question, false);
      }
    }
//    gm.setEndTime();
  }

  @Override
  public void interactivePlayGame(Object game) {
    System.out.println("\nGame started...... ");
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    Game gm = (Game) game;
    Map<Question, Boolean> selectedQuestions = gm.getSelectedQuestions();
    System.out.println("There are " + selectedQuestions.size()
        + " questions that need to be answered.\n");
    gm.setStartTime();
    int questionNum = 1;
    int correctAns = 0;
    for (Question question : selectedQuestions.keySet()) {
      System.out.println("Question number " + questionNum + " : " + question.getQuest());
      List<String> choices = question.getChoices();
      for (int i = 0; i < choices.size(); i++) {
        System.out.println((i + 1) + ". " + choices.get(i));
      }
      int answer;
      while (true) {
        System.out.println("Your answer (1-4) ? : ");
        try {
          answer = Integer.parseInt(reader.readLine());
          break;
        } catch (Exception e) {
          System.out.println("Input your answer with number from 1 to 4 !");
        }
      }
      if (question.getCorrectAns() == answer) {  //correct
        System.out.println("Correct!");
        gm.getSelectedQuestions().put(question, true);
        correctAns++;
      } else { //wrong
        System.out.println("Wrong! Correct answer was " + question.getCorrectAns());
        gm.getSelectedQuestions().put(question, false);
      }
      System.out.println();
      questionNum++;
    }
    System.out.println("Game Over.....");
    gm.setEndTime();
    System.out.println("Your Score : "
        + (double) correctAns / selectedQuestions.size() * 100 + "\n\n");
  }

  @Override
  public void persistGame(Object game) {
    Game gm = (Game) game;
    Player player = gm.getPlayer();

    EntityManager em = lab02EntityManager.getEntityManager();
    EntityTransaction tx = null;

    try {
      tx = em.getTransaction();
      tx.begin();
      List playerResult = em.createQuery("select p from Player p "
          + "where p.name = :name").setParameter("name", player.getName()).getResultList();

      // ========== persist player and game if new player
      if (playerResult.isEmpty()) {
        player.addGame(gm);
        em.persist(gm);
        em.persist(player);
      } else {  // ========== persist game and update existing player
        Player existPlayer = (Player) em.createQuery("select p from Player p "
            + "where p.name = :name").setParameter("name", player.getName())
            .getSingleResult();
        gm.setPlayer(existPlayer);
        existPlayer.addGame(gm);
        em.persist(gm);
      }
      tx.commit();  //================================================
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
      throw e;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
}

