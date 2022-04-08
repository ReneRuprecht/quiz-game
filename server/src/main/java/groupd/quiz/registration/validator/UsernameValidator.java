package groupd.quiz.registration.validator;

import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;

@Configuration
public class UsernameValidator implements Predicate<String> {

    @Override
    public boolean test(String username) {
        return !username.isEmpty() && !username.contains(" ");

    }
}
