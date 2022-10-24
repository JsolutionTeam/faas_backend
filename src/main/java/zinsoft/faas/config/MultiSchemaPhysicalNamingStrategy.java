package zinsoft.faas.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@Component
public class MultiSchemaPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    public static final String CAMEL_CASE_REGEX = "([a-z]+)([A-Z]+)";
    public static final String SNAKE_CASE_PATTERN = "$1\\_$2";
    
    private final ProjectDbSchemaProperties schemaConfiguration;

    public MultiSchemaPhysicalNamingStrategy(ProjectDbSchemaProperties schemaConfiguration) {
        this.schemaConfiguration = schemaConfiguration;
    }

//    @Override
//    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
//        if (name != null) {
//            Identifier identifier = super.getIdentifier(schemaConfiguration.getConfig()
//                    .get(name.getText()), name.isQuoted(), jdbcEnvironment);
//            return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
//        }
//        return name;
//    }

//    @Override
//    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
//        if (name != null) {
//            try {
//                return new Identifier(schemaConfiguration.getConfig()
//                        .get(name.getText()), name.isQuoted());
//            } catch (Exception ignored) {
//                return name;
//            }
//        }
//        return null;
//    }

//    @Override
//    public Identifier toPhysicalCatalogName(
//            Identifier name, JdbcEnvironment context) {
//        return formatIdentifier(
//                super.toPhysicalCatalogName(name, context)
//        );
//    }

        @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name != null) {
            Identifier identifier = Identifier.toIdentifier(schemaConfiguration.getConfig()
                    .get(name.getText()), name.isQuoted());
            return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
        }
        return null;
    }


//    @Override
//    public Identifier toPhysicalSchemaName(
//            Identifier name, JdbcEnvironment context) {
//        return formatIdentifier(
//                super.toPhysicalSchemaName(name, context)
//        );
//    }
//
//    @Override
//    public Identifier toPhysicalTableName(
//            Identifier name, JdbcEnvironment context) {
//        return formatIdentifier(
//                super.toPhysicalTableName(name, context)
//        );
//    }
//
//    @Override
//    public Identifier toPhysicalSequenceName(
//            Identifier name, JdbcEnvironment context) {
//        return formatIdentifier(
//                super.toPhysicalSequenceName(name, context)
//        );
//    }
//
//    @Override
//    public Identifier toPhysicalColumnName(
//            Identifier name, JdbcEnvironment context) {
//        return formatIdentifier(
//                super.toPhysicalColumnName(name, context)
//        );
//    }
//
//    private Identifier formatIdentifier(
//            Identifier identifier) {
//        if (identifier != null) {
//            String name = identifier.getText();
//
//            String formattedName = name.replaceAll(
//                    CAMEL_CASE_REGEX,
//                    SNAKE_CASE_PATTERN
//            ).toLowerCase();
//
//            return !formattedName.equals(name) ?
//                    Identifier.toIdentifier(
//                            formattedName,
//                            identifier.isQuoted()
//                    ) :
//                    identifier;
//        } else {
//            return null;
//        }
//    }

}