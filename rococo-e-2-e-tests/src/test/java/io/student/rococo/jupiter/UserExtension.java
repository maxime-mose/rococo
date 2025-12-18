package io.student.rococo.jupiter;

import io.student.rococo.model.UserJson;
import io.student.rococo.service.UsersClient;
import io.student.rococo.service.UsersDbClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;


public class UserExtension implements BeforeEachCallback, ParameterResolver {

  private static final Faker FAKER = new Faker();
  private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  private final UsersClient usersClient = new UsersDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class).ifPresent(anno ->
        context.getStore(NAMESPACE)
            .put(context.getUniqueId(), usersClient.createUser(FAKER.credentials().username(), FAKER.credentials().password(3, 12))));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
  }
}
