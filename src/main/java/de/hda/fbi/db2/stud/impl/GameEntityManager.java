package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryDelegate;

public class GameEntityManager extends Lab02EntityManager {


  @Override
  public void persistData() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = null;
    List<Category> categories = (List<Category>) lab01Data.getCategories();
    List<Question> questions = (List<Question>) lab01Data.getQuestions();
    try {
      tx = em.getTransaction();
      tx.begin(); // Start der Transaktion
      // Laden, Verändern, Speichern von Daten …
      categories.forEach((category) -> em.persist(category));
      questions.forEach((question) -> em.persist(question));
      tx.commit(); // Beenden der Transaktion
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        tx.rollback(); // Rollback der Transaktion
      }
      throw e;
    } finally {
      em.close(); // Session wird geschlossen
    }
  }

  @Override
  public EntityManager getEntityManager() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
    EntityManager em = emf.createEntityManager();

    return em;
  }
}
