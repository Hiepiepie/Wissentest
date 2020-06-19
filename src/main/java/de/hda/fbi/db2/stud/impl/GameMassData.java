package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;

import javax.persistence.EntityManager;
import java.util.*;
import javax.persistence.EntityTransaction;

public class GameMassData extends Lab04MassData {

    // help function to create random number
    private static int getRandomNumber(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public void createMassData() {
        final int playerSize = 10000;
        final int gameSize = 100;
        final String playerName = "Player_";
        Random random = new Random();

        EntityManager em = lab02EntityManager.getEntityManager();
        EntityTransaction tx = null;

        Map<Integer, Category> categories = new HashMap<>();

        // fetch categories from DB
        List categoriesDB = em.createQuery("select c from Category c").getResultList();
        int indexCounter = 0;
        for (Object o : categoriesDB){
            categories.put(indexCounter,(Category) o);
            indexCounter++;
        }

        Date d = new Date();
        for (int i = 0; i < playerSize ; i++) {
            //create distinct player's name
            Player player = new Player(playerName + i);
            for (int k = 0; k < gameSize; k++) {
                int amountQuestions = getRandomNumber(3, 4);
                int amountCategories = getRandomNumber(4, 5);

                Map<Integer, Category> copyCategories = categories;
                List<Category> selectedCategories = new ArrayList<>();
                Map<Question, Boolean> selectedQuestions = new HashMap<>();

                //random the categories
                for (int c = 0; i < amountCategories; i++) {
                    int randomIndex = random.nextInt(copyCategories.size());
                    selectedCategories.add(copyCategories.get(randomIndex));
                    copyCategories.remove(randomIndex);
                }

                //random the questions
                for (Object o : copyCategories.values()) {
                    Category category = (Category) o;
                    List<Question> questions = category.getQuestions();
                    if (questions.size() <= amountQuestions) {
                        for (Object ob : questions) {
                            selectedQuestions.put((Question) ob, random.nextBoolean());
                        }
                    } else {
                        for (int j = 0; j < amountQuestions; j++) {
                            int randomIndex = random.nextInt(questions.size());
                            selectedQuestions.put(questions.get(randomIndex), random.nextBoolean());
                            questions.remove(randomIndex);
                        }
                    }
                }
                //create a game
                Game game = new Game(player);
                game.setSelectedQuestions(selectedQuestions);
                game.setBothTimes();

                //add game in player
                player.addGame(game);
            }
            //persist each player and its game
            em = lab02EntityManager.getEntityManager();
            tx = null;
            try {
                tx = em.getTransaction();
                tx.begin();
                em.persist(player);
                List<Game> games = player.getGames();
                for (Object ob : games) {
                    em.persist((Game) ob);
                }
                tx.commit();
                System.out.println("new Player added");
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
        Date t = new Date();
        System.out.println("Start " + d + "\nEnd "+ t );
    }
}

