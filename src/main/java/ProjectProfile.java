import lombok.Getter;

import java.io.FileInputStream;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Properties;

public class ProjectProfile {


    public static final Properties properties = new Properties();


    @Getter
    private static final ProjectProfile instance = new ProjectProfile();


    static {
        try (FileInputStream fileInput = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fileInput);
        } catch (Exception ignored) {
        }
    }


    public String privateApiKey() {
        return instance.getOptionalValue("api.private.key", String.class)
                .orElseThrow(() -> new RuntimeException("API private key has to be specified."));
    }


    public String publicApiKey() {
        return instance.getOptionalValue("api.public.key", String.class)
                .orElseThrow(() -> new RuntimeException("API public key has to be specified."));
    }


    public String paymentPageUrl() {
        return instance.getOptionalValue("payment.page.url", String.class)
                .orElse("https://payment-page.solidgate.com/api/v1/");
    }


    public String paymentStatusUrl() {
        return instance.getOptionalValue("payment.status.url", String.class)
                .orElse("https://pay.solidgate.com/api/v1/");
    }


    private <T> T convertValue(String value, Class<T> type) {
        if (type == Boolean.class) {
            return type.cast(Boolean.parseBoolean(value));
        } else if (type == Integer.class) {
            return type.cast(Integer.parseInt(value));
        } else if (type == Double.class) {
            return type.cast(Double.parseDouble(value));
        } else if (type == String.class) {
            return type.cast(value);
        } else if (type == LocalTime.class) {
            return type.cast(LocalTime.parse(value));
        }
        throw new IllegalArgumentException(String.format("Unsupported type %s for value %s", type.getName(), value));
    }


    private <T> Optional<T> getOptionalValue(String key, Class<T> type) {
        // env goes first
        String value = System.getenv(key);

        // then properties
        if (value == null || value.isBlank()) {
            value = properties.getProperty(key);
        }

        return Optional.ofNullable(convertValue(value, type));
    }


}