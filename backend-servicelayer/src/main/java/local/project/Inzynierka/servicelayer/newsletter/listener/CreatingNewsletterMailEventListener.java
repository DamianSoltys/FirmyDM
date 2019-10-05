package local.project.Inzynierka.servicelayer.newsletter.listener;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.NewsletterSubscriptionRepository;
import local.project.Inzynierka.servicelayer.newsletter.event.CreatingNewsletterMailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreatingNewsletterMailEventListener {

    private final JavaMailSender javaMailSender;

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    private final CompanyRepository companyRepository;

    @Autowired
    public CreatingNewsletterMailEventListener(JavaMailSender javaMailSender,
                                               NewsletterSubscriptionRepository newsletterSubscriptionRepository,
                                               CompanyRepository companyRepository) {
        this.javaMailSender = javaMailSender;
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
        this.companyRepository = companyRepository;
    }

    @Async
    @EventListener
    public void handleSendingNewsletterOut(CreatingNewsletterMailEvent event) {
        Company company = companyRepository.findById(event.getCompanyId()).
                orElseThrow(IllegalArgumentException::new);

        List<NewsletterSubscription> newsletterSubscriptions = newsletterSubscriptionRepository
                .findByCompanyAndVerified(company,true);

        newsletterSubscriptions.forEach(e -> {
            final SimpleMailMessage mailMessage = constructEmailMessage(event, e, company);
            javaMailSender.send(mailMessage);
        });

    }


    private SimpleMailMessage constructEmailMessage(CreatingNewsletterMailEvent creatingNewsletterMailEvent,
                                                    NewsletterSubscription newsletterSubscription,
                                                    Company company) {

        String companyName = company.getName();
        String recipient = newsletterSubscription.getEmailAddressEntity().getEmail();
        String signOutLink = creatingNewsletterMailEvent.getAppUrl() + "/newsletter/signout/" +
                newsletterSubscription.getUnsubscribeToken().getToken();

        String message = creatingNewsletterMailEvent.getMessage() + "\r\n\r\n" +
                     "\r\n\r\n" + "Aby wypisać się z listy newslettera, kliknij tu:\r\n"+signOutLink;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("test@example.com");
        simpleMailMessage.setSubject(creatingNewsletterMailEvent.getSubject() + " Newsletter -" + companyName);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setText(message);

        return simpleMailMessage;
    }
}
