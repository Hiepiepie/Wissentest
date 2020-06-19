package de.hda.fbi.db2.test;

import de.hda.fbi.db2.controller.Controller;
import java.lang.reflect.Method;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import org.eclipse.persistence.internal.jpa.metamodel.AttributeImpl;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;
import org.eclipse.persistence.internal.jpa.metamodel.SingularAttributeImpl;
import org.eclipse.persistence.mappings.AggregateCollectionMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectCollectionMapping;
import org.eclipse.persistence.mappings.DirectMapMapping;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by l.koehler on 05.08.2019.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Lab02Test {

  private static Metamodel metaData;

  private static EntityTypeImpl categoryEntity;

  private static EntityTypeImpl questionEntity;

  private static EntityTypeImpl answerEntity;

  private static Controller controller;

  private static synchronized void setMetaData(Metamodel metaData) {
    Lab02Test.metaData = metaData;
  }

  private static synchronized void setCategoryEntity(EntityTypeImpl categoryEntity) {
    Lab02Test.categoryEntity = categoryEntity;
  }

  private static synchronized void setQuestionEntity(EntityTypeImpl questionEntity) {
    Lab02Test.questionEntity = questionEntity;
  }

  private static synchronized void setAnswerEntity(EntityTypeImpl answerEntity) {
    Lab02Test.answerEntity = answerEntity;
  }

  /**
   * Lab02Test init.
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

    if (!controller.isCsvRead()) {
      controller.readCsv();
    }

    if (!controller.isPersisted()) {
      controller.persistData();
    }
  }

  @Test
  public void test1EntityManager() {
    try {
      if (controller.getLab02EntityManager().getEntityManager() == null) {
        Assert.fail("Lab02EntityManager.getEntityManager() returns null");
      }
      setMetaData(controller.getLab02EntityManager().getEntityManager().getMetamodel());
    } catch (Exception e) {
      Assert.fail("Exception during entityManager creation");
    }
  }

  @Test
  public void test2FindCategory() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    for (EntityType current : metaData.getEntities()) {
      if (current.getName().toLowerCase().equals("category")) {
        setCategoryEntity((EntityTypeImpl) current);
        return;
      }
    }
    Assert.fail("Could not find category entity");
  }

  @Test
  public void test3FindQuestion() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    for (EntityType current : metaData.getEntities()) {
      if (current.getName().toLowerCase().equals("question")) {
        setQuestionEntity((EntityTypeImpl) current);
        return;
      }
    }
    Assert.fail("Could not find question entity");
  }

  @Test
  public void test4FindAnswer() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (questionEntity == null) {
      Assert.fail("Could not find questionEntity");
    }

    for (Object member : questionEntity.getAttributes()) {
      DatabaseMapping mapping = ((AttributeImpl) member).getMapping();
      if (mapping instanceof DirectMapMapping) {
        return;
      }
      if (mapping instanceof AggregateCollectionMapping) {
        return;
      }
      if (mapping instanceof DirectCollectionMapping) {
        return;
      }
    }
    Assert.fail("Could not find a possible answer constellation in question");
  }

  @Test
  public void test5AnswerStringsInQuestion() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (questionEntity == null) {
      Assert.fail("Could not find questionEntity");
    }

    int stringCount = 0;
    for (Object member : questionEntity.getAttributes()) {
      if (member instanceof SingularAttribute) {
        if (((SingularAttribute) member).getType().getJavaType() == String.class) {
          stringCount = stringCount + 1;
        }
      }
    }

    if (stringCount > 2) {
      Assert.fail("Found more then 2 Strings in Question!");
    }
  }

  @Test
  public void test6QuestionInCategory() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (questionEntity == null || categoryEntity == null) {
      Assert.fail("Could not find questionEntity or categoryEntity");
    }

    for (Object member : categoryEntity.getAttributes()) {
      try {
        Type type = ((PluralAttribute) member).getElementType();
        if (type == questionEntity) {
          return;
        }
      } catch (Exception ignored) {
        //This is expected
      }
    }

    Assert.fail("Could not find questions in category");
  }

  @Test
  public void test7EqualsFunction() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (questionEntity == null || categoryEntity == null) {
      Assert.fail("Could not find questionEntity or categoryEntity");
    }

    boolean foundEquals = false;
    for (Method method : questionEntity.getJavaType().getMethods()) {
      if (method.getName().equals("equals") && !method.getDeclaringClass().getSimpleName()
          .equals("Object")) {
        foundEquals = true;
        break;
      }
    }

    if (!foundEquals) {
      Assert.fail("Could not find equals method in question entity");
    }

    foundEquals = false;
    for (Method method : categoryEntity.getJavaType().getMethods()) {
      if (method.getName().equals("equals") && !method.getDeclaringClass().getSimpleName()
          .equals("Object")) {
        foundEquals = true;
        break;
      }
    }

    if (!foundEquals) {
      Assert.fail("Could not find equals method in category entity");
    }

    if (answerEntity != null) {
      foundEquals = false;
      for (Method method : answerEntity.getJavaType().getMethods()) {
        if (method.getName().equals("equals") && !method.getDeclaringClass().getSimpleName()
            .equals("Object")) {
          foundEquals = true;
          break;
        }
      }

      if (!foundEquals) {
        Assert.fail("Could not find equals method in answer entity");
      }
    }

    Object question1 = controller.getLab01Data().getQuestions().get(0);
    Object question2 = controller.getLab01Data().getQuestions().get(1);
    if (question1.equals(question2)) {
      Assert.fail("Two different questions should be equal");
    }
  }

  @Test
  public void test8HashCodeFunction() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (questionEntity == null || categoryEntity == null) {
      Assert.fail("Could not find questionEntity or categoryEntity");
    }

    boolean foundEquals = false;
    for (Method method : questionEntity.getJavaType().getMethods()) {
      if (method.getName().equals("hashCode") && !method.getDeclaringClass().getSimpleName()
          .equals("Object")) {
        foundEquals = true;
        break;
      }
    }

    if (!foundEquals) {
      Assert.fail("Could not find hashCode method in question entity");
    }

    foundEquals = false;
    for (Method method : categoryEntity.getJavaType().getMethods()) {
      if (method.getName().equals("hashCode") && !method.getDeclaringClass().getSimpleName()
          .equals("Object")) {
        foundEquals = true;
        break;
      }
    }

    if (!foundEquals) {
      Assert.fail("Could not find hashCode method in category entity");
    }

    if (answerEntity != null) {
      foundEquals = false;
      for (Method method : answerEntity.getJavaType().getMethods()) {
        if (method.getName().equals("hashCode") && !method.getDeclaringClass().getSimpleName()
            .equals("Object")) {
          foundEquals = true;
          break;
        }
      }

      if (!foundEquals) {
        Assert.fail("Could not find hashCode method in answer entity");
      }
    }
  }

  @Test
  public void test9CategoryNameUnique() {
    if (metaData == null) {
      Assert.fail("No MetaModel");
    }

    if (categoryEntity == null) {
      Assert.fail("Could not find categoryEntity");
    }

    for (Object member : categoryEntity.getAttributes()) {
      System.out.println();
      try {
        String name = ((SingularAttributeImpl) member).getName();
        if (name.equals("name")) {
          boolean isUnique = ((SingularAttributeImpl) member).getMapping().getField().isUnique();
          if (!isUnique) {
            Assert.fail("Name in Category is not unique");
          } else {
            return;
          }
        }
      } catch (Exception ignored) {
        //This is expected
      }
    }
    Assert.fail("Could not find name in category");
  }
}
