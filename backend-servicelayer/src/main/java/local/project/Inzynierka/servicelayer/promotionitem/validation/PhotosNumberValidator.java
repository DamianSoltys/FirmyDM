package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhotosNumberValidator implements ConstraintValidator<ZeroOrMaximum5Photos, PromotionItemAddedEvent> {
    @Override
    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return value.getNumberOfPhotos() == null ||
                (value.getNumberOfPhotos() != null && integerInRange0to5(value.getNumberOfPhotos()));
    }

    private boolean integerInRange0to5(Integer number) {
       return number >= 0 && number <=5;
    }
}
