package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import local.project.Inzynierka.shared.UserAccount;
import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
public class User extends FullTimestampingAudit implements IEntity<Long>, UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Builder.Default
    private Long id = 0L;

    @Column(nullable = false, name = "user_name", unique = true, length = 30)
    private String name;
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_natural_person", referencedColumnName = "id_natural_person", foreignKey = @ForeignKey(name = "natural_person_FK"))
    private NaturalPerson naturalPerson;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "id_email_address", referencedColumnName = "email_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "email_FK"))
    private EmailAddress emailAddressEntity;

    @Column(nullable = false, name = "account_type")
    private Short accountType;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "verification_token_id", referencedColumnName = "token_id", unique = true, foreignKey = @ForeignKey(name = "verify_user_token_FK"))
    private VerificationToken verificationToken;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    public User() {
    }

    public boolean hasRegisteredNaturalPerson() {
        return this.getNaturalPerson() != null;
    }

    /*
     *  The parameters order has to match the order of field's declarations in the class for lombok builder to work.
     * */
    public User(long id, String name, String passwordHash, NaturalPerson naturalPerson,
                EmailAddress emailAddressEntity, Short accountType,
                VerificationToken verificationToken, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.naturalPerson = naturalPerson;
        this.emailAddressEntity = emailAddressEntity;
        this.accountType = accountType;
        this.isEnabled = isEnabled;
        this.verificationToken = verificationToken;
    }

    public User(String name, String passwordHash, EmailAddress emailAddressEntity) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.emailAddressEntity = emailAddressEntity;
    }

    @Override
    public String getEmail() {
        return getEmailAddressEntity().getEmail();
    }

    @Override
    public String getLoginName() {
        return getName();
    }

    @Override
    public String getPassword() {
        return getPasswordHash();
    }

    @Override
    public Boolean isNaturalPersonRegistered() {
        return hasRegisteredNaturalPerson();
    }

    @Override
    public Long personId() {
        return getNaturalPerson().getId();
    }

    @Override
    public Boolean isEnabled() {
        return this.isEnabled;
    }
}
