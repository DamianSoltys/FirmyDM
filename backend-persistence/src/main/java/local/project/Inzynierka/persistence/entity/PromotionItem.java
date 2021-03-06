package local.project.Inzynierka.persistence.entity;

import local.project.Inzynierka.persistence.common.FullTimestampingAudit;
import local.project.Inzynierka.persistence.constants.SendingStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "promotion_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionItem extends FullTimestampingAudit implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_item_id")
    private Long id = 0L;

    @Column(name = "name")
    private String name;

    @Column(name = "email_title")
    private String emailTitle;

    @Column(name = "html_content", length = 16777215)
    private String htmlContent;

    @Column(name = "non_html_content", length = 16777215)
    private String nonHtmlContent;

    @Column(name = "photos_number", nullable = false)
    private Integer numberOfPhotos;

    @ManyToOne
    @JoinColumn(name = "promoting_company_id", nullable = false, foreignKey = @ForeignKey(name = "promoting_company_FK"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "promotion_type_id", nullable = false, foreignKey = @ForeignKey(name = "promotion_type_FK"))
    private PromotionItemType promotionItemType;

    @Column(name = "planned_sending_at", columnDefinition = "TIMESTAMP")
    private Timestamp plannedSendingAt;

    @Column(name = "sending_strategy", nullable = false)
    private String sendingStrategy;

    @Column(name = "adding_completed", nullable = false)
    private Boolean addingCompleted;

    @Column(name = "promotion_item_uuid", unique = true, nullable = false)
    private String promotionItemUUID;

    @Override
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }

    public boolean hasAtWillSendingStrategy() {
        return SendingStrategy.AT_WILL.toString().equals(this.getSendingStrategy());
    }
}
