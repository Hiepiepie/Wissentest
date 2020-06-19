package de.hda.fbi.db2.stud.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Question", schema = "Praktikum")
public class Question {
  @Id
  @OrderBy
  private int id;

  private String quest;
  @ElementCollection
  @CollectionTable(name = "AnsChoice", schema = "Praktikum")
  @OrderColumn(name = "index")
  private List<String> choices;
  private int correctAns;
  @ManyToOne
  private Category category;

  public Question() {

  }

  /**
   * Question's Constructor.
   *
   * @param id Question ID
   * @param quest Question's content
   * @param choices List of String from answer choices
   * @param correctAns int Correct Answer's index
   */
  public Question(int id, String quest, List<String> choices, int correctAns, Category category) {
    this.id = id;
    this.quest = quest;
    this.choices = choices;
    this.correctAns = correctAns;
    this.category = category;
  }

  public int getId() {
    return id;
  }

  public String getQuest() {
    return quest;
  }

  public List<String> getChoices() {
    return choices;
  }

  public int getCorrectAns() {
    return correctAns;
  }

  public Category getCategory() {
    return category;
  }

  @Override
  public String toString() {
    String result = id + ":" + quest + ":";
    result += choices.get(0) + ";";
    result += choices.get(1) + ";";
    result += choices.get(2) + ";";
    result += choices.get(3) + ";";
    result += correctAns;

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Question)) {
      return false;
    }
    Question question = (Question) o;
    return getId() == question.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
