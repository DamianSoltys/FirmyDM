package local.project.Inzynierka.servicelayer.promotionitem.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RequiredContentToSocialMediaValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredTextContentWhenPostingToSocialMedia {
    String message() default "Promotion item should have non-html content when posted on social media.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
