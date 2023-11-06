package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField EMAIL = field("User", "email");
  public static final QueryField USERNAME = field("User", "username");
  public static final QueryField BIO = field("User", "bio");
  public static final QueryField TOKENS = field("User", "tokens");
  public static final QueryField AVATAR = field("User", "avatar");
  public static final QueryField PETS = field("User", "pets");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String") String username;
  private final @ModelField(targetType="String") String bio;
  private final @ModelField(targetType="Int") Integer tokens;
  private final @ModelField(targetType="String") String avatar;
  private final @ModelField(targetType="String") List<String> pets;
  private final @ModelField(targetType="Task") @HasMany(associatedWith = "userID", type = Task.class) List<Task> Tasks = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getUsername() {
      return username;
  }
  
  public String getBio() {
      return bio;
  }
  
  public Integer getTokens() {
      return tokens;
  }
  
  public String getAvatar() {
      return avatar;
  }
  
  public List<String> getPets() {
      return pets;
  }
  
  public List<Task> getTasks() {
      return Tasks;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private User(String id, String email, String username, String bio, Integer tokens, String avatar, List<String> pets) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.bio = bio;
    this.tokens = tokens;
    this.avatar = avatar;
    this.pets = pets;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getEmail(), user.getEmail()) &&
              ObjectsCompat.equals(getUsername(), user.getUsername()) &&
              ObjectsCompat.equals(getBio(), user.getBio()) &&
              ObjectsCompat.equals(getTokens(), user.getTokens()) &&
              ObjectsCompat.equals(getAvatar(), user.getAvatar()) &&
              ObjectsCompat.equals(getPets(), user.getPets()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getEmail())
      .append(getUsername())
      .append(getBio())
      .append(getTokens())
      .append(getAvatar())
      .append(getPets())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("bio=" + String.valueOf(getBio()) + ", ")
      .append("tokens=" + String.valueOf(getTokens()) + ", ")
      .append("avatar=" + String.valueOf(getAvatar()) + ", ")
      .append("pets=" + String.valueOf(getPets()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static EmailStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static User justId(String id) {
    return new User(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      email,
      username,
      bio,
      tokens,
      avatar,
      pets);
  }
  public interface EmailStep {
    BuildStep email(String email);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
    BuildStep username(String username);
    BuildStep bio(String bio);
    BuildStep tokens(Integer tokens);
    BuildStep avatar(String avatar);
    BuildStep pets(List<String> pets);
  }
  

  public static class Builder implements EmailStep, BuildStep {
    private String id;
    private String email;
    private String username;
    private String bio;
    private Integer tokens;
    private String avatar;
    private List<String> pets;
    public Builder() {
      
    }
    
    private Builder(String id, String email, String username, String bio, Integer tokens, String avatar, List<String> pets) {
      this.id = id;
      this.email = email;
      this.username = username;
      this.bio = bio;
      this.tokens = tokens;
      this.avatar = avatar;
      this.pets = pets;
    }
    
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          email,
          username,
          bio,
          tokens,
          avatar,
          pets);
    }
    
    @Override
     public BuildStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep username(String username) {
        this.username = username;
        return this;
    }
    
    @Override
     public BuildStep bio(String bio) {
        this.bio = bio;
        return this;
    }
    
    @Override
     public BuildStep tokens(Integer tokens) {
        this.tokens = tokens;
        return this;
    }
    
    @Override
     public BuildStep avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
    
    @Override
     public BuildStep pets(List<String> pets) {
        this.pets = pets;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String email, String username, String bio, Integer tokens, String avatar, List<String> pets) {
      super(id, email, username, bio, tokens, avatar, pets);
      Objects.requireNonNull(email);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder bio(String bio) {
      return (CopyOfBuilder) super.bio(bio);
    }
    
    @Override
     public CopyOfBuilder tokens(Integer tokens) {
      return (CopyOfBuilder) super.tokens(tokens);
    }
    
    @Override
     public CopyOfBuilder avatar(String avatar) {
      return (CopyOfBuilder) super.avatar(avatar);
    }
    
    @Override
     public CopyOfBuilder pets(List<String> pets) {
      return (CopyOfBuilder) super.pets(pets);
    }
  }
  

  public static class UserIdentifier extends ModelIdentifier<User> {
    private static final long serialVersionUID = 1L;
    public UserIdentifier(String id) {
      super(id);
    }
  }
  
}
