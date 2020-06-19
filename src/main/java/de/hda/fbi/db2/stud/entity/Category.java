package de.hda.fbi.db2.stud.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "Category", schema = "Praktikum")
public class Category {

  @Id
  @GeneratedValue
  @OrderBy
  private int id;
  @Column(unique = true)
  private String name;
  @OneToMany(mappedBy = "category")
  private List<Question> questions;

  public Category(){
  }

  public Category(String name) {
    this.name = name;
    this.questions = new ArrayList<>();
  }

  public void addQuestion(Question question) {
    questions.add(question);
  }

  public String getName() {
    return name;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Category)) {
      return false;
    }
    Category category = (Category) o;
    return getId() == category.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}
