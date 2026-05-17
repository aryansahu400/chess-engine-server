package in.aryaura.chess.engine.server.annotations;


import in.aryaura.chess.engine.server.validation.FenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FenValidator.class)
public @interface ValidFen {

    String message() default "Invalid FEN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    boolean strict() default true;
}
