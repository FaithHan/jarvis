package org.jarvis.jdbc.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;


@Data
@Table("USER")
public class User {
  @Id
  private Long id;
  private Date createdTime;
  private Date updatedTime;
  @Column("DOB")
  private Date dateofBirth;
  @MappedCollection(idColumn = "CREDS_ID")
  private Credentials credentials;
}