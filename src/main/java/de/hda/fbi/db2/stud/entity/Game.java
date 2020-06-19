package de.hda.fbi.db2.stud.entity;

import java.util.*;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "Game", schema = "Praktikum")
public class Game {

  @Id
  @GeneratedValue
  @OrderBy
  private int id;

  @ManyToOne
  private Player player;

  @ElementCollection
  @CollectionTable(
      name = "selectedQuestion",
      joinColumns = @JoinColumn(name = "gameId"),
      schema = "Praktikum")
  @MapKeyColumn(name = "questionId")
  @Column(name = "selectedAnswer")
  private Map<Question, Boolean> selectedQuestions;

  private Date startTime;
  private Date endTime;

  public Game() {

  }

  public Game(Player player) {
    this.player = player;
  }

  public int getId() {
    return id;
  }

  public Player getPlayer() {
    return player;
  }

  public Map<Question, Boolean> getSelectedQuestions() {
    return selectedQuestions;
  }

  public Date getStartTime() {
    return startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public void setSelectedQuestions(
      Map<Question, Boolean> selectedQuestions) {
    this.selectedQuestions = selectedQuestions;
  }

  public void setStartTime() {
    this.startTime = new Date();
  }

  public void setEndTime() {
    this.endTime = new Date();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Game)) {
      return false;
    }
    Game game = (Game) o;
    return getId() == game.getId();
  }
  // help function to create random number
  private static int getRandomNumber(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  // create both Start and End time
  public void setBothTimes(){
    final int YEAR = 120;
    int day = getRandomNumber(8,24);
    int hourStart = getRandomNumber(0,23);
    int minuteStart = getRandomNumber(0,59);
    int secStart = getRandomNumber(0,59);
    int playtime = getRandomNumber(300,500);

    this.startTime= new Date(YEAR, Calendar.JUNE, day,hourStart,minuteStart,secStart);
    Calendar c = Calendar.getInstance();
    c.setTime(startTime);
    c.add(Calendar.SECOND,playtime);
    this.endTime = c.getTime();

  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}
