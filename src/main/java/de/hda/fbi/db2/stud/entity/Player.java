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
@Table(name = "Player", schema = "Praktikum")
public class Player {
  @Id
  @GeneratedValue
  @OrderBy
  private int id;

  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "player")
  private List<Game> games;

  public Player() {

  }

  public Player(String name) {
    this.name = name;
    games = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public List<Game> getGames() {
    return games;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Player)) {
      return false;
    }
    Player player = (Player) o;
    return getId() == player.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public void addGame(Game game) {
    this.games.add(game);
  }
}
