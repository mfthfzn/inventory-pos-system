package io.github.mfthfzn.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Name {

  private String firstName;

  private String middleName;

  private String lastName;

}
