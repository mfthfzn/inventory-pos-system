package io.github.mfthfzn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Name {

  @Column(name = "first_name", length = 50, nullable = false)
  private String firstName;

  @Column(name = "middle_name", length = 50, nullable = false)
  private String middleName;

  @Column(name = "last_name", length = 50, nullable = false)
  private String lastName;

  public String getFullName() {
    String fullName = firstName + " " + middleName + " " + lastName;
    return fullName.trim();
  }
}
