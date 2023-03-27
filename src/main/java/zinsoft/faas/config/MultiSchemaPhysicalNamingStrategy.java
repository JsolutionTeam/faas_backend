package zinsoft.faas.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@Component
public class MultiSchemaPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    // schema 값 별로 다른 catalog 값을 가져오기 위한 클래스
    private final ProjectDbSchemaProperties schemaConfiguration;

    public MultiSchemaPhysicalNamingStrategy(ProjectDbSchemaProperties schemaConfiguration) {
        this.schemaConfiguration = schemaConfiguration;
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name != null) {
            Identifier identifier = Identifier
                    .toIdentifier(
                            schemaConfiguration.getConfig().get(name.getText())
                            , name.isQuoted()
                    );
            return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
        }
        return null;
    }
}